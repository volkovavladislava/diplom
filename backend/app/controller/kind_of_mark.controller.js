var db = require('../config/db.config.js');
var KindOfMark = db.kind_of_mark;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    KindOfMark.findAll({
        where: {
            user_id: null
        }
    })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};


exports.findAllHandMade = (req, res) => {
    KindOfMark.findAll({
        where: {
            user_id: req.params.userId
        }
    })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};


exports.create = (req, res) => { 
    KindOfMark.create({
        user_id:    req.params.userId,
        name:   req.body.name,
        enum_kind_of_mark_id: req.body.enum_kind_of_mark_id
    }).then(object => {
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};