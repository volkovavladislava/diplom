var db = require('../config/db.config.js');
var FavoriteMark = db.favorite_mark;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    FavoriteMark.findAll()
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};


exports.addFavoriteKindOfMark = (req, res) => {
    FavoriteMark.create({
        user_id:req.body.user_id,
        kind_of_mark_id:req.body.kind_of_mark_id
    })
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            // возврат найденной ошибки
            globalFunctions.sendError(res, err);
        })
};


exports.delete = (req, res) => {
    FavoriteMark.destroy({
        where: {
            user_id:req.body.user_id,
            kind_of_mark_id:req.body.kind_of_mark_id
        }
    }).then(() => {
        globalFunctions.sendResult(res, 'Запись удалена');
    }).catch(err => {
        globalFunctions.sendError(res, err);
    });
};