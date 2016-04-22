tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', 'amenityService', 'Restangular', function ($scope, $location, $routeParams, group, amenityService, Restangular) {
            //var myGroup = group.getGroup($routeParams.groupId);

            amenityService.all().then(function(data) {
                 $scope.amenities = data;
            });

            //Init reqs
            $scope.requirements = {
                ratings: []
            };

            $scope.removeItem = function(index) {
                $scope.requirements.ratings.splice(index, 1);
            };

            $scope.addItem = function() {
                $scope.requirements.ratings.push({
                    value: 1
                });
            };

            // On change amenity
            $scope.changeAmenity = function(value) {
                console.log("crap");
                //var index = $scope.amenities.indexOf(value);
                //
                //if (index > -1) {
                //    $scope.amenities.splice(index, 1);
                //}
            }
        }
    ]
);