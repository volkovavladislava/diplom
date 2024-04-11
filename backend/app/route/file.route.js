module.exports = (app) => {

    const file = require('../controller/file.controller');
    

    app.get('/api/listFile', file.findAll);

};