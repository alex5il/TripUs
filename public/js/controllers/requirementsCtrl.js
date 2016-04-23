tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', 'amenityService', 'Restangular', function ($scope, $location, $routeParams, group, amenityService, Restangular) {
            //var myGroup = group.getGroup($routeParams.groupId);

            $scope.otherSelectedValues = [];

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
            //$scope.changeAmenity = function(newVal, index) {
            //    // Fuck this shit for now , later optimize ---- nahhhhh
            //
            //    $scope.otherSelectedValues[index] = newVal;
            //
            //    // Remove undefined stuff
            //    $scope.otherSelectedValues = $scope.otherSelectedValues.filter(function(n){ return n !== undefined });
            //}
        }
    ]
);