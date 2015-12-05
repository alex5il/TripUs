var tripUs = angular.module('tripUs', ['ngRoute', 'tripUsControllers']);


tripUs.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                templateUrl: 'partials/home.html',
                controller: 'homeCtrl'
            }).otherwise({
                redirectTo: '/'
            });
    }]);