var tripUs = angular.module('tripUs', ['ngRoute', 'ui.bootstrap','tripUsControllers', 'tripUsDirectives', 'tripUsServices']);

var tripUsDirectives = angular.module('tripUsDirectives', []);
var tripUsControllers = angular.module('tripUsControllers', []);

tripUs.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: '/assets/views/homePartial.html',
                controller: 'homeCtrl'
            }).otherwise({
                redirectTo: '/'
            });
    }]
);