module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const prompt = require('../controller/prompt.controller');
    

    app.get('/api/listPrompt', [authJwt.verifyToken], prompt.findAll);

    // Обновление данных пользователя по id
    app.put('/api/addPrompt/:userId', [authJwt.verifyToken], prompt.create);


    app.get('/api/promptByUser/:userId', [authJwt.verifyToken], prompt.findByUserId);

    app.get('/api/promptById/:promptId', [authJwt.verifyToken], prompt.findById);

    app.put('/api/updatePrompt/:promptId', [authJwt.verifyToken], prompt.update);

    app.post('/api/deletePrompt/:promptId', [authJwt.verifyToken], prompt.delete);

};