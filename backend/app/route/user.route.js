module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const user = require('../controller/user.controller');
    

    app.get('/api/listUser', [authJwt.verifyToken], user.findAll);

    // Обновление данных пользователя по id
    app.put('/api/updateUser/:userId', [authJwt.verifyToken], user.update);

    // Получение пользователя по id
    app.get('/api/user/:id', [authJwt.verifyToken], user.findById);

    app.get('/api/userGuest/:id', user.generateGuest);

    app.post('/api/loginGuest', user.loginGuest);

    app.post('/api/logoutGuest', user.logoutGuest);

    app.post('/api/checkGuest', user.checkGuest);

};