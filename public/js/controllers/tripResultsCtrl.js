tripUsControllers.controller('tripResultsCtrl', function ($scope) {

    var result = {description: '', locations: []};
    $scope.currentRoute = 0;
    $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
          $scope.polylines = [
            {
                id: 1,
                path: [
                    {
                        latitude: 45,
                        longitude: -74
                    },
                    {
                        latitude: 30,
                        longitude: -89
                    },
                    {
                        latitude: 37,
                        longitude: -122
                    },
                    {
                        latitude: 60,
                        longitude: -95
                    }
                ],
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                editable: true,
                draggable: true,
                geodesic: true,
                visible: true
            },
            {
                id: 2,
                path: [
                    {
                        latitude: 47,
                        longitude: -74
                    },
                    {
                        latitude: 32,
                        longitude: -89
                    },
                    {
                        latitude: 39,
                        longitude: -122
                    },
                    {
                        latitude: 62,
                        longitude: -95
                    }
                ],
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                editable: true,
                draggable: true,
                geodesic: true,
                visible: true,
            }
        ];

        $scope.currentPath = $scope.polylines[1];
//    $scope.changeRoute = function (routeNum) {
//        $scope.polylines[$scope.currentRoute].visible = false;
//        $scope.polylines[routeNum].visible = true;
//        $scope.currentRoute = routeNum;
//    };
});
