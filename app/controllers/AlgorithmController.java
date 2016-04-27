package controllers;

import data.Point;
import org.jongo.MongoCursor;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

public class AlgorithmController extends Controller {

    private class Constraint {
        private String amenity;
        private int rating;

        public Constraint(String amenity, int rating) {
            this.amenity = amenity;
            this.rating = rating;
        }

        public String getAmenity() {
            return this.amenity;
        }

        public int getRating() {
            return this.rating;
        }
    }

    private class Individual {
        private double fitness;
        public HashSet<Point> genome;

        public Individual() {
            genome = new HashSet<Point>();
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
    private final double P_MUT = 0.2;
    private final int MIN_POINTS = 10;
    private final int ITERATIONS = 100;
    private final int POINTS_MULTIPLIER = 3;

    private double maxFitness;
    private double minFitness;
    private int totalPoints;
    private int pointsInTrip;

    private ArrayList<Individual> population;
    private HashMap<String, Integer> constraints;
    private ArrayList<Point> genesArray;

    public Result index() {
        // Test data
        // ****************************************************************************** //
//        String tripKey = "270";
//        Trip trip = Trip.findByKey(tripKey);
        constraints = new HashMap<String, Integer>();

        constraints.put("pub", 6);
        constraints.put("casino", 2);
        constraints.put("boat_sharing", 3);
        constraints.put("theatre", 1);
        // ****************************************************************************** //

        MongoCursor<Point> pointsCursor;
        genesArray = new ArrayList<Point>();

        // Calculating total points in the trip
        for (Map.Entry<String, Integer> entry : constraints.entrySet()) {
            pointsInTrip += entry.getValue();
        }

        // If less than minimum points - set to minimum
        if (pointsInTrip < MIN_POINTS) {
            pointsInTrip = MIN_POINTS;
        }

        totalPoints = POPULATION_SIZE * pointsInTrip * POINTS_MULTIPLIER;

        // Fetching points population
        pointsCursor = Point.getRandomPoints(totalPoints);
        Iterator<Point> itGenes = pointsCursor.iterator();

        // Convert cursor to array list
        while (itGenes.hasNext()) {
            genesArray.add(itGenes.next());
        }

        Random rnd = new Random();
        maxFitness = 0;
        minFitness = Double.MAX_VALUE;

        population = new ArrayList<Individual>();

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
            population.add(individual);
        }

        // Evolving the population
        for (int i = 0; i < ITERATIONS; i++) {
            evolution();
        }

        Individual best = bestIndividual();

        return ok();
    }

    private void evolution() {
        double fitnessVariety = maxFitness - minFitness;
        double selectionProbability;
        Random rnd = new Random();
        ArrayList<Individual> toCrossover = new ArrayList<Individual>();

        // Performing selection
        for (Iterator<Individual> it = population.iterator(); it.hasNext(); ) {
            Individual individual = it.next();

            // Selection probability of the individual
            selectionProbability = (individual.getFitness() - minFitness) / (fitnessVariety);

            // The individual is not selected (removed)
            if (selectionProbability < rnd.nextDouble()) {
                it.remove();
            } else if (rnd.nextDouble() <= P_CROSS) {
                // The individual is chosen for crossover
                toCrossover.add(individual);
            } else if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(individual);
            }
        }

        // Performing crossover
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

            // Mutating the old individuals
            if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(ind1);
            }
            if (rnd.nextDouble() <= P_MUT) {
                // Performing mutation
                mutation(ind2);
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
    }

    private void calcFitness(Individual individual) {
        double minLong = Double.MAX_VALUE, minLat = Double.MAX_VALUE;
        double maxLong = -Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double fitness = 0;

        // Need the hash map to check that enough POIs of the same
        // amenity were already added to the trip.
        HashMap<String, Integer> leftovers = new HashMap<>(constraints);

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

            // If amenity is found in the constraints
            if (constraints.containsKey(point.getAmenity()) &&
                    leftovers.get(point.getAmenity()) > 0) {
                // Increase the fitness
                fitness += constraints.get(point.getAmenity());

                // Decrement leftover
                leftovers.put(point.getAmenity(), leftovers.get(point.getAmenity()) - 1);
            }
        }

        // Divide according to difference in distance
        fitness /= ((maxLong - minLong) + (maxLat - minLat));

        // Calculating max and min population fitness for further calculations
        if (maxFitness < fitness) {
            maxFitness = fitness;
        }
        if (minFitness > fitness) {
            minFitness = fitness;
        }

        individual.setFitness(fitness);
    }

    private Individual bestIndividual() {
        double bestFitness = 0;
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
}