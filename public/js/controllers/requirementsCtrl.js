tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', 'amenityService', 'Restangular', function ($scope, $location, $routeParams, group, amenityService, Restangular) {
            //var myGroup = group.getGroup($routeParams.groupId);

            // amenityService.all().then(function(amenities){
            //     console.log(amenities);
            // });

            $scope.amenities = amenityService.mock();

            $scope.requirements = {
                ratings: [{
                    value: 3
                }, {
                    value: 2
                }]
            };

            $scope.removeItem = function(index) {
                $scope.requirements.ratings.splice(index, 1);
            };

            $scope.addItem = function() {
                $scope.requirements.ratings.push({
                    value: 1
                });
            }
        }
    ]
);