module.exports = (app) => {

    const prompt = require('../controller/prompt.controller');
    

    app.get('/api/listPrompt', prompt.findAll);

    // Обновление данных пользователя по id
    app.put('/api/addPrompt/:userId', prompt.create);


    app.get('/api/promptByUser/:userId', prompt.findByUserId);

    app.get('/api/promptById/:promptId', prompt.findById);

    app.put('/api/updatePrompt/:promptId', prompt.update);

    app.post('/api/deletePrompt/:promptId', prompt.delete);

};