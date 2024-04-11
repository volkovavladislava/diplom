module.exports = (app) => {

    const base_operating_value_of_mark = require('../controller/base_operating_value_of_mark.controller');
    

    app.get('/api/listBaseOperatingValueOfMark', base_operating_value_of_mark.findAll);

};