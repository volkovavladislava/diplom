module.exports = (app) => {

    const user = require('../controller/user.controller');
    

    app.get('/api/listUser', user.findAll);

    // Обновление данных пользователя по id
    app.put('/api/updateUser/:userId', user.update);

    // Получение пользователя по id
    app.get('/api/user/:id', user.findById);

    app.get('/api/userGuest/:id', user.generateGuest);

    app.post('/api/loginGuest', user.loginGuest);

};