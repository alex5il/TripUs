tripUsControllers.controller('groupCreatorModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance', function ($scope, $location, group, modalInstance) {
    $scope.create = function(groupName){
        group.createGroup(groupName);
        modalInstance.close();
    };
    $scope.cancel = function () {
        modalInstance.dismiss('cancel');
    };
}]);

tripUsControllers.controller('groupJoinModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance', function ($scope, $location, group, modalInstance) {
    $scope.join = function(groupCode){
        $scope.myGroup = group.getGroup(groupCode);
        if (angular.isDefined($scope.myGroup)) {
            console.log("you have joined group - '" + $scope.myGroup.name + "'");
        } else {
            console.log("no group found");
        }
        modalInstance.close();
    };
    $scope.cancel = function () {
        modalInstance.dismiss('cancel');
    };
}]);