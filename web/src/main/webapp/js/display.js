(function($) {

    var REST_DATA = 'api/trips/';

    function loadTrip() {
        var urlSearch = new URLSearchParams(window.location.search);
        if(!urlSearch.has("id")) {
            alert("Empty id parameter.");
            return;
        }
        var id = urlSearch.get("id");
        xhrGet(REST_DATA + id,
            function (data) {
                var urlToTrip = "display.html?id=" + data._id;
                $("#title").text(data.title);
                $("#description").text(data.description);
                $("#image").attr("src", data.imageUrl);
                $("#price").text(data.price + " " + data.currency);
                $("#rating").text(data.rating + "/10");
                $("#update").attr('href', 'create.html?id=' + id);
            },
            function (err) {
            })
    }

    loadTrip();

    $("#delete").click(function(event) {
        event.preventDefault();

        if (confirm('Do you really want to delete this trip?')) {
            var urlSearch = new URLSearchParams(window.location.search);
            if(!urlSearch.has("id")) {
                alert("Empty id parameter.");
                return;
            }
            var id = urlSearch.get("id");

            xhrDelete(REST_DATA + id, function() {
                window.location.replace("index.html");
            });
        }
    })

})(jQuery);