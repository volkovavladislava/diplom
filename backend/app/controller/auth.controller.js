var db = require("../config/db.config");
var config = require("../config/auth.config");
var User = db.user;
var globalFunctions = require('../config/global.functions.js');
var jwt = require("jsonwebtoken");
var bcrypt = require("bcryptjs");

exports.register = (req, res) => {

    console.log(req.body.date_birth)
    console.log(req.body.gender)

    let g = 0
    if(req.body.gender == "m" || req.body.gender == "м" || req.body.gender == "М" || req.body.gender == "M"){
        g = 1
    }
    if( req.body.gender == "ж" || req.body.gender == "Ж" ){
        g = 0
    }
    
    User.create({
        name: req.body.name,
        height: req.body.height,
        weight: req.body.weight,
        date_birth: req.body.date_birth,
        gender: g,
        login: req.body.login,
        password: bcrypt.hashSync(req.body.password, 10) // генерация хеша пароля
    })
        .then(() => {
            var result = {
                message: "Пользователь зарегистрирован"
            };
            globalFunctions.sendResult(res, result);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        });
};

exports.login = (req, res) => {
    User.findOne({
        where: {
            login: req.body.login
        }
    })
        .then(user => {
            if (!user) {
                res.status(404).send({ message: "Неверно введенный логин и/или пароль" });
                return;
            }

            var passwordIsValid = bcrypt.compareSync(
                req.body.password,
                user.password
            );
            if (!passwordIsValid) {
                res.status(401).send({
                    accessToken: null,
                    message: "Неверно введенный логин и/или пароль"
                });
                return;
            }

            var token = jwt.sign({ id: user.id }, config.secret, {
                expiresIn: 1000 // 8 часов — время действия токена  28800 
            });

            var object = {
                id: user.id,
                name: user.name,
                login: user.login,
                height: user.height,
                weight: user.weight,
                date_birth: user.date_birth,
                gender: user.gender,
                accessToken: token
            };
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        });
};

exports.userBoard = (req, res) => {
    globalFunctions.sendResult(res, "Пользователь авторизован");
};