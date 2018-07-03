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