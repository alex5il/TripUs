/**
 * Created by Mike on 18/12/2015.
 */
tripUsControllers.controller('servicesCtrl', ['$scope', '$uibModal', 'group',
    function ($scope, $modal, group) {
        $scope.createGroup = function () {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/assets/directives/group-creator-modal.html',
                scope: $scope,
                resolve: { modalInstance: function () {
                    return modalInstance;
                }},
                controller: function ($scope){
                    $scope.create = function(groupName){
                        group.createGroup(groupName);
                        modalInstance.close();
                    };
                    $scope.cancel = function () {
                        modalInstance.dismiss('cancel');
                    };
                },
                size: 'md'
            });
        };

        $scope.joinGroup = function () {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/assets/directives/group-join-modal.html',
                scope: $scope,
                resolve: { modalInstance: function () {
                    return modalInstance;
                }},
                controller: function ($scope){
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
                },
                size: 'md'
            });
        };
    }
]);
