module.exports = (app) => {

    const user_operating_value_of_mark = require('../controller/user_operating_value_of_mark.controller');
    

    app.get('/api/listUserOperatingValueOfMark', user_operating_value_of_mark.findAll);

};