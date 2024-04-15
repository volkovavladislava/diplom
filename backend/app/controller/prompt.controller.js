var db = require('../config/db.config.js');

var Prompt = db.prompt;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    Prompt.findAll()
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

// Добавление пользователя
exports.create = (req, res) => {
    console.log( req.body.date) 
    Prompt.create({
        user_id:req.body.userId,
        name: req.body.name,
        description: req.body.description,
        date: req.body.date,
    }).then(object => {
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};


// Получение данных пользователя по id
exports.findByUserId = (req, res) => {
    Prompt.findAll({
        where: {
            user_id: req.params.userId
        }
    }).then(object => {
            
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


exports.findById = (req, res) => {
    Prompt.findByPk(req.params.promptId)
        .then(object => {
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};

exports.update = (req, res) => {
    Prompt.update({
            name: req.body.name,
            description: req.body.description,
            date: req.body.date
        },
        {
            where: {
                id: req.params.promptId
            }
        }
    ).then(object => {
        // console.log( object)
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};
