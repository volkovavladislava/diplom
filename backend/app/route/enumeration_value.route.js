module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const enumeration_value = require('../controller/enumeration_value.controller');
    

    app.get('/api/listEnumerationValue', [authJwt.verifyToken], enumeration_value.findAll);

    app.get('/api/kindOfMarkValues/:kindOfMarkId', [authJwt.verifyToken], enumeration_value.findByKindOfMarkId);

};