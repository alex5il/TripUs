tripUsControllers.controller('tripResultsCtrl', function ($scope, TripResults, $routeParams, $timeout) {
    var isFirstRun = true;
    $scope.isFinished = false;
    $scope.polylines = [];
    $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };


    $scope.setPathMarkers = function(currentPath) {
        $scope.currentPath = currentPath;
        $scope.pathMarkers = [];
        angular.forEach(currentPath.path, function (point, index) {
                $scope.pathMarkers.push(
                    {
                        id: index,
                        coords: {longitude: point.longitude, latitude: point.latitude},
                        options:
                            {
                                labelContent: (point.name && point.name != 'null') ? point.name : '',
                                labelAnchor: '0 0',
                                labelClass: 'anchorLabel',
                                animation: index == 0? 1 : 0
                            }
                    }
                );
            });
    };

    function getTrips () {
        TripResults.get($routeParams.groupId).then(function (result) {
                $scope.tripResults = result.results;
                $scope.isFinished = result.isFinished;
                $scope.polylines = [];
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

                if (isFirstRun && $scope.polylines.length > 0) {
                    $scope.currentPath = $scope.polylines[0];
                    isFirstRun = false;
                }
                if (angular.isDefined($scope.currentPath)) {
                    $scope.setPathMarkers($scope.currentPath);
                }
            });
    }

    function pollTrips () {
        if (!$scope.isFinished) {
            $timeout(function () {
                getTrips();
                pollTrips();
            }, 5000);
        }
    }

    getTrips();
    pollTrips();
});
