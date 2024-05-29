var db = require('../config/db.config.js');
var User = db.user;
var globalFunctions = require('../config/global.functions.js');
var bcrypt = require("bcryptjs");

exports.findAll = (req, res) => {
    User.findAll()
        .then(objects => {
            // возврат найденных записей
            // console.log("objects ")
            // console.log(objects)
            // console.log("res ")
            // console.log( res)
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};




// Обновление данных пользователя по id
exports.update = (req, res) => {
    User.update({
            name: req.body.name,
            height: req.body.height,
            weight: req.body.weight,
            date_birth: req.body.date_birth
        },
        {
            where: {
                id: req.params.userId
            }
        }
    ).then(object => {
        // console.log( object)
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};


// Получение данных пользователя по id
exports.findById = (req, res) => {
    User.findByPk(req.params.id)
        .then(object => {
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};




exports.generateGuest = (req, res) => {

    const characters = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const passwordLength = 8;
    let password = '';

    for (let i = 0; i < passwordLength; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        password += characters[randomIndex];
    }

    User.update({
            guest_password: bcrypt.hashSync(password, 10) ,
            date_guest_password: getCurrentDateTime()
        },
        {
            where: {
                id: req.params.id
            }
        }
    ).then(object => {
            // User.findByPk(req.params.id)
            // .then(object => {
            //     globalFunctions.sendResult(res, object);
            // })
            // .catch(err => {
            //     globalFunctions.sendError(res, err);
            // })
            let jsonObject = {
                guest_password: password
            };
            let jsonString = JSON.stringify(jsonObject);

            globalFunctions.sendResult(res, jsonString);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};


exports.loginGuest = (req, res) => {
    
    User.findOne({
        where: {
            login: req.body.login
        }
    }).then(user => {
        
        if (!user) {
            res.status(404).send({ message: "Неверно введенный логин и/или пароль" });
            return;
        }

        if (user.guest_password == null) {
            res.status(404).send({ message: "Неверно введенный логин и/или пароль" });
            return;
        }



        var passwordIsValid = bcrypt.compareSync(
            req.body.password,
            user.guest_password
        );
        if (!passwordIsValid) {
            res.status(401).send({
                accessToken: null,
                message: "Неверно введенный логин и/или пароль"
            });
            console.log("пароль не подошел")
            return;
        }



        const now = new Date();
        const passtime = now - user.date_guest_password;

        if(Math.floor(passtime / 1000 / 60) > 40){
            
            console.log("Временный пароль устарел")
            User.update({
                guest_password: null ,
                date_guest_password: null
            },
            {
                where: {
                    id: user.id
                }
            })
            // ).then(a => {    
            //     globalFunctions.sendResult(res, a);
            // }).catch(err => {
            //     globalFunctions.sendError(res, err);
            // })
            res.status(403).send({
                accessToken: null,
                message: "Временный пароль устарел. Требуется сгенерировать новый."
            });
            return;
        }


        var object = {
            id: user.id,
            name: user.name,
            login: user.login,
            height: user.height,
            weight: user.weight,
            date_birth: user.date_birth,
            gender: user.gender
        };
        globalFunctions.sendResult(res, object);

        // User.update({
        //     guest_password: null ,
        //     date_guest_password: null
        // },
        // {
        //     where: {
        //         id: user.id
        //     }
        // }
        // ).then(a => {    
        //     globalFunctions.sendResult(res, object);
        // }).catch(err => {
        //     globalFunctions.sendError(res, err);
        // })

    })
    .catch(err => {
        globalFunctions.sendError(res, err);
    });
    
    
};



exports.logoutGuest = (req, res) => {
    
    User.update({
        guest_password: null ,
        date_guest_password: null
    },
    {
        where: {
            id: req.body.id
        }
    }
    ).then(a => {    
        globalFunctions.sendResult(res, a);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
    
};


exports.checkGuest = (req, res) => {
    
    User.findOne({
        where: {
            login: req.body.login
        }
    }).then(user => {
        
        if (!user) {
            res.status(404).send({ message: "Неверно введенный логин и/или пароль" });
            return;
        }

        if (user.guest_password == null) {
            res.status(404).send({ message: "Неверно введенный логин и/или пароль" });
            return;
        }


        const now = new Date();
        const passtime = now - user.date_guest_password;

        if(Math.floor(passtime / 1000 / 60) > 40){
            res.status(403).send({
                accessToken: null,
                message: "Временный пароль устарел. Требуется сгенерировать новый."
            });
            console.log("Временный пароль устарел")
            User.update({
                guest_password: null ,
                date_guest_password: null
            },
            {
                where: {
                    id: user.id
                }
            }
            ).then(a => {    
                globalFunctions.sendResult(res, a);
            }).catch(err => {
                globalFunctions.sendError(res, err);
            })
            // return;
        }


        var object = {
            id: user.id,
            name: user.name,
            login: user.login,
            height: user.height,
            weight: user.weight,
            date_birth: user.date_birth,
            gender: user.gender
        };
        globalFunctions.sendResult(res, object);
    })
    .catch(err => {
        globalFunctions.sendError(res, err);
    });
    
    
};




function getCurrentDateTime() {
    const now = new Date();
    
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
