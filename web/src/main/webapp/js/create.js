(function($) {

    var REST_DATA = 'api/trips/';

    var id = "";

    var urlSearch = new URLSearchParams(window.location.search);
    if(urlSearch.has("id")) {
        id = urlSearch.get("id");
    }

    if (id !== "") {
        $("#submitTrip").val("Update");
    }

    function objectToQuery(map){
        var enc = encodeURIComponent, pairs = [];
        for(var name in map){
            var value = map[name];
            var assign = enc(name) + "=";
            if(value && value instanceof Array){
                for(var i = 0, len = value.length; i < len; ++i){
                    pairs.push(assign + enc(value[i]));
                }
            }else{
                pairs.push(assign + enc(value));
            }
        }
        return pairs.join("&");
    }

    function loadDataWhenIdAvailable() {
        if (id === "") {
            $("#page-title").text("Create");
            return ;
        } else {
            $("#page-title").text("Update");
        }

        xhrGet(REST_DATA + id,
            function (data) {

                $("#title").val(data.title);
                $("#description").val(data.description);
                $("#imageUrl").val(data.imageUrl);
                $("#price").val(data.price);
                $("#rating").val(data.rating);
                $("#currency").val(data.currency.toLowerCase());

                $("#preview-title").text($("#title").val());
                $("#preview-content").text($("#description").val());
                $("#preview-price").text($("#price").val());
                $("#preview-price").text($("#price").val());
                $("#preview-currency").text($("#currency option:selected").text());
                $("#preview-rating").text($("#rating").val() + "/10");

                if(checkURL($("#imageUrl").val()) === true) {
                    $("#preview-image").attr("src", $("#imageUrl").val());
                } else {
                    setDefaultImage();
                }

            },
            function() {});
    }

    $("#submitTrip").click(function() {

        if (id !== "") {

            // update
            var xhttp = new XMLHttpRequest();
            xhttp.open("PUT", REST_DATA + id, false);
            xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

            var oTrip = {
                title: $("#title").val(),
                description: $("#description").val(),
                imageUrl: $("#imageUrl").val(),
                rating: $("#rating").val(),
                price: $("#price").val(),
                currency: $("#currency").val()
            };

            xhttp.send(objectToQuery(oTrip));
            var response = JSON.parse(xhttp.responseText);
            if(xhttp.status === 200) {
                window.location.replace("display.html?id=" + response._id);
            } else {
                alert("Error occurred.");
            }

            return;
        }

        // create
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/api/trips", false);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        var oTrip = {
            title: $("#title").val(),
            description: $("#description").val(),
            imageUrl: $("#imageUrl").val(),
            rating: $("#rating").val(),
            price: $("#price").val(),
            currency: $("#currency").val()
        };

        xhttp.send(objectToQuery(oTrip));
        var response = JSON.parse(xhttp.responseText);
        if(xhttp.status === 200) {
            window.location.replace("display.html?id=" + response._id);
        } else {
            alert("Error occurred.");
        }
    });

    $("#title").on("paste, keyup", function() {
        $("#preview-title").text($("#title").val());
    });

    $("#description").on("paste, keyup", function() {
        $("#preview-content").text($("#description").val());
    });

    $("#price").on("paste, keyup", function() {
        $("#preview-price").text($("#price").val());
    });

    $("#currency").on("paste keyup", function() {
        $("#preview-currency").text($("#currency option:selected").text());
    });

    $("#rating").on("paste, keyup", function() {
        var iRating = $("#rating").val();
        if(iRating < 0) {
            iRating = 0;
        } else if (iRating > 10) {
            iRating = 10;
        }
        $("#rating").val(iRating);
        $("#preview-rating").text(iRating + "/10");
    });

    $("#preview-image").error(function() {
        console.error("Could not load image.");
        setDefaultImage();
    });

    $("#imageUrl").on("change", function() {
        if(checkURL($("#imageUrl").val()) === true) {
            $("#preview-image").attr("src", $("#imageUrl").val());
        } else {
            setDefaultImage();
        }
    });
    function checkURL(url) {
        return(url.match(/\.(jpeg|jpg|gif|png)$/) != null);
    }
    function setDefaultImage() {
        var sourceUrl = $("#preview-image").src;
        var defaultUrl = "images/no_image.png";
        if(sourceUrl !== defaultUrl) {
            $("#preview-image").attr("src", defaultUrl);
        }
    }

    loadDataWhenIdAvailable();

})(jQuery);