tripUsControllers.controller('requirementsCtrl',
    ['$scope', '$location','$routeParams' , 'group', 'amenityService', 'Restangular', 'alghorithm', function ($scope, $location, $routeParams, group, amenityService, Restangular, alghorithm) {

            $scope.requirements = {
                ratings: []
            };

            // Get data from server if user was already at this group
            group.reqGet($routeParams.groupId, $routeParams.userName).then(function(res){
                $scope.isLeader = res.isLeader;

                _(res.reqs).forEach(function (n) {
                    n.selectedAmenity = n.amenity;
                    n.value = n.rank;

                    $scope.requirements.ratings.push(n);
                });

            },function(res){ // Err
                console.log(res);
            });

            $scope.otherSelectedValues = [];

            amenityService.all().then(function(data) {
                 $scope.amenities = data;
            });

            $scope.removeItem = function(index) {
                $scope.requirements.ratings.splice(index, 1);
            };

            $scope.addItem = function() {
                $scope.requirements.ratings.push({
                    value: 1
                });
            };

            $scope.submit = function() {
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
                    return;
                } else {
                    $scope.errorMsg = false;
                }

                // Check for duplicates
                if(hasDuplicates(tempReq)) {
                    $scope.errorMsg = "There are duplicates in amenities, remove them fist";
                    $scope.okMsg = false;
                    return;
                } else {
                    $scope.errorMsg = false;
                }

                // Send request
                group.reqSet($routeParams.groupId, $routeParams.userName, tempReq).then(function(response){
                    $scope.errorMsg = false;
                    $scope.okMsg = "Awesome data sent!";
                }, function(res){
                    $scope.errorMsg = res.data;
                    $scope.okMsg = false;
                });
            };

            $scope.searchTrip = function() {
                // Call to start alghorithm
                alghorithm.start($routeParams.groupId).then(function(res){
                    $scope.errorMsg = false;
                    $scope.okMsg = "Algorithm started!";
                }, function(res){
                    $scope.errorMsg = res.data;
                    $scope.okMsg = false;
                });
            };

             // Check for duplicates
            function hasDuplicates(values) {
                var valueArr = values.map(function(item){ return item.selectedAmenity });
                var isDuplicate = valueArr.some(function(item, idx){
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