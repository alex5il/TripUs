var express = require('express');
var app = express();

app.use(express.static('public'));

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

var server = app.listen(8080, function () {
  var host = "localhost";
  var port = server.address().port;

  console.log("Server started");
});
