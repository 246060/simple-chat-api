const express = require("express");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const jwt = require("jsonwebtoken");
const indexRouter = require("./routes/index");
const { Server } = require("socket.io");
const { createAdapter } = require("@socket.io/redis-adapter");
const { createClient } = require("redis");
const { checkSocketToken, join } = require("./service/eventService");

const app = express();

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use("/", indexRouter);

// app.io = io();
const io = new Server();
app.io = io;
app.io.use(checkSocketToken);

/**
 * socket.io emit : event.chat.server
 * socket.io listen : event.chat.client
 * redis listen : event.api.server
 */

app.io.on("connection", function (socket) {
  let token = socket.handshake.headers.authorization.split(" ")[1];
  let decoded = jwt.decode(token);
  console.log(`User(${decoded.sub}) Socket Connection Success`);

  //socket.join("all");
  socket.emit("event.chat.server", {
    message: `Socket Connection Success`,
  });

  socket.on("event.chat.client", function (req) {
    /**
     * req : {
         "type" : "channel | user",
         "targetId" : channelId | userId
       }
     */
    if ("channel" == req.type || "user" == req.type) {

      /**
       * join("channel" + channelId) 
       * or
       * join("user" + userId)
       */
      console.log(`${req.type}(${req.targetId}) Socket Connection Success`)

      socket.join(req.type + req.targetId);
      socket.emit("event.chat.server", {
        message: `${req.type} Socket Connection Success`,
      });
    }
  });

  socket.on("disconnect", (reason) => {
    console.log("disconnect : ", reason);
  });
});

(async () => {
  const client = createClient();
  const subscriber = client.duplicate();

  await subscriber.connect();
  await subscriber.subscribe("event.api.server", (event) => {
    console.log(event);

    const data = JSON.parse(event);
    let routing = data.routing;
    let message = data.message;

    if (routing.type == "to.all") {
      app.io.emit("event.chat.server", message);
    } else {
      if (routing.type == "to.channel" || routing.type == "to.user") {
        let routingKey;

        if (routing.type == "to.channel") {
          routingKey = "channel" + routing.targetId;
        } else {
          routingKey = "user" + routing.targetId;
        }

        app.io.to(routingKey).emit("event.chat.server", message);
      }
    }
  });
})();

module.exports = app;
