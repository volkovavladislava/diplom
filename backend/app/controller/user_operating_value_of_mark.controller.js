var db = require('../config/db.config.js');
var UserOperatingValueOfMark = db.user_operating_value_of_mark;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    UserOperatingValueOfMark.findAll()
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};

exports.findAllForUser = (req, res) => {
    // UserOperatingValueOfMark.findAll({
    //     where: {
    //         user_id: req.params.userId
    // }})
    //     .then(objects => {
    //         globalFunctions.sendResult(res, objects);
            
    //     })
    //     .catch(err => {
    //         // возврат найденной ошибки
    //         globalFunctions.sendError(res, err);
    //     })

        db.sequelize.query('SELECT * FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId AND `user_operating_value_of_mark`.`date` = (SELECT MAX(`user_operating_value_of_mark`.`date`) FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId);',
        { replacements: { userId: req.params.userId}, type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};




// Обновление данных пользователя по id
exports.update = (req, res) => {

    const mas = [];

    if(req.body.value1 != null){mas.push({kind_of_mark_id : 1 , user_id : req.body.user_id, value : req.body.value1, date : req.body.date})}
    if(req.body.value2 != null){mas.push({kind_of_mark_id : 2 , user_id : req.body.user_id, value : req.body.value2, date : req.body.date})}
    if(req.body.value3 != null){mas.push({kind_of_mark_id : 3 , user_id : req.body.user_id, value : req.body.value3, date : req.body.date})}
    if(req.body.value4 != null){mas.push({kind_of_mark_id : 4 , user_id : req.body.user_id, value : req.body.value4, date : req.body.date})}
    if(req.body.value5 != null){mas.push({kind_of_mark_id : 5 , user_id : req.body.user_id, value : req.body.value5, date : req.body.date})}
    UserOperatingValueOfMark.bulkCreate(mas)
    .then(object => {
        // console.log( object)
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};