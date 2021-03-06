package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import data.*;
import org.jongo.MongoCursor;
import play.libs.EventSource;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class AlgorithmController extends Controller {

    private class Individual {
        private double fitness;
        public HashSet<Point> genome;

        public Individual() {
            genome = new HashSet<>();
        }

        public double getFitness() {
            return this.fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }
    }

    private final int POPULATION_SIZE = 1000;
    private final double P_CROSS = 0.7;
    private final double P_MUT = 0.3;
    private final int MIN_POINTS = 5;
    private final int MAX_POINTS = 20;
    private final int ITERATIONS = 800;
    private final int POINTS_MULTIPLIER = 3;
    private final int MIN_POPULATION = (int) (POPULATION_SIZE * 0.1);
    private final int MAX_POPULATION = POPULATION_SIZE * 10;
    private final int SUBMIT_ON_ITERATION = 205;

    private double maxFitness;
    private double minFitness;
    private int totalPoints;
    private int pointsInTrip;
    private int totalRank;

    private ArrayList<Individual> population;
    private HashMap<String, Integer> constraints;
    private ArrayList<Point> genesArray;

    // Hash map of events for communication
    HashMap<String, ArrayList<EventSource>> events = new HashMap<>();

    public Result index() {
        int suggestedTrip = 1;

        // Getting group key
        final JsonNode values = request().body().asJson();
        String tripKey = values.get("groupKey").asText();

        // Initializing global variables
        maxFitness = 0;
        minFitness = Double.MAX_VALUE;
        totalPoints = 0;
        pointsInTrip = 0;
        totalRank = 0;

        MongoCursor<Amenity> cursorAmenities = Amenity.amenities();
        HashMap<String, String> mapAmenities = new HashMap<>();

        // Mapping amenities from display name to actual name in DB
        for (Amenity amenity : cursorAmenities) {
            mapAmenities.put(amenity.getDisplayName(), amenity.getName());
        }

        MongoCursor<Point> pointsCursor;
        constraints = new HashMap<>();
        Trip trip = Trip.findByKey(tripKey);
        User[] users = trip.getUsers();

        // Building map of constraints
        // For every user participating in the trip
        for (int i = 0; i < users.length; i++) {
            Pick[] picks = users[i].getRequirements();

            pointsInTrip += users[i].getPointsNumber();

            // If user entered any requirements
            if (picks != null) {
                // For every requirement of the user
                for (int j = 0; j < picks.length; j++) {

                    // If constraints already contain given amenity
                    if (constraints.containsKey(mapAmenities.get(picks[j].getAmenity()))) {
                        // Increase the rank
                        constraints.put(mapAmenities.get(picks[j].getAmenity()),
                                constraints.get(mapAmenities.get(picks[j].getAmenity())) +
                                        Integer.parseInt(picks[j].getRank()));
                    } else {
                        // Put new amenity
                        constraints.put(mapAmenities.get(picks[j].getAmenity()),
                                Integer.parseInt(picks[j].getRank()));
                    }

                    totalRank += Integer.parseInt(picks[j].getRank());
                }
            }
        }

        // Calculate average wanted number of points in trip
        pointsInTrip /= users.length;

        // If less than minimum points - set to minimum
        if (pointsInTrip < MIN_POINTS) {
            pointsInTrip = MIN_POINTS;
        } else if (pointsInTrip > MAX_POINTS) {
            pointsInTrip = MAX_POINTS;
        }

        // First result
        // *************************************************************** //
        Individual firstInd = new Individual();
        Point.getByAmenities(constraints.keySet(), pointsInTrip).forEach(point -> {
            firstInd.genome.add(point);
        });

        trip.addAnotherResult(new data.Result("Longest Trip", toTrack(firstInd.genome)));
        trip.insert();
        // *************************************************************** //

        totalPoints = POPULATION_SIZE * pointsInTrip * POINTS_MULTIPLIER;
        genesArray = Point.getByAmenities(constraints.keySet(), totalPoints);

        if (totalPoints > genesArray.size()) {
            totalPoints = genesArray.size();
        }

        Random rnd = new Random();
        population = new ArrayList<>();

        // Randomly generating individuals for the population
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual individual = new Individual();

            for (int j = 0; j < pointsInTrip; j++) {
                // Choosing random point as gene
                boolean unique = individual.genome.add(genesArray.get(rnd.nextInt(totalPoints)));

                // Dealing with duplicates in the genome
                while (!unique) {
                    unique = individual.genome.add(genesArray.get(rnd.nextInt(totalPoints)));
                }
            }

            // Calculate the individual fitness
            calcFitness(individual);
            updateGlobalFitness(individual);
            population.add(individual);
        }

        // Evolving the population
        for (int i = 0; i < ITERATIONS; i++) {
            evolution();

            if (i % SUBMIT_ON_ITERATION == 0) {
                // Submitting new result
                Individual best = bestIndividual();
                Point[] track = toTrack(best.genome);
                trip.addAnotherResult(new data.Result("Trip Suggestion: " + ++suggestedTrip, track));
                trip.insert();
            }
        }

        // Submitting final result
        Individual best = bestIndividual();
        Point[] track = toTrack(best.genome);
        trip.addAnotherResult(new data.Result("Optimal Trip", track));
        trip.setFinished(true);
        trip.insert();

        events.remove(tripKey);

        return ok();
    }

    // Register for event source
    public Result regEvent(String tripKey) {
        EventSource event = new EventSource() {
            @Override
            public void onConnected() {
                this.send(new Event("Connected to algorithm event source.", "alg", "alg"));
            }
        };

        ArrayList<EventSource> eventsArray;

        if (events.containsKey(tripKey)) {
            eventsArray = events.get(tripKey);
        } else {
            eventsArray = new ArrayList<>();
            events.put(tripKey, eventsArray);
        }

        eventsArray.add(event);

        return ok(event);
    }

    // Send event that algorithm has started to team members
    public Result sendEvent() {
        final JsonNode values = request().body().asJson();
        String tripKey = values.get("groupKey").asText();

        for (EventSource event : events.get(tripKey)) {
            event.send(new EventSource.Event("Algorithm started.", tripKey, tripKey));
        }

        return ok();
    }

    private void evolution() {
        double fitnessVariety = maxFitness - minFitness;
        double currMinFitness = minFitness;
        double selectionProbability;

        // Initializing min and max fitness to calculate
        // updated values for the next calculation
        maxFitness = 0;
        minFitness = Double.MAX_VALUE;

        Random rnd = new Random();
        ArrayList<Individual> toCrossover = new ArrayList<>();

        // Performing selection
        for (Iterator<Individual> it = population.iterator(); it.hasNext(); ) {
            Individual individual = it.next();

            // Selection probability of the individual
            selectionProbability = (individual.getFitness() - currMinFitness) / (fitnessVariety);

            // The individual is not selected (removed)
            // Only if the population is not too low
            if (population.size() > MIN_POPULATION &&
                    selectionProbability < rnd.nextDouble()) {
                it.remove();
            } else {
                if (population.size() < MAX_POPULATION &&
                        rnd.nextDouble() <= P_CROSS) {
                    // The individual is chosen for crossover
                    // Only if the population is not too high
                    toCrossover.add(individual);
                } else if (rnd.nextDouble() <= P_MUT) {
                    // Performing mutation
                    mutation(individual);
                } else {
                    // Calculating max and min population fitness for further calculations
                    updateGlobalFitness(individual);
                }
            }
        }

        // Performing crossover only if another pair is available
        while (toCrossover.size() >= 2) {
            int crossoverPoint = rnd.nextInt(pointsInTrip);
            Individual ind1, ind2;
            Individual newInd1, newInd2;

            // First individual for crossover
            ind1 = toCrossover.remove(rnd.nextInt(toCrossover.size()));

            // Second individual for crossover
            ind2 = toCrossover.remove(rnd.nextInt(toCrossover.size()));

            // Creating two new individuals from the crossover
            newInd1 = new Individual();
            newInd2 = new Individual();

            // Sharing first individual genome
            int i = 0;
            for (Point point : ind1.genome) {
                if (i < crossoverPoint) {
                    newInd1.genome.add(point);
                } else {
                    newInd2.genome.add(point);
                }
                i++;
            }

            // Sharing second individual genome
            i = 0;
            for (Point point : ind2.genome) {
                if (i < crossoverPoint) {
                    boolean unique = newInd2.genome.add(point);

                    // Dealing with duplicates in the genome
                    while (!unique) {
                        unique = newInd2.genome.add(genesArray.get(rnd.nextInt(totalPoints)));
                    }
                } else {
                    boolean unique = newInd1.genome.add(point);

                    // Dealing with duplicates in the genome
                    while (!unique) {
                        unique = newInd1.genome.add(genesArray.get(rnd.nextInt(totalPoints)));
                    }
                }
                i++;
            }

            // Calculating fitness of the new individuals
            calcFitness(newInd1);
            calcFitness(newInd2);

            // Adding the new individuals to the population
            population.add(newInd1);
            population.add(newInd2);

            // Calculating max and min population fitness for newInd1
            updateGlobalFitness(newInd1);

            // Calculating max and min population fitness for newInd2
            updateGlobalFitness(newInd2);

            // Mutating the old individuals
            if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(ind1);
            }

            // Calculating max and min population fitness for mutated ind2
            updateGlobalFitness(ind1);

            if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(ind2);
            }

            // Calculating max and min population fitness for mutated ind1
            updateGlobalFitness(ind2);
        }

        // If last individual left
        if (toCrossover.size() != 0) {
            if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(toCrossover.get(0));
                updateGlobalFitness(toCrossover.get(0));
            }
        }
    }

    private void mutation(Individual individual) {
        Random rnd = new Random();
        int geneToChange = rnd.nextInt(pointsInTrip);

        // Remove the old gene
        int i = 0;
        for (Point point : individual.genome) {
            if (i == geneToChange) {
                individual.genome.remove(point);
                break;
            }
            i++;
        }

        // Swap the old gene with a random new gene
        boolean unique = individual.genome.add(genesArray.get(rnd.nextInt(totalPoints)));

        // Dealing with duplicates in the genome
        while (!unique) {
            unique = individual.genome.add(genesArray.get(rnd.nextInt(totalPoints)));
        }

        // Calculate the new fitness
        calcFitness(individual);

        // Calculating max and min population fitness for further calculations
        updateGlobalFitness(individual);
    }

    private void calcFitness(Individual individual) {
        final int lengthMultiplier = 8;
        final int rankMultiplier = 5;
        final double basicFitness = 800;

        double minLong = Double.MAX_VALUE, minLat = Double.MAX_VALUE;
        double maxLong = -Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double fitness = 0;
        int multiplier = 1;
        HashMap<String, Integer> constraintsMet = new HashMap<>();

        // Hash map for calculating met constraints
        for (Map.Entry<String, Integer> entry : constraints.entrySet()) {
            constraintsMet.put(entry.getKey(), 0);
        }

        // For every point in the genome
        for (Point point : individual.genome) {
            // Calculating min and max longitude and latitude
            if (maxLong < point.getLongitude()) {
                maxLong = point.getLongitude();
            }
            if (minLong > point.getLongitude()) {
                minLong = point.getLongitude();
            }
            if (maxLat < point.getLatitude()) {
                maxLat = point.getLatitude();
            }
            if (minLat > point.getLatitude()) {
                minLat = point.getLatitude();
            }

            // If amenity is found in the constraints and the amenity
            // is not present too many times in the individual
            if (constraints.containsKey(point.getAmenity()) &&
                    constraintsMet.get(point.getAmenity()) <
                            Math.ceil(((double) constraints.get(point.getAmenity()) / totalRank) * pointsInTrip)) {

                // If a new constraint is met
                if (constraintsMet.get(point.getAmenity()) == 0) {
                    multiplier++;
                }

                // Increase the fitness
                fitness += constraints.get(point.getAmenity()) * rankMultiplier;

                // Increase constraints met for given amenity
                constraintsMet.put(point.getAmenity(), constraintsMet.get(point.getAmenity()) + 1);
            }
        }

        // Multiply by the amount of constraints met and
        // divide according to difference in distance
        fitness = (basicFitness + (fitness * multiplier)) /
                (lengthMultiplier * ((maxLong - minLong) + (maxLat - minLat)));

        individual.setFitness(fitness);
    }

    private Individual bestIndividual() {
        double bestFitness = -1;
        Individual bestInd = null;

        // Searching for the fittest individual
        for (Individual ind : population) {
            if (bestFitness < ind.fitness) {
                bestInd = ind;
                bestFitness = ind.fitness;
            }
        }

        return bestInd;
    }

    private void updateGlobalFitness(Individual individual) {
        // Calculating max and min population fitness for further calculations
        if (maxFitness < individual.fitness) {
            maxFitness = individual.fitness;
        }
        if (minFitness > individual.fitness) {
            minFitness = individual.fitness;
        }
    }

    private Point[] toTrack(HashSet<Point> genome) {
        double minDist, dist;
        Point currPoint, nextPoint = null;
        Point[] track = new Point[pointsInTrip];

        // Copying the hash set to not to make changes in the original one
        HashSet<Point> tempGenome = new HashSet<>(genome);

        // Calculating the starting point
        currPoint = calcStartingPoint(tempGenome);

        // Adding starting point
        track[0] = currPoint;
        tempGenome.remove(currPoint);

        // Compute points for the track
        for (int i = 1; i < track.length; i++) {
            minDist = Double.MAX_VALUE;

            // For all the remaining points of genome
            for (Point point : tempGenome) {
                // Calculate manhattan distance
                dist = Math.abs(point.getLongitude() - currPoint.getLongitude()) +
                        Math.abs(point.getLatitude() - currPoint.getLatitude());

                // If the distance is minimal
                if (dist < minDist) {
                    minDist = dist;
                    nextPoint = point;
                }
            }

            // Add the closest point to track
            track[i] = nextPoint;
            tempGenome.remove(nextPoint);
            currPoint = nextPoint;
        }

        return track;
    }

    private Point calcStartingPoint(HashSet<Point> genome) {
        double minLong = Double.MAX_VALUE, minLat = Double.MAX_VALUE;
        double maxLong = -Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        int currRank = 0;
        Point left = null, right = null, top = null, bottom = null, startingPoint;

        // Searching for extremum points
        for (Point point : genome) {
            // Calculating min and max longitude and latitude
            if (maxLong < point.getLongitude()) {
                maxLong = point.getLongitude();
                right = point;
            }
            if (minLong > point.getLongitude()) {
                minLong = point.getLongitude();
                left = point;
            }
            if (maxLat < point.getLatitude()) {
                maxLat = point.getLatitude();
                top = point;
            }
            if (minLat > point.getLatitude()) {
                minLat = point.getLatitude();
                bottom = point;
            }
        }

        // Default - left extremum point
        startingPoint = left;

        // Searching for the most wanted extremum point
        if (constraints.containsKey(top.getAmenity()) &&
                constraints.get(top.getAmenity()) > currRank) {
            startingPoint = top;
            currRank = constraints.get(top.getAmenity());
        }
        if (constraints.containsKey(bottom.getAmenity()) &&
                constraints.get(bottom.getAmenity()) > currRank) {
            startingPoint = bottom;
            currRank = constraints.get(bottom.getAmenity());
        }
        if (constraints.containsKey(left.getAmenity()) &&
                constraints.get(left.getAmenity()) > currRank) {
            startingPoint = left;
            currRank = constraints.get(left.getAmenity());
        }
        if (constraints.containsKey(right.getAmenity()) &&
                constraints.get(right.getAmenity()) > currRank) {
            startingPoint = right;
        }

        return startingPoint;
    }
}