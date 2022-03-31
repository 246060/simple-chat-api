const jwt = require("jsonwebtoken");

function isValidJwt(authorization) {
  if (!authorization || !authorization.startsWith("Bearer")) {
    return false;
  }

  let token = authorization.split(" ")[1];
  console.log("token :", token);

  try {
    //let secret = Buffer.from("jCIIGf7BKMMszsfSIXYS2v/CI1+aQ0fl4yZegXjVQLk=", "base64").toString('utf8');
    let secret = Buffer.from(
      "jCIIGf7BKMMszsfSIXYS2v/CI1+aQ0fl4yZegXjVQLk=",
      "base64"
    );
    let decoded = jwt.verify(token, secret);
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

};
