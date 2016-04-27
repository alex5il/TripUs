var tripUs = angular.module('tripUs', ['ngRoute', 'ui.bootstrap','tripUsControllers', 'tripUsDirectives', 'tripUsServices', 'tripUsFilters', 'restangular', 'uiGmapgoogle-maps']);

var tripUsServices = angular.module('tripUsServices', []);
var tripUsDirectives = angular.module('tripUsDirectives', ['tripUsServices']);
var tripUsControllers = angular.module('tripUsControllers', ['tripUsServices']);
var tripUsFilters = angular.module('tripUsFilters', []);


tripUs.config(['$routeProvider', 'uiGmapGoogleMapApiProvider',
    function($routeProvider, uiGmapGoogleMapApiProvider) {
        $routeProvider
        .when('/', {
                templateUrl: '/assets/views/homePartial.html',
                controller: 'homeCtrl'
        })
        .when('/requirements/:groupId/:userName', {
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