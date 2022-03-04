const jwt = require("jsonwebtoken");

function isValidJwt(authorization) {
  if (!authorization || !authorization.startsWith("Bearer")) {
    return false;
  }

  let token = authorization.split(" ")[1];
  console.log("token :", token);

  try {
    let decoded = jwt.verify(token, "password");
    return true;
  } catch (error) {
    console.error("socket connection fail", error);
    return false;
  }
}

module.exports = {
  checkApiToken: function (req, res, next) {
    console.log(req.url);
    if (req.url == "/token") {
      next();
    }

    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];

    if (token == null) {
      res.sendStatus(401);
      next(new Error("Authentication Error"));
    } else {
      jwt.verify(token, "password", (err, decoded) => {
        if (err) {
          console.log(err);
          next(new Error("Authentication Error"));
        }
        next();
      });
    }
  },

  checkSocketToken: function (socket, next) {
    let authorization = socket.handshake.headers.authorization;
    if (isValidJwt(authorization)) {
      next();
    } else {
      next(new Error("Socket Authentication Error"));
    }
  },

  join: function (socket, args) {
    let response;

    if ("person" == args.type) {
      socket.join(args.userId);
      response = {
        meta: { type: "person" },
        data: { msg: `Person Socket Connection Success` },
      };
      socket.emit("event.server", response);
    } else if ("chat" == args.type) {
      socket.join(args.roomId);
      response = {
        meta: { type: "chat" },
        data: { msg: `Chat Socket Connection Success` },
      };
      socket.emit("event.server", response);
    }else if("all" == args.type){
      socket.join("all");
      response = {
        meta: { type: "all" },
        data: { msg: `all Socket Connection Success` },
      };
      socket.emit("event.server", response);
    }

    return response;
  },
};
