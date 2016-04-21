/**
 * Created by Akon on 05/12/2015.
 */

tripUsDirectives.directive('amenityRating', [function () {
    return {
        templateUrl: '/assets/directives/amenityRatingDirective.html',
        scope: {
            removeItem: '&',
            addItem: '&',
            item: "="
        },
        link: function ($scope, element, attrs) {
            // Create rating
            var ele = element.find("div input.rating");

            // Init value
            ele.attr('value', $scope.item.value);

            // Init rating
            ele.rating();

            // Watch the rating change
            ele.on('rating.change', function(event, value, caption) {
                // 'value' is a string here!
                $scope.item.value = Number(value);
            });
        }
    }
}]);