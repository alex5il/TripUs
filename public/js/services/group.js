/**
 * Created by Mike on 05/12/2015.
 */

tripUsServices.service('group', function(){
    this.groups = {};
    this._groupId = 1;
    return {
        groups: this.groups,
        _groupId: this._groupId,
        createGroup: function(groupName){
            var newGroup = { name: groupName, code: this._groupId};
            this.groups[this._groupId] = newGroup;
            this._groupId++;
            return newGroup;
        },
        getGroup: function(code) {
            return this.groups[code] || undefined;
        }
    };
});