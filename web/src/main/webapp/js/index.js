(function($) {
    var REST_DATA = 'api/trips';

    function loadTrips() {
        xhrGet(REST_DATA,
            function (data) {
                for (var i = 0; i < data.length; i++){
                    if ((data[i] && '_id' in data[i]) &&
                        (data[i] && 'description' in data[i]) &&
                        (data[i] && 'title' in data[i])) {

                        var urlToTrip = "display.html?id=" + data[i]._id;
                        $('section .main-content').append('<div class="trip"><h3><a href="' + urlToTrip + '">' + data[i].title + '</a></h3><img src="'
                            + data[i].imageUrl+'"><p>'+ descriptionShortening(data[i].description) + '</p></div>')
                    }

                }
            },

            function (err) {
            })
    }

    function descriptionShortening(description) {
        return description.length > 400 ? description.substr(0, 400) : description;
    }

    loadTrips();

})(jQuery);