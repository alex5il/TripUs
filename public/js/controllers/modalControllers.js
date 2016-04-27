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
    $scope.join = function(groupCode, userName){
        // Validate stuff

        if ($scope.myForm.$valid) {
            group.join().then(function(result){
                console.log(result);
                $location.path("/requirements/" + $scope.groupCode + "/" + userName);


                modalInstance.close();

                // Dismiss all hack
                $uibModalStack.dismissAll();
            }, function(res){
                $scope.joinResponse = res.data;
            }) ;
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