module.exports = (app) => {

    const kind_of_mark = require('../controller/kind_of_mark.controller');
    

    app.get('/api/listKindOfMarkOfSystem', kind_of_mark.findAll);

    app.get('/api/listKindOfMarkOfHandMade/:userId', kind_of_mark.findAllHandMade);


    app.put('/api/addHandMadeKindOfMark/:userId', kind_of_mark.create);

};