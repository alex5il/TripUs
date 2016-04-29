tripUsControllers.controller('tripResultsCtrl', function ($scope, TripResults, $routeParams) {

    $scope.polylines = [];
    $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
    $scope.setPathMarkers = function() {
        $scope.pathMarkers = [];
        angular.forEach($scope.currentPath.path, function (point, index) {
                    $scope.pathMarkers.push(
                        {
                            id: index,
                            coords: {longitude: point.longitude, latitude: point.latitude},
                            options: {labelContent: (point.name && point.name != 'null') ? point.name : '', labelAnchor: '0 0', labelClass: 'anchorLabel', animation: index == 0? 1 : 0}
                        }
                    );
                });
    };
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

        $scope.currentPath = $scope.polylines[0];
        $scope.setPathMarkers();
    });
});
