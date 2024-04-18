var db = require('../config/db.config.js');
var MarkValue = db.mark_value;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    MarkValue.findAll()
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


exports.create = (req, res) => { 
    MarkValue.create({
        user_id:req.body.userId,
        kind_of_mark_id:req.body.kind_of_mark_id,
        date: req.body.date,
        value_number1: req.body.value_number1,
        value_number2: req.body.value_number2,
        value_bool: req.body.value_bool,
        value_string: req.body.value_string,
        value_enum: req.body.value_enum
    }).then(object => {
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};


exports.MarksForUser= (req, res) => {
    MarkValue.findAll({
        where: {
            user_id: req.params.userId,
            kind_of_mark_id: req.params.kindOfMarkId
        }
    })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};