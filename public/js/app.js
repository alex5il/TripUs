var tripUs = angular.module('tripUs', ['ngRoute', 'tripUsControllers']);


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