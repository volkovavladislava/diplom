module.exports = (app) => {

    const mark_value = require('../controller/mark_value.controller');
    

    app.get('/api/listMarkValue', mark_value.findAll);

};