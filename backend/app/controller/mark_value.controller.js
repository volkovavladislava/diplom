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
        situation: req.body.situation,
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
        situation: req.body.situation,
        value_number: req.body.value_number1,
        value_string: null,
        value_enum: null
    });

    const dav2 = MarkValue.create({
        user_id:    req.body.userId,
        kind_of_mark_id:    2,
        date:   req.body.date,
        situation: req.body.situation,
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
            situation: req.body.situation,
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
            situation: req.body.situation,
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
            situation: req.body.situation,
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


    db.sequelize.query('SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`, `enumeration_value`.`value` AS `value` FROM `mark_value` AS `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId ;', 
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

    db.sequelize.query('SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`, `enumeration_value`.`value` AS `value` FROM `mark_value` AS `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1;', 
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


// WITH selected_data AS (
//     SELECT 
//         *
//     FROM 
//         `mark_value`
//     WHERE 
//         `user_id` = 1 
//         AND `kind_of_mark_id` = 3
//     ORDER BY 
//         `date`
// )
// SELECT 
//     `user_id`,
//     `kind_of_mark_id`,
//     `date`,
//     `situation`,
//     `value_number`,
//     AVG(`value_number`) OVER (
//         PARTITION BY `user_id` 
//         ORDER BY `date` 
//         ROWS BETWEEN 4 PRECEDING AND CURRENT ROW
//     ) AS moving_average
// FROM 
//     selected_data;

// WITH selected_data AS (SELECT * FROM `mark_value` WHERE `user_id` = 1  AND `kind_of_mark_id` = 3 ORDER BY `date`)
// SELECT 
//     `user_id`,
//     `kind_of_mark_id`,
//     `date`,
//     `situation`,
//     `value_number`,
//     AVG(`value_number`) OVER (
//         PARTITION BY `user_id` 
//         ORDER BY `date` 
//         ROWS BETWEEN 2 PRECEDING AND 2 following
//     ) AS moving_average
// FROM 
//     selected_data;


// WITH selected_data AS (SELECT * FROM `mark_value` WHERE `user_id` = 1  AND `kind_of_mark_id` = 3 ORDER BY `date`)
// SELECT 
//     *,
//     ROUND(AVG(`value_number`) OVER (
//         PARTITION BY `user_id` 
//         ORDER BY `date` 
//         ROWS BETWEEN 2 PRECEDING AND 2 following
//     ), 0) AS moving_average
// FROM 
//     selected_data;


// WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = 1  AND `mark_value`.`kind_of_mark_id` =  3 ORDER BY `mark_value`.`date`)
// SELECT 
//     *,
//     ROUND(AVG(`value_number`) OVER (
//         PARTITION BY`user_id` 
//         ORDER BY `date` 
//         ROWS BETWEEN 2 PRECEDING AND 2 following
//     ), 0) AS moving_average
// FROM 
//     selected_data;

exports.MarksWithAverage= (req, res) => {

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId  ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN 2 PRECEDING AND 2 following), 0) AS moving_average FROM selected_data;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId}
        , type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


exports.MarksWithAverageByDate= (req, res) => {

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1 ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN 2 PRECEDING AND 2 following), 0) AS moving_average FROM selected_data;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId, date1: req.params.date1, date2: req.params.date2 }
        , type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};




exports.MarksWithAverageByDateWithParametrs= (req, res) => {

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1 ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN :param PRECEDING AND :param following), 0) AS moving_average FROM selected_data;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId, date1: req.params.date1, date2: req.params.date2, param: req.params.param }
        , type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


exports.getAdvice= (req, res) => {
    db.sequelize.query('SELECT * FROM `mark_value` AS `mark_value`  WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId, date1: req.params.date1, date2: req.params.date2 }
        , type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            // globalFunctions.sendResult(res, objects);

            
            
            db.sequelize.query('SELECT * FROM `user` WHERE `id` = :userId;',
            { replacements: { userId: req.params.userId}, type: db.sequelize.QueryTypes.SELECT })
            .then(user => {


                let  valAverage = 0;
                for(let  i = 0; i < objects.length; i++){
                    valAverage += objects[i].value_number;
                }
                valAverage = Math.round(valAverage/objects.length);

               
                let age = calculateAge(user[0].date_birth)

                let norma = null

                db.sequelize.query('SELECT * FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId AND `user_operating_value_of_mark`.`kind_of_mark_id` = :kindOfMarkId AND`user_operating_value_of_mark`.`date` = (SELECT MAX(`user_operating_value_of_mark`.`date`) FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId) ORDER BY `id` DESC LIMIT 1;',
                { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId}, type: db.sequelize.QueryTypes.SELECT })
                .then(objectsOperating => {
                    
                    if(objectsOperating.length != 0){
                        console.log("length " + 0)
                        db.sequelize.query('SELECT * FROM `base_operating_value_of_mark` WHERE  `kind_of_mark_id` = :kindOfMarkId AND `gender` = :gender AND  :age >= `min_age` AND :age <= `max_age` ;',
                        { replacements: { gender: user[0].gender,  kindOfMarkId: req.params.kindOfMarkId, age: age}, type: db.sequelize.QueryTypes.SELECT })
                        .then(objectsNorma => {
                            norma = objectsNorma

                            let razbeg = norma[1].max_value - norma[1].min_value
                            let niz = objectsOperating[0].value - razbeg/2
                            let verh = objectsOperating[0].value + razbeg/2
                            

                            if(valAverage >= niz && valAverage <=verh){
                                console.log("good ")
                            }
                            if(valAverage < niz){
                                console.log("too small ")
                            }
                            if(valAverage > verh){
                                console.log("too big ")
                            }
                            // console.log("objectsOperating[0].value "+objectsOperating[0].value)
                            // console.log("niz "+niz)
                            // console.log("verh "+verh)
                            
                        })
                        .catch(err => {
                            globalFunctions.sendError(res, err);
                        })

                        
                    }
                    else{
                        db.sequelize.query('SELECT * FROM `base_operating_value_of_mark` WHERE  `kind_of_mark_id` = :kindOfMarkId AND `gender` = :gender AND  :age >= `min_age` AND :age <= `max_age` AND :value >= `min_value` AND :value <= `max_value` ;',
                        { replacements: { gender: user[0].gender,  kindOfMarkId: req.params.kindOfMarkId, age: age, value: valAverage}, type: db.sequelize.QueryTypes.SELECT })
                        .then(objectsAdvice => {
                            console.log(objectsAdvice)
                            globalFunctions.sendResult(res, objectsAdvice);
                        })
                        .catch(err => {
                            globalFunctions.sendError(res, err);
                        })
                        
                    }




                })
                .catch(err => {
                    globalFunctions.sendError(res, err);
                })






            })
            .catch(err => {
                globalFunctions.sendError(res, err);
            });

            

        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};




function calculateAge(birthDateString) {

    const birthDate = new Date(birthDateString);

    const today = new Date();

    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    return age;
}
