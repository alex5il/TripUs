var tripUs = angular.module('tripUs', ['ngRoute', 'ui.bootstrap','tripUsControllers', 'tripUsDirectives', 'tripUsServices', 'restangular', 'uiGmapgoogle-maps']);

var tripUsServices = angular.module('tripUsServices', []);
var tripUsDirectives = angular.module('tripUsDirectives', ['tripUsServices']);
var tripUsControllers = angular.module('tripUsControllers', ['tripUsServices']);


tripUs.config(['$routeProvider', 'uiGmapGoogleMapApiProvider',
    function($routeProvider, uiGmapGoogleMapApiProvider) {
        $routeProvider
        .when('/', {
                templateUrl: '/assets/views/homePartial.html',
                controller: 'homeCtrl'
        })
        .when('/requirements/:groupId', {
                templateUrl: '/assets/views/requirements.html',
                controller: 'requirementsCtrl'
        })
        .when('/tripResults/:groupId', {
                templateUrl: '/assets/views/tripResults.html',
                controller: 'tripResultsCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });

        uiGmapGoogleMapApiProvider.configure({
                //    key: 'your api key',
                v: '3.20', //defaults to latest 3.X anyhow
                libraries: 'weather,geometry,visualization'
            });
    }]
);