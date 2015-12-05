var express = require('express');
var app = express();

app.use(express.static('public'));

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

app.get('/home', function (req, res) {
  res.sendFile(__dirname + '/public/homePartial.html');
});

app.get('/group-creator', function (req, res) {
  res.sendFile(__dirname + '/public/group-creator.html');
});

app.get('/group-creator-modal', function (req, res) {
  res.sendFile(__dirname + '/public/group-creator-modal.html');
});

var server = app.listen(8080, function () {
  var host = "localhost";
  var port = server.address().port;

  console.log("Server started");
});
