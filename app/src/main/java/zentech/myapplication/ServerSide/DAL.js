var mydb = require('./myDB')
 , ObjectID = require('mongodb').ObjectID;

var connection;
mydb.openDatabase(function (err, db) {
    if (db != null) {
        connection = db;
    }
});

module.exports = {

	/*addNewUser: function (newUser, callback){
		var collection = connection.collection("Users");
		
		var result = collection.insert({
			Email: newUser.email
		}, function (err, data){
			
			if(err){
				console.log('new user error: ' + err);
				callback(err,data);
			}
			else{
				console.log('new user success');
                callback(null, data.ops[0]);
			}			
		});
	},*/

	addABoat: function(boat, callback)
	{
	    var collection = connection.collection("Boats");

	    var result = collection.insert({
        			Name: boat.name,
        			IsDocked: false,
        			IsRacked: false,
        			IsForSale: boat.isForSale,
        			Weight: boat.weight,
        			OwnerID: boat.ownerID,
        			RackID: 0,
        			DockName: "",
        			AtMarine:"",
        			LastTreatedTime:boat.time,
        			LastTreatedDate:boat.date,
        			PermittedUsersIDs:[],
        			GetBoatOrdersIDs:[],
        			ReturnBoatOrdersIDs:[]
        		}, function (err, data){

        			if(err){
        				console.log('add boat error: ' + err);
        				callback(err,data);
        			}
        			else{
        				console.log('boat was added successfully');
                        callback(null, data.ops[0]);
        			}
        		});
	},

	addGetBoatOrderToMarina: function (order, callback)
	{
        var collection = connection.collection("Marines");
        console.log("the param : order._id is : " + order._id);
        var o_id = ObjectID(order.marineID);

        var result = collection.update({_id:o_id},{ $addToSet: { GetBoatOrders: order._id } }, function(err)
        {
           if(err){
               console.log('could not add the order to the marina: ' + err);
               callback(err);
           }
           else
           {
               console.log('the order was added to the marina: ' + result);
               callback(null);
           }
       });
	},

	orderGetBoat: function(order,callback)
	{
	    var collection = connection.collection("GetBoatOrders");
	    var result = collection.insert({
                			BoatID: order.boatID,
                			MarineID: order.marineID,
                			OrderingUserID: order.orderingUserID,
                			Date: order.date,
                			Time: order.time,
                			Ice: order.ice,
                			Fuel: order.fuel,
                			Services:order.moreServices
                		}, function (err, data){

                			if(err){
                				console.log('add getBoatOrder error: ' + err);
                				callback(err,data);
                			}
                			else{
                				console.log('get boat order was added successfully');
                                callback(null, data.ops[0]);
                			}
                		});
	},

	updateOrderGetBoat: function(order,callback)
    	{
            var collection = connection.collection("GetBoatOrders");
            console.log("the order id is : " + order._id);
            var o_id = ObjectID(order._id);
            var u_id = order.orderingUserID + "";
            //TODO: add services to the order
            collection.update({_id: o_id }, { $set: { Date:order.date, Time:order.time, BoatID:order.boatID, OrderingUserID:order.orderingUserID,
                                                                                     MarineID:order.marineID, Ice:order.ice, Fuel:order.fuel, Services:order.moreServices }}, function(err)
                {
                     if (err)
                     {
                         console.log('update get boat order error: ' + err);
                         callback(err,null);
                     }
                     else
                     {
                         console.log('get boatr order updated successfully');
                         callback(null, order);
                     }
                 });
    	},

	RelateBoatToUser: function(boat, callback)
	{
	    var collection = connection.collection("Users");
	    console.log("the boat id is : " + boat._id);
        // it is OwnerID with capital O because the properties came from the DB !
        var o_id = ObjectID(boat.OwnerID);
        var b_id = boat._id + "";


       collection.update({_id: o_id }, { $push: { BoatsIDs: b_id }}, function(err)
        {
             if (err)
             {
                 console.log('update owner boat error: ' + err);
                 callback(err,null);
             }
             else
             {
                 console.log('boat was related to owner successfully');
                 callback(null, boat);
             }
         });
	},





	findUserByEmailAndPass: function (email,password, callback)
	{
	    var collection = connection.collection("Users");

        var result = collection.findOne({Email: email, Password: password}, function(err,result){
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	findUserByEmail: function (email, callback)
	{
	    var collection = connection.collection("Users");

	     var result = collection.findOne({Email: email}, function(err,result){
                    if(err){
                        console.log('err: ' + err);
                        callback(err,result);
                    }
                    else
                    {
                        console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                        callback(null,result);
                    }
                });
	},

	findUserByFacebookID: function (id, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.findOne({FacebookID: id}, function(err,result){
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	createNewUserByEmail: function (email,password,name,zipCode,dob, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.insert({
        			//Favorites: newUser.favorites,
        			FacebookID: "",
        			Name: name,
        			FacebookEmail: "",
        			Email:email,
        			Password:password,
        			ZipCode:zipCode,
        			DateOfBirth:dob,
        			BoatsIDs:[]
        		}, function (err, data){

        			if(err){
        				console.log('new user error: ' + err);
        				callback(err,data);
        			}
        			else{
        				console.log('new user success');
                        callback(null, data.ops[0]);
        			}
        		});
	},

	createNewUserByFacebookID: function (id,email,name, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.insert({
        			//Favorites: newUser.favorites,
        			FacebookID: id,
        			Name: name,
        			FacebookEmail: email,
        			Email:"",
        			Password:"",
        			ZipCode:"",
                    DateOfBirth:"",
                    BoatsIDs:[]
        		}, function (err, data){

        			if(err){
        				console.log('new user error: ' + err);
        				callback(err,data);
        			}
        			else{
        				console.log('new user success');
                        callback(null, data.ops[0]);
        			}
        		});
	},

	updateUserDetailsByFacebook: function(id,email,name,callback)
	{
         var collection = connection.collection("Users");

          var result = collection.update(
                 {FacebookID: id },
                 {Name: name,
                  FacebookEmail: email
                 }, function (err, numberOfRecordsUpdatedJson) {
                     if (err) {
                             console.log('update User Details By FacebookID error: ' + err);
                             callback(err, objectWithStatusOperation);
                         }
                         else {
                             console.log('update User Details By FacebookID success');
                             console.log('number of records Changed : ' + numberOfRecordsUpdatedJson);
                             callback(null, numberOfRecordsUpdatedJson);
                         }
                 });
	},

	getAllMarinesIds: function(callback)
	{
	    var results;
        connection.collection("Marines").find({},{ fields: { 'Name': 0, 'FacebookID':0,'FacebookEmail':0,'Email':0,'Docks':0,'WorkersIDs':0,'ReturnBoatServicesIDs':0,
        'GetBoatServicesIDs':0, 'RackedBoatsIDs':0} })
        .toArray(function (err, ids)
        {
            if(err)
            {
                console.log("get Marines ids err : " + err);
                callback(err, results);
            }
            else
            {
                results = ids;
                console.log(results);
                callback(null, results);
            }
        });
	},

	getMarineById: function(id ,callback)
	{
         var collection = connection.collection("Marines");
         var o_id = ObjectID(id);

         var result = collection.findOne({_id:o_id}, function(err,result)
         {
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 marine !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},
	getUserBoatsByID: function(id ,callback)
    {
           var collection = connection.collection("Users");
           var o_id = ObjectID(id);

           var result = collection.find({_id:o_id}, { fields: {'_id':0, 'Name': 0, 'FacebookID':0,'FacebookEmail':0,'Email':0,'Password':0,'ZipCode':0,'DateOfBirth':0}}
           ).toArray(function(err,result)
           {
              if(err){
                  console.log('err: ' + err);
                  callback(err,result);
              }
              else
              {
                  console.log('!@#!@#!@#!@!#@!#@ found the boats !#!@#!@#!@#!@#!@# : ' + result);
                  callback(null,result);
              }
          });
    },

	getWorkerById: function(id,callback)
	{
         var collection = connection.collection("Workers");
         var o_id = ObjectID(id);

         var result = collection.findOne({_id:o_id}, function(err,result)
         {
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 worker !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	getServiceById: function(id,callback)
    	{
             var collection = connection.collection("Services");
             var o_id = ObjectID(id);

             var result = collection.findOne({_id:o_id}, function(err,result)
             {
                if(err){
                    console.log('err: ' + err);
                    callback(err,result);
                }
                else
                {
                    console.log('!@#!@#!@#!@!#@!#@ found 1 service !#!@#!@#!@#!@#!@# : ' + result);
                    callback(null,result);
                }
            });
    	},
    getBoatByID: function(id,callback)
    {
        console.log('the bots id is : ' + id);
         var collection = connection.collection("Boats");
         var o_id = ObjectID(id);

         var result = collection.findOne({_id:o_id}, function(err,result)
         {
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 boat !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
    },

	getAllMarines: function(callback)
	{
	     var collection = connection.collection("Marines");

         var result = collection.find({}).toArray(function(err, results)
         {
            if(err){
                console.log('err: ' + results);
                callback(err,results);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 marine !#!@#!@#!@#!@#!@# : ' + results);
                callback(null,results);
            }
        });
	}
};