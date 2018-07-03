/**
 * Created by jjmendoza on 27/6/2018.
 */

var canvas;
var context;
var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var paint;

function prepareCanvas() {
    canvas = document.getElementById('canvas');
    context = canvas.getContext("2d");

    $('#canvas').mousedown(function (e) {
        var canvasPos = $(this).offset();       // alternative to work with bootstrap positioning

        var mouseX = e.pageX - canvasPos.left;
        var mouseY = e.pageY - canvasPos.top;

        paint = true;
        addClick(mouseX, mouseY);
        redraw();
    });

    $('#canvas').mousemove(function (e) {
        var canvasPos = $(this).offset();

        var mouseX = e.pageX - canvasPos.left;
        var mouseY = e.pageY - canvasPos.top;

        if (paint) {
            addClick(mouseX, mouseY, true);
            redraw();
        }
    });

    $('#canvas').mouseup(function (e) {
        paint = false;
    });

    $('#canvas').mouseleave(function (e) {
        paint = false;
    });

    // Add touch event listeners to canvas element
    canvas.addEventListener("touchstart", function (e) {
        // Mouse down location
        var canvasPos = $(this).offset();

        var mouseX = (e.changedTouches ? e.changedTouches[0].pageX : e.pageX) - canvasPos.left,
            mouseY = (e.changedTouches ? e.changedTouches[0].pageY : e.pageY) - canvasPos.top;

        paint = true;
        addClick(mouseX, mouseY, false);
        redraw();
    }, false);
    canvas.addEventListener("touchmove", function (e) {
        var canvasPos = $(this).offset();

        var mouseX = (e.changedTouches ? e.changedTouches[0].pageX : e.pageX) - canvasPos.left,
            mouseY = (e.changedTouches ? e.changedTouches[0].pageY : e.pageY) - canvasPos.top;

        if (paint) {
            addClick(mouseX, mouseY, true);
            redraw();
        }
        e.preventDefault()
    }, false);
    canvas.addEventListener("touchend", function (e) {
        paint = false;
        redraw();
    }, false);
    canvas.addEventListener("touchcancel", function (e) {
        paint = false;
    }, false);
}

function addClick(x, y, dragging) {
    clickX.push(x);
    clickY.push(y);
    clickDrag.push(dragging);
}

function redraw() {
    context.clearRect(0, 0, context.canvas.width, context.canvas.height); // Clears the canvas

    context.strokeStyle = "#000";
    context.lineJoin = "round";
    context.lineWidth = 5;

    for (var i = 0; i < clickX.length; i++) {
        context.beginPath();
        if (clickDrag[i] && i) {
            context.moveTo(clickX[i - 1], clickY[i - 1]);
        } else {
            context.moveTo(clickX[i] - 1, clickY[i]);
        }
        context.lineTo(clickX[i], clickY[i]);
        context.closePath();
        context.stroke();
    }
}

function clearCanvas() {
    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    clickX = new Array();
    clickY = new Array();
    clickDrag = new Array();
}

function prepareSignImageForUpload() {
    document.getElementById("firma").value = canvas.toDataURL();
}
