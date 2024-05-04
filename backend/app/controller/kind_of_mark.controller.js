var db = require('../config/db.config.js');
var KindOfMark = db.kind_of_mark;
var globalFunctions = require('../config/global.functions.js');
const Sequelize = require('sequelize');

// exports.findAll = (req, res) => {
//     KindOfMark.findAll({
//         where: {
//             user_id: null
//         }
//     })
//         .then(objects => {
//             globalFunctions.sendResult(res, objects);
            
//         })
//         .catch(err => {
//             // возврат найденной ошибки
//             globalFunctions.sendError(res, err);
//         })
// };

exports.findAll = (req, res) => {
    db.sequelize.query('SELECT `kind_of_mark`.*, (CASE WHEN (SELECT COUNT(*) FROM `favorite_mark` WHERE `favorite_mark`.kind_of_mark_id = `kind_of_mark`.id) > 0 THEN 1 ELSE 0 END) AS has_reference FROM `kind_of_mark`  WHERE `kind_of_mark`.user_id IS NULL ORDER BY name ASC;', { type: db.sequelize.QueryTypes.SELECT })
    .then(objects => {
            objects.sort((a, b) => {
                if (a.has_reference > b.has_reference) {
                    return -1;
                }
                if (a.has_reference < b.has_reference) {
                    return 1;
                }
                return 0;
            });
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};


exports.findAllHandMade = (req, res) => {
    // KindOfMark.findAll({
    //     where: {
    //         user_id: req.params.userId
    //     }
    // })
    //     .then(objects => {
    //         globalFunctions.sendResult(res, objects);
            
    //     })

        db.sequelize.query('SELECT `kind_of_mark`.*, (CASE WHEN (SELECT COUNT(*) FROM `favorite_mark` WHERE `favorite_mark`.kind_of_mark_id = `kind_of_mark`.id) > 0 THEN 1 ELSE 0 END) AS has_reference FROM `kind_of_mark`  WHERE `kind_of_mark`.user_id = :userId ORDER BY name ASC;', 
        { replacements: { userId: req.params.userId }, type: db.sequelize.QueryTypes.SELECT })
        .then(objects => {
                objects.sort((a, b) => {
                    if (a.has_reference > b.has_reference) {
                        return -1;
                    }
                    if (a.has_reference < b.has_reference) {
                        return 1;
                    }
                    return 0;
                });
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