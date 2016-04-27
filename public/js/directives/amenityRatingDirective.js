/**
 * Created by Akon on 05/12/2015.
 */

tripUsDirectives.directive('amenityRating', [function () {
    return {
        templateUrl: '/assets/directives/amenityRatingDirective.html',
        scope: {
            removeItem: '&',
            addItem: '&',
            item: "=",
            amenities: "=",
            selectedAmenity: "=",

            changeAmenity: '&',
            amenityIndex: '=',
            otherSelectedValues: "="
        },
        link: function ($scope, element, attrs) {
            // Create rating
            var ele = element.find("div input.rating");

            // Init value
            ele.attr('value', $scope.item.value);

            ele.data('amenity', $scope.currAmenity);

            // Init rating
            ele.rating();

            // Watch the rating change
            ele.on('rating.change', function(event, value, caption) {
                // 'value' is a string here!
                $scope.item.value = Number(value);
            });

            //Setting first option as selected in configuration select
            $scope.selectedAmenity = $scope.amenities[0];
        }
    }
}]);