var tripUs = angular.module('tripUs', ['ngRoute', 'ui.bootstrap','tripUsControllers', 'tripUsDirectives', 'tripUsServices', 'restangular']);

var tripUsServices = angular.module('tripUsServices', []);
var tripUsDirectives = angular.module('tripUsDirectives', ['tripUsServices']);
var tripUsControllers = angular.module('tripUsControllers', ['tripUsServices']);


tripUs.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
        .when('/', {
                templateUrl: '/assets/views/homePartial.html',
                controller: 'homeCtrl'
        })
        .when('/requirements/:groupId', {
                templateUrl: '/assets/views/requirements.html',
                controller: 'requirementsCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
    }]
);