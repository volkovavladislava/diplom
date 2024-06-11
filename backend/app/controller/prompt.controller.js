var db = require('../config/db.config.js');

var Prompt = db.prompt;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    Prompt.findAll()
        .then(objects => {
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
    console.log(req.body.calendar_id)
    Prompt.create({
        user_id:req.body.userId,
        name: req.body.name,
        description: req.body.description,
        date: req.body.date,
        calendar_id: req.body.calendar_id,
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
        },
        order: [['date', 'DESC']]
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
    console.log(req.body.calendar_id)
    const calId = req.body.calendar_id === undefined ? null : req.body.calendar_id;
    console.log(calId)
    Prompt.update({
            name: req.body.name,
            description: req.body.description,
            date: req.body.date,
            calendar_id: calId,
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

exports.delete = (req, res) => {
    Prompt.destroy({
        where: {
            id: req.params.promptId
        }
    }).then(() => {
        globalFunctions.sendResult(res, 'Запись удалена');
    }).catch(err => {
        globalFunctions.sendError(res, err);
    });
};
