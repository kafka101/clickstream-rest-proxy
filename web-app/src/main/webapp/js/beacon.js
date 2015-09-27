//Konfiguration
var destination = "/rest/actions/click";

function createEvent(ref, e) {
    var event = {};
    event["destination"] = ref.prop("href");
    event["coords"] = {"x": e.pageX, "y": e.pageY};
    event["timestamp"] = new Date(e.timeStamp).toISOString();
    event["referrer"] = document.referrer;
    event["source"] = $(location).prop("href");
    return event;
}

function sendClickEvent(click) {
    console.log("Sending click event to server:")
    console.log(click);
    $.ajax(destination, {
        type: "POST",
        async: true,
        data: JSON.stringify(click),
        contentType: "application/json",
        dataType: "json",
        success: function (event) {
            console.log("Click event sent successfully");
        }
    });
}

$(document).ready(function (e) {
    $('a').click(function (e) {
        sendClickEvent(createEvent($(this), e));
    });
});
