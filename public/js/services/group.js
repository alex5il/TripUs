/**
 * Created by Mike on 05/12/2015.
 */
var tripUsServices = angular.module('tripUsServices', []);

tripUsServices.service('group', function(){
    this.groups = [];
    return {
        groups: this.groups,
        createGroup: function(groupName){
            this.groups.push(groupName);
        },
        getGroup: function(name) {
            return this.group[name];
        }
    };
});