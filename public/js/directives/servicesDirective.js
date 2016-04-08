/**
 * Created by Mike on 05/12/2015.
 */

tripUsDirectives.directive('services', ['$uibModal', 'group', '$location', function ($modal, group, $location) {
    return {
        templateUrl: '/assets/directives/servicesDirective.html',
        scope: {},
        link: function ($scope, element, attrs) {
            $scope.createGroup = function () {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/assets/directives/group-creator-modal.html',
                    scope: $scope,
                    resolve: { modalInstance: function () {
                        return modalInstance;
                    }},
                    controller: 'groupCreatorModalCtrl',
                    size: 'md'
                });
            };

            $scope.joinGroup = function () {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/assets/directives/group-join-modal.html',
                    scope: $scope,
                    resolve: {
                        modalInstance: function () {
                            return modalInstance;
                        }
                    },
                    controller: 'groupJoinModalCtrl',
                    size: 'md'
                });
            };
        }
    }
}]);