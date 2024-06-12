module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const kind_of_mark = require('../controller/kind_of_mark.controller');
    
    

    app.get('/api/listKindOfMarkOfSystem', [authJwt.verifyToken], kind_of_mark.findAll);

    app.get('/api/listKindOfMarkOfHandMade/:userId', [authJwt.verifyToken], kind_of_mark.findAllHandMade);


    app.put('/api/addHandMadeKindOfMark/:userId', [authJwt.verifyToken], kind_of_mark.create);

    app.post('/api/deletePersonalMark', [authJwt.verifyToken], kind_of_mark.deletePersonalMark);

};