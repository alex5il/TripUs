tripUsServices.service('TripResults', ['Restangular', function(Restangular){

    var baseTrip = Restangular.all('Trip');

    return {
        get: function(groupKey){
            return baseTrip.customGET("result", {"tripGroupKey": groupKey});
        },
        getSubmittedUsers: function(groupKey){
            return baseTrip.customGET("submitted", {"tripGroupKey": groupKey});
        }
    };
}]);