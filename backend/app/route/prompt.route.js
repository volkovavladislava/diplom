module.exports = (app) => {

    const prompt = require('../controller/prompt.controller');
    

    app.get('/api/listPrompt', prompt.findAll);

    // Обновление данных пользователя по id
    app.put('/api/addPrompt/:userId', prompt.create);

};