var dateParam = document.getElementById("form:date");
var timeParam = document.getElementById("form:time");

var clocks = document.getElementsByClassName("clock");
var dates = document.getElementsByClassName("date");

(function () {
    update();
    window.setInterval(update, 1000);
})();

function update() {
    var now = new Date();
    var date = getLocalDate(now);
    var time = getLocalTime(now);

    if (dateParam != null) {
        dateParam.value = date;
    }
    if (timeParam != null) {
        timeParam.value = time;
    }

    for (var c in clocks) {
        clocks[c].innerHTML = time;
    }

    for (var d in dates) {
        dates[d].innerHTML = date;
    }
}

function getLocalDate(date) {
    var d = date.getDate();
    if (d < 10) {
        d = '0' + d;
    }
    var m = 1 + date.getMonth();
    if (m < 10) {
        m = '0' + m;
    }
    return d + "." + m + "." + date.getFullYear();
}

function getLocalTime(date) {
    var h = date.getHours();
    if (h < 10) {
        h = '0' + h;
    }
    var m = date.getMinutes();
    if (m < 10) {
        m = '0' + m;
    }
    var s = date.getSeconds();
    if (s < 10) {
        s = '0' + s;
    }
    return h + ":" + m + ":" + s;
}
