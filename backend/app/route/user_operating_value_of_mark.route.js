module.exports = (app) => {

    const user_operating_value_of_mark = require('../controller/user_operating_value_of_mark.controller');
    

    app.get('/api/listUserOperatingValueOfMark', user_operating_value_of_mark.findAll);

    app.get('/api/userOpertingValue/:userId', user_operating_value_of_mark.findAllForUser);

    app.put('/api/updateUserOperatingValue/:userId', user_operating_value_of_mark.update);

};