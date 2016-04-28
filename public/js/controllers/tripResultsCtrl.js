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

        $scope.markers = [
            {
                id: 0,
                coords: {
                    latitude: 40.1451,
                    longitude: -99.6680
                },
                options: {
                    labelContent: "Fuck my life",
                    labelAnchor: "0 0",
                    labelClass: "marker-labels"
                }
            },
            {
                id: 1,
                coords: {
                    latitude: 45.1451,
                    longitude: -95.6680
                },
                options: {
                    labelContent: "Fuck my life",
                    labelAnchor: "0 0",
                    labelClass: "marker-labels"
                }
            }
        ];
    });
});
