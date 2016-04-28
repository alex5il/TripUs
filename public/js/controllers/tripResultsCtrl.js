tripUsControllers.controller('tripResultsCtrl', function ($scope, TripResults, $routeParams) {

    $scope.polylines = [];
    $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
    TripResults.get($routeParams.groupId).then(function (result) {
        $scope.tripResults = result;
        angular.forEach($scope.tripResults, function (tripResult, index) {
            $scope.polylines.push({
                id : index,
                path: tripResult.points,
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                geodesic: true,
                fit: true
            });
        });

        $scope.currentPath = $scope.polylines[1];
    });
});
