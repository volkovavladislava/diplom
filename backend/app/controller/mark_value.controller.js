var db = require('../config/db.config.js');
var MarkValue = db.mark_value;
var globalFunctions = require('../config/global.functions.js');
const { Op } = require('sequelize');

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

exports.update = (req, res) => {
    MarkValue.update({
            user_id:req.body.userId,
            kind_of_mark_id:req.body.kind_of_mark_id,
            date: req.body.date,
            value_number1: req.body.value_number1,
            value_number2: req.body.value_number2,
            value_bool: req.body.value_bool,
            value_string: req.body.value_string,
            value_enum: req.body.value_enum
        },
        {
            where: {
                id: req.params.markId
            }
        }
    ).then(object => {
        // console.log( object)
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




// ,
//         order: [['date', 'DESC']]

exports.MarksForUserByDate= (req, res) => {
    MarkValue.findAll({
        where: {
            user_id: req.params.userId,
            kind_of_mark_id: req.params.kindOfMarkId,
            date: {
                [Op.gte]: req.params.date1,
                [Op.lte]: req.params.date2
            }
        }
    })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


exports.findById = (req, res) => {
    MarkValue.findByPk(req.params.markId)
        .then(object => {
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};

exports.delete = (req, res) => {
    MarkValue.destroy({
        where: {
            id: req.params.markId
        }
    }).then(() => {
        globalFunctions.sendResult(res, 'Запись удалена');
    }).catch(err => {
        globalFunctions.sendError(res, err);
    });
};