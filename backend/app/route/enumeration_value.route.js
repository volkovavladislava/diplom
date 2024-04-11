module.exports = (app) => {

    const enumeration_value = require('../controller/enumeration_value.controller');
    

    app.get('/api/listEnumerationValue', enumeration_value.findAll);

};