module.exports = (app) => {

    const enum_kind_of_mark = require('../controller/enum_kind_of_mark.controller');
    
    
    app.get('/api/listEnumKindOfMark', enum_kind_of_mark.findAll);

};