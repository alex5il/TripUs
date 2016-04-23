/**
 * Created by Alex on 4/23/2016.
 */

// Filter for select box thingy
tripUsFilters.filter('duplicatesFilter', function() {
        return function(items, index, selected) {

            // Get the other selected values
            var res = [selected[index]];

            // Iterate over them and remove unwanted
            _.forEach(items, function(item){
                if(!_.find(selected, function(label) {
                        return label === item;
                    })){

                    // Push wanted --- hurray!!!
                    if(!!item) res.push(item);

                }
            });

             // return shit
            return res;
        };
    });