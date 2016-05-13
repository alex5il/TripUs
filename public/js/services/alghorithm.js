
tripUsServices.service('alghorithm', ['Restangular', function(Restangular){

    var baseAlg = Restangular.all('Alg');

    return {
        start: function(groupKey, pointsNum){
            return baseAlg.customPOST({groupKey: groupKey, pointsNum:pointsNum}, "start", {}, {});
        }
    };
}]);