   var Dal = require('./DAL')
  , Apriori = require('apriori')
  , Underscore = require('underscore');

// -----------------------------------
// -------------- Add ----------------
// -----------------------------------

 exports.loginUser = function (user, res)
 {
    console.log("loginuser method got : " + user.email + " ," + user.password)

	Dal.findUserByEmailAndPass(user.email, user.password, function(err, user)
	{
	    if(err)
	    {
            console.log("Faild to check if the user already exists: " + err);
			res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
		}
		else
		{
		    if(user)
		    {
		        console.log("User exists");
		        res.send(JSON.stringify({user: user }));
		    }
		    else
		    {
		         console.log("User doesn't exists");
		         res.send(JSON.stringify({ error:"User doesn't exists", user: user }));
		    }
		}
    });
 };

 exports.orderGetBoat = function (order,res)
  {
    console.log("orderGetBoatWithoutServices method got : " + order );
    var globalOrder = order;

  	Dal.orderGetBoat(order, function(err, order)
  	{
  	    if(err)
  	    {
            console.log("Failed to add the order: " + err);
  			res.send(JSON.stringify({ error:"Failed to add the order: " + err, order: order }));
  		}
  		else
  		{
             console.log("GetBoatOrder created");
             console.log("Adding the order to the marina's orders");

             Dal.addGetBoatOrderToMarina(order, function(err)
             {
                if(err)
                {
                    console.log("Failed to add the order to the marina: " + err);
                    res.send(JSON.stringify({ error:"Failed to add the order to the marina: " + err, order: order }));
                }
                else
                {
                    console.log("The get boat order was added to the marina");
                    res.send(JSON.stringify({order:globalOrder}))
                }
             });
  		}
      });
  };

  exports.updateOrderGetBoat = function(order,res)
  {
      console.log("updateOrderGetBoat method got : " + order )

      Dal.updateOrderGetBoat(order, function(err, order)
      {
        if(err)
        {
            console.log("Failed to update the order: " + err);
            res.send(JSON.stringify({ error:"Failed to update the order: " + err, order: order }));
        }
        else
        {
             console.log("GetBoatOrder updated");
             res.send(JSON.stringify({order:order}))
        }
      });
  };

 exports.addABoat = function (boat,res)
 {
    console.log("addABoat method got : " + boat )

 	Dal.addABoat(boat, function(err, boat)
 	{
 	    if(err)
 	    {
             console.log("Faild to add the boat: " + err);
 			res.send(JSON.stringify({ error:"Faild to add the boat: " + err, boat: boat }));
 		}
 		else
 		{
 		    if(boat)
 		    {
 		        console.log("boat was added trying to relate it to owner");
 		        console.log("the boat id : " +boat._id );

                Dal.RelateBoatToUser(boat, function (err, boat)
                {
                    if(err)
                    {
                        console.log('Relate Boat To User  error: ' + err);
                       res.send(JSON.stringify({error: 'Relate Boat To User  error: ' + err }));
                    }
                    else
                    {
                        console.log('Relate Boat To User success');
                        res.send(JSON.stringify({boat: boat }));
                    }
                });
 		    }
 		    else
 		    {
 		         console.log("boat was not added");
 		         res.send(JSON.stringify({ error:"boat was not added and there was no error, check db" }));
 		    }
 		}
     });
 };

 exports.createNewUser = function (user,res)
 {
     console.log("createNewUser method got : " + user.email + " ," + user.password);
      var userDetails = user;

     Dal.findUserByEmail(user.email, function(err,user)
     {
        if(err)
	    {
            console.log("Faild to check if the user already exists: " + err);
			res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
		}
		else
		{
		    if(user)
		    {
		        console.log("User exists");
		        res.send(JSON.stringify({user: user }));
		    }
		    else
		    {
		         console.log("Registering the user");
		         //pay attention that at this point there is no user name yet..
                 Dal.createNewUserByEmail(userDetails.email,userDetails.password,userDetails.name,userDetails.zipCode,userDetails.dob, function(err,user)
                 {
                    if(err)
                    {
                        console.log("Failed to register new user: " + err);
                        res.send(JSON.stringify({ error:"Failed to register new user: " + err, user: null }));
                    }
                    else
                    {
                         console.log("User was created : " + user.Email);
                         res.send(JSON.stringify({user: user }));
                    }
                 });
		    }
		}
     });


 };

 exports.loginUserWithFacebook = function (user, res)
  {
     console.log("loginUserWithFacebook method got : " + user.facebookID)
     var userDetails = user;

 	Dal.findUserByFacebookID(user.facebookID, function(err, user)
 	{
 	    if(err || user == null)
 	    {
 			console.log("user doesnt exists, starting registering process");
 			Dal.createNewUserByFacebookID(userDetails.facebookID,userDetails.facebookEmail,userDetails.name, function(err,user)
 			{
 			    if(err)
               {
                     console.log("Faild to register user by facebook details: " + err);
                     res.send(JSON.stringify({ error:"Faild to register user by facebook details: " + err, user: user }));
               }
               else
               {
                    console.log("User was created");
                    res.send(JSON.stringify({user: user }));
               }
 			});
 		}
 		else
 		{
 		    if(user)
 		    {
 		        console.log("User exists");

 		        Dal.updateUserDetailsByFacebook(user.facebookID, user.facebookEmail,user.name, function(err,user)
 		        {
 		             if(err)
                       {
                     	   console.log("Faild to update user details by facebook details: " + err);
                       }
                      else
                      {
                         console.log("User details were updated");
                      }
 		        });

 		        res.send(JSON.stringify({user: user }));
 		    }
 		    else
 		    {
 		         console.log("User doesn't exists");
 		         res.send(JSON.stringify({error:"Faild to register user by facebook details: " + err, user: user }));
 		    }
 		}
     });
  };

 /*exports.addUser = function (user, res)
 {
    Dal.findUserByMail(user.email, function(err, user)
    {
        if(err)
        {
            console.log("Faild to check if the user already exists: " + err);
            res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
        }
        else
        {
            if(user)
            {
                console.log("User exists");
                res.send(JSON.stringify({error:"User exists" , user: user }));
            }
		    else
		    {
		       Dal.addNewUser(userjson,function (err, user)
		       {
                   if (err)
                   {
                        console.log("Faild to add new user: " + err);
                        res.send(JSON.stringify({ error:"Faild to add new user: " + err, user: user }));
                   }
                   else
                   {
                       console.log("New user saved!");
                       res.send(JSON.stringify({user: user }));
                   }
               });
		    }
		}
	});

 };*/

 exports.getMarinesPicturesPaths = function(res)
 {
    Dal.getAllMarinesIds(function(err,ids)
    {
        if(err)
        {
             console.log("Faild to get marines ids: " + err);
             res.send(JSON.stringify({ error:"Faild to get marines ids: " + err, marines: ids }));
        }
        else
        {
              console.log("Got all Marine Ids");
              res.send(JSON.stringify({marines: ids }));
        }
    });
 };

 exports.getMarineById = function(marine, res)
 {
    Dal.getMarineById(marine.id, function(err,marine)
    {
        if(err)
        {
            console.log("Failed to get marine by id : " + err );
            res.send(JSON.stringify({ error:"Failed to get marine by id : " + err, marine: marine }));
        }
        else
        {
            console.log("Got the Marine by id");
            res.send(JSON.stringify({marine: marine}));
        }
    });
 };

 exports.getUserBoatsByID = function(user,res)
 {
        Dal.getUserBoatsByID(user.id, function(err,boats)
        {
            if(err)
                {
                    console.log("Failed to get boat by id : " + err );
                    res.send(JSON.stringify({ error:"Failed to get boat  by id : " + err, boats: boats }));
                }
                else
                {
                    console.log("Got the boat by id");
                    res.send(JSON.stringify({boats: boats}));
                }
        });
 };

 exports.getBoatByID = function(boat,res)
 {
    Dal.getBoatByID(boat.id, function(err,boat)
         {
             if(err)
             {
                 console.log("Failed to get boat by id : " + err)
                 res.send(JSON.stringify({ error:"Failed to get boat by id : " + err, boat: boat }));
             }
             else
             {
                 console.log("Got the boat by id");
                 res.send(JSON.stringify({boat:boat}));
             }
         });
 };

 exports.getWorkerById = function(worker,res)
 {
    Dal.getWorkerById(worker.id, function(err,worker)
    {
        if(err)
        {
            console.log("Failed to get worker by id : " + err)
            res.send(JSON.stringify({ error:"Failed to get worker by id : " + err, worker: worker }));
        }
        else
        {
            console.log("Got the worker by id");
            res.send(JSON.stringify({worker:worker}));
        }
    });
 };

  exports.getServiceById = function(service,res)
  {
     Dal.getServiceById(service.id, function(err,service)
     {
         if(err)
         {
             console.log("Failed to get service by id : " + err)
             res.send(JSON.stringify({ error:"Failed to get service by id : " + err, service: service }));
         }
         else
         {
             console.log("Got the service by id");
             res.send(JSON.stringify({service:service}));
         }
     });
  };

 exports.getAllMarines = function(res)
 {
    Dal.getAllMarines(function(err,marines)
    {
        if(err)
        {
            console.log("Failed to get all marines : " + err);
            res.send(JSON.stringify({ error:"Failed to get all marines : " + err, marines: marines }));
        }
        else
        {
            console.log("Got all the Marine");
            res.send(JSON.stringify({marines: marines}));
        }
    });
 };