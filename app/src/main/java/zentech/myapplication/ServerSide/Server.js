var express = require('express')
  , app = express()
  , http = require('http')
  , server = http.createServer(app)
  , bodyParser = require('body-parser');

server.listen(8080);
var bl = require('./BL');

app.use(bodyParser.urlencoded({ extended: false }))

app.use(bodyParser.json())

app.post('/loginUser',function(req,res)
{
	var user = req.body.LaunchMyBoat;
	console.log("the user param is : " + user);
	var userjson = JSON.parse(user);
	console.log("the user json is : " + userjson);
	bl.loginUser(userjson ,res);
});

app.post('/registerUserByEmail',function(req,res)
{
	var user = req.body.LaunchMyBoat;
	console.log("the user param is : " + user);
	var userjson = JSON.parse(user);
	console.log("the user json is : " + userjson);
	bl.createNewUser(userjson ,res);
});


app.post('/loginUserWithFacebook',function(req,res)
{
	var user = req.body.LaunchMyBoat;
	console.log("the user param is : " + user);
	var userjson = JSON.parse(user);
	console.log("the user json is : " + userjson);
	bl.loginUserWithFacebook(userjson ,res);
});

app.post('/getMarinesPicturesPaths',function(req,res)
{
    console.log("getMarinesPicturesPaths");
    bl.getMarinesPicturesPaths(res);
});

app.post('/getMarineById',function(req,res)
{
    var marine = req.body.LaunchMyBoat;
    console.log("the marine param is : " + marine);
    var marinejson = JSON.parse(marine);
    console.log("getMarineById");
    bl.getMarineById(marinejson ,res);
});

app.post('/getUserBoatsByID',function(req,res)
{
     var userID = req.body.LaunchMyBoat;
        console.log("userID : " + userID);
        var Ujson = JSON.parse(userID);
        bl.getUserBoatsByID(Ujson, res);
});

app.post('/getAllMarines', function(req,res)
{
     console.log("getAllMarines");
     bl.getAllMarines(res);
});

app.post('/getWorkerById', function(req,res)
{
    var worker = req.body.LaunchMyBoat;
    console.log("getWorkerById : " + worker);
    var wjson = JSON.parse(worker);
    bl.getWorkerById(wjson, res);
});

app.post('/getServiceById', function(req,res)
{
    var service = req.body.LaunchMyBoat;
    console.log("getServiceById : " + service);
    var jsonObj = JSON.parse(service);
    bl.getServiceById(jsonObj, res);
});

app.post('/getBoatByID', function(req,res)
{
    var object = req.body.LaunchMyBoat;
    var json = JSON.parse(object);
     bl.getBoatByID(json, res);
});

app.post('/addABoat', function(req,res)
{
     var object = req.body.LaunchMyBoat;
     var json = JSON.parse(object);
     bl.addABoat(json, res);
});

app.post('/orderGetBoat', function(req,res)
{
     var object = req.body.LaunchMyBoat;
     var json = JSON.parse(object);
     bl.orderGetBoat(json, res);
});

app.post('/updateOrderGetBoat', function(req,res)
{
     var object = req.body.LaunchMyBoat;
     var json = JSON.parse(object);
     bl.updateOrderGetBoat(json, res);
});

var os = require('os');

var interfaces = os.networkInterfaces();
var addresses = [];
for (var k in interfaces) {
    for (var k2 in interfaces[k]) {
        var address = interfaces[k][k2];
        if (address.family === 'IPv4' && !address.internal) {
            addresses.push(address.address);
        }
    }
}

console.log(addresses);