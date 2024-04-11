module.exports = (app) => {

    const user = require('../controller/user.controller');
    

    app.get('/api/listUser', user.findAll);

};