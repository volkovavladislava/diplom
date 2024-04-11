module.exports = (app) => {

    const favorite_mark = require('../controller/favorite_mark.controller');
    

    app.get('/api/listFavoriteMark', favorite_mark.findAll);

};