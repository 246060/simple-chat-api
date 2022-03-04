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

app.io.on("connection", function (socket) {
  console.log("New Socket Connected");

  socket.on("event.client", function (msg) {
    let token = socket.handshake.headers.authorization.split(" ")[1];
    let decoded = jwt.decode(token);
    join(socket, { type: msg.type, userId: decoded.sub, roomId: msg.roomId });
  });

  socket.on("disconnect", (reason) => {
    console.log("disconnect : ", reason);
  });
});

(async () => {
  const client = createClient();
  const subscriber = client.duplicate();
  await subscriber.connect();

  await subscriber.subscribe("relay", (event) => {
    console.log(event);
    const reply = JSON.parse(event);

    if (reply.type == "chat") {
      app.io.to(reply.roomId).emit("event.server", reply);
    } else if (reply.type == "person") {
      app.io.to(reply.userId).emit("event.server", reply);
    } else if (reply.type == "all") {
      app.io.to("all").emit("event.server", reply);
    }
  });
})();

module.exports = app;
