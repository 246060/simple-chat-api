const express = require("express");
const router = express.Router();
const jwt = require("jsonwebtoken");

router.post("/relay", function (req, res, next) {
  console.log(req.body);
  const io = req.app.io;
  let event;

  if (req.body.meta.type == "chat") {
    console.log("chat");
    req.body.timestamp = new Date().toISOString();
    event = io.to(req.body.data.roomId);
  } else if (req.body.meta.type == "person") {
    console.log("person");
    event = io.to(req.body.data.userId);
  } else if (req.body.meta.type == "all") {
    console.log("all");
    event = io;
  }

  event.emit("event.server", req.body);
  res.json({ msg: "relay success" });
});

router.post("/token", function (req, res, next) {
  console.log(req.body);

  let payload = { sub: "user-01" };
  let secret = "password";
  let token = jwt.sign(payload, secret, { expiresIn: "10h" });
  console.log(jwt.verify(token, secret));

  res.json({
    msg: "create token success",
    token: token,
  });
});

module.exports = router;
