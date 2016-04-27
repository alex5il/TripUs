/**
 * Created by Mike on 05/12/2015.
 */

tripUsServices.service('group', ['Restangular', function(Restangular){

    var baseTrip = Restangular.all('Trip');

    return {
        create: function(groupName){
            return baseTrip.customPOST({name: groupName}, "create", {}, {});
        },
        join: function(groupKey, userName){
            return baseTrip.customPOST({groupKey: groupKey, userName: userName}, "join", {}, {});
        },
        c: function(groupKey, userName){
            return baseTrip.customPOST({groupKey: groupKey, userName: userName, reqs:[{amenity:"", rank:""}, {amenity:"", rank:""}]}, "user/reqSet", {}, {});
        }
    };
}]);