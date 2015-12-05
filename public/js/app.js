var tripUs = angular.module('tripUs', ['ngRoute', 'ui.bootstrap','tripUsControllers', 'tripUsDirectives', 'tripUsServices']);


tripUs.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'home',
                controller: 'homeCtrl'
            }).otherwise({
                redirectTo: '/'
            });
    }]
);