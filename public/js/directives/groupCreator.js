/**
 * Created by Mike on 05/12/2015.
 */
var tripUsDirectives = angular.module('tripUsDirectives', []);
tripUsControllers.directive('groupCreator', ['$uibModal', 'group', function ($modal, group) {
    return {
        templateUrl: 'group-creator',
        scope: {},
        link: function ($scope, element, attrs) {
            $scope.createGroup = function () {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'group-creator-modal.html',
                    scope: $scope,
                    resolve: { modalInstance: function () {
                        return modalInstance;
                    }},
                    controller: function ($scope){
                        $scope.create = function(groupName){
                            group.createGroup(groupName);
                            modalInstance.close();                        };
                        $scope.cancel = function () {
                            modalInstance.dismiss('cancel');
                        };
                    },
                    size: 'md'
                });
            };
            console.log('directive loaded');
        }
    }
}]);