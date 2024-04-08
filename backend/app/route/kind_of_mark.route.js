module.exports = (app) => {

    const kind_of_mark = require('../controller/kind_of_mark.controller');
    
    // Получение всех категорий
    app.get('/api/listKindOfMark', kind_of_mark.findAll);

};