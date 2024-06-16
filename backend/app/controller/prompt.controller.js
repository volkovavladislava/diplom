var db = require('../config/db.config.js');

var Prompt = db.prompt;
var globalFunctions = require('../config/global.functions.js');
const moment = require('moment-timezone');

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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    Prompt.create({
        user_id:req.body.userId,
        name: req.body.name,
        description: req.body.description,
        date: utcTime,
        calendar_id: req.body.calendar_id,
    }).then(object => {
	console.log( object ) 
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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    Prompt.update({
            name: req.body.name,
            description: req.body.description,
            date: utcTime,
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
