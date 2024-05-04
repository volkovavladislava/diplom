var db = require('../config/db.config.js');
var MarkValue = db.mark_value;
var globalFunctions = require('../config/global.functions.js');
const { Op } = require('sequelize');

exports.findAll = (req, res) => {
    MarkValue.findAll()
        .then(objects => {
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
        value_number: req.body.value_number,
        value_string: req.body.value_string,
        value_enum: req.body.value_enum
    }).then(object => {
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};

exports.createD = (req, res) => { 

    const dav1 = MarkValue.create({
        user_id:    req.body.userId,
        kind_of_mark_id:    1,
        date:   req.body.date,
        value_number: req.body.value_number1,
        value_string: null,
        value_enum: null
    });

    const dav2 = MarkValue.create({
        user_id:    req.body.userId,
        kind_of_mark_id:    2,
        date:   req.body.date,
        value_number: req.body.value_number2,
        value_string: null,
        value_enum: null
    });


    Promise.all([dav1, dav2])
    .then(([result1, result2]) => {
        globalFunctions.sendResult(res, {result1, result2});
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })



};


exports.update = (req, res) => {
    MarkValue.update({
            user_id:req.body.userId,
            kind_of_mark_id:req.body.kind_of_mark_id,
            date: req.body.date,
            value_number: req.body.value_number,
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