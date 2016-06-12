tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location', '$routeParams', 'group', 'amenityService', 'Restangular', 'alghorithm', 'TripResults', function ($scope, $location, $routeParams, group, amenityService, Restangular, alghorithm, TripResults) {

        // handles connection to submit event
        var handleConnSubmitted = function (msg) {
            $scope.$apply(function () {
                //$scope.msg = JSON.parse(msg.data)
                // Get submitted users via HTTP request
                TripResults.getSubmittedUsers($routeParams.groupId).then(function (res) {
                    $scope.submittedUsers = res.names;
                }, function (res) { // ERR
                    console.error("Sad face");
                });
            });
        };

        // handles the callback from submit event
        var handleStartSubmitted = function (msg) {
            $scope.$apply(function () {

                $scope.submittedUsers.push(msg.data);
                console.log("User " + msg.data + " submitted reqs.");
            });
        };

        var sourceSubmitted = new EventSource('/Trip/reqSubmitEvent?tripKey=' + $routeParams.groupId);
        sourceSubmitted.addEventListener('sub', handleConnSubmitted, false);
        sourceSubmitted.addEventListener($routeParams.groupId, handleStartSubmitted, false);

        // handles the callback from algorithm start event
        var handleStart = function (msg) {
            $scope.$apply(function () {
                //Routes to result page
                $location.path("/tripResults/" + $routeParams.groupId);
            });
        };

        // handles the callback from conn event
        var handleConn = function (msg) {
            $scope.$apply(function () {
                //$scope.msg = JSON.parse(msg.data)
                console.log(msg.data);
            });
        };

        var source = new EventSource('/Alg/regEvent?tripKey=' + $routeParams.groupId);
        source.addEventListener('alg', handleConn, false);
        source.addEventListener($routeParams.groupId, handleStart, false);

        // No power crap
        $("#poi").bind('keyup mouseup', function () {
            if (this.value > 20) {
                $scope.pointsNum = 20;
                this.value = 20;
            } else if (this.value < 5) {
                $scope.pointsNum = 5;
                this.value = 5;
            }
        });

        $scope.requirements = {
            ratings: []
        };

        $scope.groupKey = $routeParams.groupId;

        $scope.pointsNum = $scope.pointsNum || 8;

        // Get data from server if user was already at this group
        group.reqGet($routeParams.groupId, $routeParams.userName).then(function (res) {

            $scope.isSubmited = res.addedReqs;

            $scope.isLeader = res.isLeader;

            $scope.pointsNum = res.pointsNum || 8;

            _(res.reqs).forEach(function (n) {
                n.selectedAmenity = n.amenity;
                n.value = n.rank;

                $scope.requirements.ratings.push(n);
            });

        }, function (res) { // Err
            console.log(res);
        });

        $scope.otherSelectedValues = [];

        amenityService.all().then(function (data) {
            $scope.amenities = data;
        });

        $scope.removeItem = function (index) {
            $scope.requirements.ratings.splice(index, 1);
        };

        $scope.addItem = function () {
            $scope.requirements.ratings.push({
                value: 1
            });
        };

        $scope.searchTrip = function () {
            // If leader - run the algorithm and redirect to results page
            if ($scope.isLeader) {
                // Call to start alghorithm
                alghorithm.start($routeParams.groupId, $scope.pointsNum).then(function (res) {
                    $scope.errorMsg = false;
                    $scope.okMsg = "Algorithm started!";
                }, function (res) {
                    $scope.errorMsg = res.data;
                    $scope.okMsg = false;

                    $scope.isSubmited = false;
                });

                // Clearing users submitting event.
                TripResults.clearEvent($routeParams.groupId);

                // Sending event to other clients
                alghorithm.sendEvent($routeParams.groupId).then(function (res) {
                    $scope.errorMsg = false;
                    $scope.okMsg = "Event sent!";

                    $scope.isSubmited = false;
                }, function (res) {
                    $scope.errorMsg = res.data;
                    $scope.okMsg = false;

                    $scope.isSubmited = false;
                });
            }
        };

        $scope.submit = function () {

            $scope.isSubmited = true;

            // Check validations
            var tempReq = $scope.requirements.ratings.slice(0);

            _(tempReq).forEach(function (n) {
                n.amenity = n.selectedAmenity;
                n.rank = n.value;
                delete n.$$hashKey
            });

            // Check user and group - from url
            if (!$routeParams.groupId && !$routeParams.userName) {
                $scope.errorMsg = "Url doesn't contain user or group key";
                $scope.okMsg = false;

                $scope.isSubmited = false;
                return;
            } else {
                $scope.errorMsg = false;
            }

            // Check for duplicates
            if (hasDuplicates(tempReq)) {
                $scope.errorMsg = "There are duplicates in amenities, remove them fist";
                $scope.okMsg = false;

                $scope.isSubmited = false;
                return;
            } else {
                $scope.errorMsg = false;
            }

            // Send request
            group.reqSet($routeParams.groupId, $routeParams.userName, tempReq, $scope.pointsNum).then(function (response) {
                $scope.errorMsg = false;
                $scope.okMsg = "Awesome data sent!";

                $scope.submitDone = true;

            }, function (res) {
                $scope.errorMsg = res.data;
                $scope.okMsg = false;

                $scope.isSubmited = false;
                $scope.submitDone = false;
            });
        };

        // Check for duplicates
        function hasDuplicates(values) {
            var valueArr = values.map(function (item) {
                return item.selectedAmenity
            });
            var isDuplicate = valueArr.some(function (item, idx) {
                return valueArr.indexOf(item) != idx
            });

            return isDuplicate;
        }

        // On change amenity
        //$scope.changeAmenity = function(newVal, index) {
        //    // Fuck this shit for now , later optimize ---- nahhhhh
        //
        //    $scope.otherSelectedValues[index] = newVal;
        //
        //    // Remove undefined stuff
        //    $scope.otherSelectedValues = $scope.otherSelectedValues.filter(function(n){ return n !== undefined });
        //}
    }
    ]
);