/**
 * Created by Mike on 05/12/2015.
 */
var tripUsDirectives = angular.module('tripUsDirectives', []);
tripUsControllers.directive('groupCreator', ['$uibModal', 'group', function ($modal, group) {
    return {
        templateUrl: 'group-creator',
        scope: {},
        link: function ($scope, element, attrs) {
            console.log('directive loaded');
        }
    }
}]);