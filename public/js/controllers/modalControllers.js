tripUsControllers.controller('groupCreatorModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance','$uibModal',
    '$uibModalStack', '$timeout',
    function ($scope, $location, group, modalInstance, $modal, $uibModalStack, $timeout) {
    $scope.create = function(groupName, userName){
        if (!$scope.createForm.$valid) {
            if (!groupName) {
                $scope.createForm.groupName.$setValidity("required", false);
                $scope.createForm.groupName.$setDirty();
            } else if (!userName) {
                $scope.createForm.userName.$setValidity("required", false);
                $scope.createForm.userName.$setDirty();
            }
        } else {
            // Create group
            group.create(groupName, userName).then(function(groupCode){
                $scope.groupCode = groupCode;
                $location.path("/requirements/" + $scope.groupCode + "/" + userName);
            });
        }
    };

    $scope.join = function() {
        modalInstance = $modal.open({
            animation: true,
            templateUrl: '/assets/directives/group-join-modal.html',
            scope: $scope,
            resolve: {
                result: function(){
                    console.log("sadad");
                }
            },
            controller: 'groupJoinModalCtrl',
            size: 'md'
        });
    };

    $scope.cancel = function () {
        modalInstance.dismiss('cancel');
    };
}]);

tripUsControllers.controller('groupJoinModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance', '$uibModalStack', function ($scope, $location, group, modalInstance, $uibModalStack) {
    $scope.join = function(groupCode, userName){
        // Validate stuff

        if ($scope.myForm.$valid) {
            group.join(groupCode, userName).then(function(result){
                $location.path("/requirements/" + $scope.groupCode + "/" + userName);
                modalInstance.close();

                // Dismiss all hack
                $uibModalStack.dismissAll();
            }, function(res){
                $scope.joinResponse = res.data;
            });
        } else {
            // No power i know there are better ways...
            if (!groupCode) {
                $scope.myForm.groupCode.$setValidity("required", false);
                $scope.myForm.groupCode.$setDirty();
            } else if (!userName) {
                $scope.myForm.userName.$setValidity("required", false);
                $scope.myForm.userName.$setDirty();
            }
        }

    };
    $scope.cancel = function () {
        modalInstance.dismiss('cancel');

        // Dismiss all hack
        $uibModalStack.dismissAll();
    };
}]);