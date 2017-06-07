var dateParam = document.getElementById("stamp:date");
var timeParam = document.getElementById("stamp:time");
var clock = document.getElementById("clock");

(function () {
    update();
    window.setInterval(update, 1000);
})();

function update() {
    var d = new Date();
    var date = getLocalDate(d);
    var time = getLocalTime(d);
    dateParam.value = date;
    timeParam.value = time;
    clock.innerHTML = time;
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
