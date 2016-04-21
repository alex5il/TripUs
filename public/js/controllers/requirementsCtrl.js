tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', function ($scope, $location, $routeParams, group) {
            //var myGroup = group.getGroup($routeParams.groupId);

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