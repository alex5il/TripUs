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
                    animation: $scope.animationsEnabled,
                    templateUrl: 'group-creator-modal.html',
                    scope: $scope,
                    controller: function ($scope){
                        $scope.create = function(groupName){
                            group.createGroup(groupName);
                            console.log('group created');
                        };
                    },
                    size: 'md'
                });
            };
            console.log('directive loaded');
        }
    }
}]);