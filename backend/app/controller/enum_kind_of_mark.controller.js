var db = require('../config/db.config.js');
var EnumKindOfMark = db.enum_kind_of_mark;
var globalFunctions = require('../config/global.functions.js');

// Получение всех показателей
exports.findAll = (req, res) => {
    EnumKindOfMark.findAll()
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