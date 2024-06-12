module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const enum_kind_of_mark = require('../controller/enum_kind_of_mark.controller');
    
    
    app.get('/api/listEnumKindOfMark', [authJwt.verifyToken], enum_kind_of_mark.findAll);

};