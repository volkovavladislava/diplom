var db = require('../config/db.config.js');
var MarkValue = db.mark_value;
var EnumerationValue = db.enumeration_value;
var globalFunctions = require('../config/global.functions.js');
const { Op } = require('sequelize');

const moment = require('moment-timezone');

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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    MarkValue.create({
        user_id:req.body.userId,
        kind_of_mark_id:req.body.kind_of_mark_id,
        date: utcTime,
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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    const dav1 = MarkValue.create({
        user_id:    req.body.userId,
        kind_of_mark_id:    1,
        date:   utcTime,
        situation: req.body.situation,
        value_number: req.body.value_number1,
        value_string: null,
        value_enum: null
    });

    const dav2 = MarkValue.create({
        user_id:    req.body.userId,
        kind_of_mark_id:    2,
        date:   utcTime,
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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();


    MarkValue.update({
            user_id:req.body.userId,
            kind_of_mark_id:req.body.kind_of_mark_id,
            date: utcTime,
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

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    console.log(req.body)
    const dav1 = MarkValue.update({
            user_id:    req.body.user_id,
            kind_of_mark_id:    req.body.kind_of_mark_id1,
            date:   utcTime,
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
            date: utcTime,
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

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId  ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN 1 PRECEDING AND 1 following), 1) AS moving_average FROM selected_data;', 
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

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1 ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN 1 PRECEDING AND 1 following),1) AS moving_average FROM selected_data;', 
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

    const p = parseInt(req.params.myparam, 10); 

    db.sequelize.query('WITH selected_data AS (SELECT `mark_value`.`id`, `mark_value`.`user_id`, `mark_value`.`kind_of_mark_id`, `mark_value`.`date`, `mark_value`.`situation`, `mark_value`.`value_number`, `mark_value`.`value_string`, `mark_value`.`value_enum`,`enumeration_value`.`value` AS `value` FROM `mark_value` LEFT OUTER JOIN `enumeration_value` AS `enumeration_value` ON `mark_value`.`value_enum` = `enumeration_value`.`id` WHERE `mark_value`.`user_id` = :userId AND `mark_value`.`kind_of_mark_id` = :kindOfMarkId AND `mark_value`.`date` <= :date2 AND `mark_value`.`date` >= :date1 ORDER BY `date`) SELECT *, ROUND(AVG(`value_number`) OVER (PARTITION BY `user_id` ORDER BY `date` ROWS BETWEEN :myparam PRECEDING AND :myparam following), 1) AS moving_average FROM selected_data;', 
        { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId, date1: req.params.date1, date2: req.params.date2, myparam: p }
        , type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
            
            globalFunctions.sendResult(res, objects);
        })
        .catch(err => {
            console.log(err)
            globalFunctions.sendError(res, err);
        })
};


exports.getAdvice= (req, res) => {

    var itog = {
        comment : ""
    }
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
                valAverage = Math.round(valAverage/objects.length * 10) / 10;
                console.error(valAverage);
               
                let age = calculateAge(user[0].date_birth)

                let norma = null

                db.sequelize.query('SELECT * FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId AND `user_operating_value_of_mark`.`kind_of_mark_id` = :kindOfMarkId AND`user_operating_value_of_mark`.`date` = (SELECT MAX(`user_operating_value_of_mark`.`date`) FROM `user_operating_value_of_mark` WHERE `user_operating_value_of_mark`.`user_id` = :userId) ORDER BY `id` DESC LIMIT 1;',
                { replacements: { userId: req.params.userId,  kindOfMarkId: req.params.kindOfMarkId}, type: db.sequelize.QueryTypes.SELECT })
                .then(objectsOperating => {
                    
                    

                        db.sequelize.query('SELECT * FROM `base_operating_value_of_mark` WHERE  `kind_of_mark_id` = :kindOfMarkId AND `gender` = :gender AND  :age >= `min_age` AND :age <= `max_age` ;',
                        { replacements: { gender: user[0].gender,  kindOfMarkId: req.params.kindOfMarkId, age: age}, type: db.sequelize.QueryTypes.SELECT })
                        .then(objectsNorma => {
                            norma = objectsNorma
                            // console.error(norma.length);
                            // console.error(norma[0]);
                            
                            let kritichniz = Math.round(norma[0].max_value/2 * 10) / 10
                            let kritichverh = norma[2].min_value + Math.round((norma[2].max_value - norma[2].min_value)/2 * 10) / 10

                            console.error(kritichniz + " " + norma[0].comment);
                            console.error(kritichverh+ " " + norma[2].comment);

                            let countkritich = 0
                            for(let  i = 0; i < objects.length; i++){
                                if(objects[i].value_number <= kritichniz || objects[i].value_number >= kritichverh){
                                    countkritich +=1
                                    console.error(objects[i].value_number);
                                }
                            }

                            if(countkritich >5){
                                itog.comment = "Есть несколько критически низких или высоких значений. Советуем обратиться к врачу."
                                globalFunctions.sendResult(res, itog);
                                return;
                            }

                            if(objectsOperating.length != 0){

                                let razbeg = norma[1].max_value - norma[1].min_value
                                let niz = objectsOperating[0].value - razbeg/2
                                let verh = objectsOperating[0].value + razbeg/2
                                
                                
                                if(valAverage >= niz && valAverage <=verh){
                                    // console.log("good ")
                                    itog.comment = norma[1].comment
                                    // console.log("itogcomment " + itog.comment)
                                }
                                if(valAverage < niz){
                                    // console.log("too small ")
                                    itog.comment = norma[0].comment
                                }
                                if(valAverage > verh){
                                    // console.log("too big ")
                                    itog.comment = norma[2].comment
                                }
                                
                                globalFunctions.sendResult(res, itog);

                            }
                            else{
                                db.sequelize.query('SELECT * FROM `base_operating_value_of_mark` WHERE  `kind_of_mark_id` = :kindOfMarkId AND `gender` = :gender AND  :age >= `min_age` AND :age <= `max_age` AND :value >= `min_value` AND :value <= `max_value` ;',
                                { replacements: { gender: user[0].gender,  kindOfMarkId: req.params.kindOfMarkId, age: age, value: valAverage}, type: db.sequelize.QueryTypes.SELECT })
                                .then(objectsAdvice => {
                                    
                                    itog.comment = objectsAdvice[0].comment
                                
                                    globalFunctions.sendResult(res, itog);
                                })
                                .catch(err => {
                                    console.error(err);
                                    globalFunctions.sendError(res, err);
                                })
                                
                            }
                        })
                        .catch(err => {
                            console.error(err);
                            globalFunctions.sendError(res, err);
                        })

                        
                    




                })
                .catch(err => {
                    console.error(err);
                    globalFunctions.sendError(res, err);
                })






            })
            .catch(err => {
                console.error(err);
                globalFunctions.sendError(res, err);
            });

            

        })
        .catch(err => {
            console.error(err);
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
