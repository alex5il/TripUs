/**
 * Created by Mike on 05/12/2015.
 */

tripUsControllers.directive('groupCreatorDirective', ['$uibModal', 'group', function ($modal, group) {
    return {
        templateUrl: '/assets/directives/group-creator',
        scope: {},
        link: function ($scope, element, attrs) {
            group.create();
            console.log('directive loaded');
        }
    }
}]);