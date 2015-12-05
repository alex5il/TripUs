/**
 * Created by Mike on 05/12/2015.
 */
var tripUsControllers = angular.module('tripUsControllers', []);
tripUsControllers.controller('homeCtrl', ['$scope', '$http',
    function ($scope) {
        $scope.what = 'nothing';
    }]
);