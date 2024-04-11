var db = require('../config/db.config.js');
var File = db.file;
var globalFunctions = require('../config/global.functions.js');


exports.findAll = (req, res) => {
    File.findAll()
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