var stompClient = null;

connect();

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/statistics', function (statistics) {
            statsJson = JSON.parse(statistics.body);
            if (statsJson.message !== undefined) {
                showStatistics(statsJson.message);
            } else {
                showStatistics(JSON.stringify(JSON.parse(statistics.body), null, '\t'));
            }
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendNet() {
    stompClient.send("/petri/compute/distributed", {}, $("#net").val());
}

function showStatistics(statistics) {
    $("#stats").val(statistics);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#submit-net" ).click(function() { sendNet(); });
});