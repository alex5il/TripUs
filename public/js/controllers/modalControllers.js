tripUsControllers.controller('groupCreatorModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance','$uibModal', '$uibModalStack', function ($scope, $location, group, modalInstance, $modal, $uibModalStack) {
    $scope.create = function(groupName){
        // Create group
        group.create(groupName).then(function(groupCode){
            $scope.groupCode = groupCode;
        });
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

        //modalInstance.close();

        //setTimeout(console.log($("span .fa.fa-sign-in").parent().click()), 1000);
        //$location.path("/requirements/" + $scope.groupKey);
    };

    $scope.cancel = function () {
        modalInstance.dismiss('cancel');
    };
}]);

tripUsControllers.controller('groupJoinModalCtrl', ['$scope', '$location', 'group', '$uibModalInstance', '$uibModalStack', function ($scope, $location, group, modalInstance, $uibModalStack) {
    $scope.join = function(groupCode){
        // Add user
        $scope.myGroup = group.getGroup(groupCode);

        if (angular.isDefined($scope.myGroup)) {
            console.log("you have joined group - '" + $scope.myGroup.name + "'");
            $location.path("/requirements/" + $scope.groupCode);
        } else {
            console.log("no group found");
        }
        modalInstance.close();

        $uibModalStack.dismissAll();
    };
    $scope.cancel = function () {
        modalInstance.dismiss('cancel');


        $uibModalStack.dismissAll();
    };
}]);