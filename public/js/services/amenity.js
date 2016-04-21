tripUsServices.service('amenityService', ['Restangular', function(Restangular){
    // The base URL
    // TODO - normalize routes later...
    var baseAmenities = Restangular.all('amenities');

    return {
        // This will query /amenities and return a promise.
        all: function(){
            Restangular.all('AllAmenities').getList();

            // baseAmenities.getList().then(function(amenities) {
            //     console.log(amenities);
            //     $scope.amenities = amenities;
            // });
        },
        // This will query /amenities and return a promise.
        withFilter: function(){
            Restangular.all('AllAmenitiesFilter').getList();

            // baseAmenities.getList().then(function(amenities) {
            //     console.log(amenities);
            //     $scope.amenities = amenities;
            // });
        },
        mock: function(){
            return ["pub","strip","supermarket"]

            // baseAmenities.getList().then(function(amenities) {
            //     console.log(amenities);
            //     $scope.amenities = amenities;
            // });
        },
        get: function(amenityId) {
            Restangular.one('amenities', amenityId).then(function(amenity){
                console.log(amenity);
            });
        }
    };
}]);