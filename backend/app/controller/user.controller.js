var db = require('../config/db.config.js');
var User = db.user;
var globalFunctions = require('../config/global.functions.js');


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