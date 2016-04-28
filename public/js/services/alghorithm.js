
tripUsServices.service('alghorithm', ['Restangular', function(Restangular){

    var baseAlg = Restangular.all('Alg');

    return {
        start: function(groupKey){
            return baseAlg.customPOST({groupKey: groupKey}, "start", {}, {});
        }
    };
}]);