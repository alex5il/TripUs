tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', function ($scope, $location, $routeParams, group) {
            var myGroup = group.getGroup($routeParams.groupId);
            $scope.requirements = ['a', 'b', 'c', 'd'];

        // Star rating crap
        console.log("asdasd ");
        $("#input-id").rating();
        }
    ]
);