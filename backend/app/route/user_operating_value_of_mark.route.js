module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const user_operating_value_of_mark = require('../controller/user_operating_value_of_mark.controller');
    

    app.get('/api/listUserOperatingValueOfMark', [authJwt.verifyToken], user_operating_value_of_mark.findAll);

    app.get('/api/userOpertingValue/:userId', [authJwt.verifyToken], user_operating_value_of_mark.findAllForUser);

    app.put('/api/updateUserOperatingValue/:userId', [authJwt.verifyToken], user_operating_value_of_mark.update);

};