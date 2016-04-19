tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', function ($scope, $location, $routeParams, group) {
            var myGroup = group.getGroup($routeParams.groupId);
            console.log(myGroup.name);
            $scope.requirements = ['a', 'b', 'c', 'd'];
        }
    ]
);