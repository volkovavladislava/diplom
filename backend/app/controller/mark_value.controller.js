var db = require('../config/db.config.js');
var MarkValue = db.mark_value;
var EnumerationValue = db.enumeration_value;
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


exports.updateDavlenie = (req, res) => {

    console.log(req.body)
    const dav1 = MarkValue.update({
            user_id:    req.body.user_id,
            kind_of_mark_id:    req.body.kind_of_mark_id1,
            date:   req.body.date,
            value_number:   req.body.value_number1,
            value_string: null,
            value_enum: null
        },
        {
            where: {
                id: req.body.id1
            }
        }
    );

    const dav2 = MarkValue.update({
            user_id:req.body.user_id,
            kind_of_mark_id:req.body.kind_of_mark_id2,
            date: req.body.date,
            value_number: req.body.value_number2,
            value_string: null,
            value_enum: null
        },
        {
            where: {
                id: req.body.id2
            }
        }
    );


    Promise.all([dav1, dav2])
    .then(([result1, result2]) => {
        globalFunctions.sendResult(res, {result1, result2});
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};


exports.MarksForUser= (req, res) => {
    // MarkValue.findAll({
    //     where: {
    //         user_id: req.params.userId,
    //         kind_of_mark_id: req.params.kindOfMarkId
    //     }
    //     ,
    //     include: [{
    //         model: EnumerationValue, // Модель, к которой вы хотите присоединиться
    //         attributes: ['value'],// Перечислите столбцы, которые вы хотите выбрать из таблицы N
    //         as: 'enumeration_value'
    //     }],
    //     raw: true
    // })


    db.sequelize.query('SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`, `enumeration_value`.`value` AS `value` FROM `mark_value` AS `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId ;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId}, type: db.sequelize.QueryTypes.SELECT })
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
    // MarkValue.findAll({
    //     where: {
    //         user_id: req.params.userId,
    //         kind_of_mark_id: req.params.kindOfMarkId,
    //         date: {
    //             [Op.gte]: req.params.date1,
    //             [Op.lte]: req.params.date2
    //         }
    //     }
    // })

    db.sequelize.query('SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`, `enumeration_value`.`value` AS `value` FROM `mark_value` AS `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId, date1: req.params.date1, date2: req.params.date2 }
        , type: db.sequelize.QueryTypes.SELECT })
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


exports.deleteDavlenie = (req, res) => {
    // MarkValue.destroy({
    //     where: {
    //         id: req.params.markId
    //     }
    // }).then(() => {
    //     globalFunctions.sendResult(res, 'Запись удалена');
    // }).catch(err => {
    //     globalFunctions.sendError(res, err);
    // });

    const dav1 = MarkValue.destroy({
        where: {
            id: req.body.id1
        }
    });

    const dav2 = MarkValue.destroy({
        where: {
            id: req.body.id2
        }
    });


    Promise.all([dav1, dav2])
    .then(([result1, result2]) => {
        globalFunctions.sendResult(res, {result1, result2});
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
};