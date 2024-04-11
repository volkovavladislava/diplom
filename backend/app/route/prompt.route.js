module.exports = (app) => {

    const prompt = require('../controller/prompt.controller');
    

    app.get('/api/listPrompt', prompt.findAll);

};