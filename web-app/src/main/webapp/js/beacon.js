//Konfiguration
var destination = "/event";

function createEvent(ref, e) {
    var event = {};
    event["destination"] = ref.prop("href");
    event["coords"] = {"x": e.pageX, "y": e.pageY};
    event["timestamp"] = new Date(e.timeStamp).toISOString();
    event["referrer"] = document.referrer;
    event["source"] = $(location).prop("href");
    return event;
}

function sendBeacon(beacon) {
    console.log("Sending beacon event to server:")
    console.log(beacon);
    $.ajax(destination, {
        type: "POST",
        async: true,
        data: JSON.stringify(beacon),
        contentType: "application/json",
        dataType: "json",
        success: function (event) {
            console.log("Beacon sent successfully");
        }
    });
}

$(document).ready(function (e) {
    $('a').click(function (e) {
        sendBeacon(createEvent($(this), e));
    });
});
