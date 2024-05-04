module.exports = (app) => {

    const favorite_mark = require('../controller/favorite_mark.controller');
    

    app.get('/api/listFavoriteMark', favorite_mark.findAll);

    app.put('/api/addFavoriteKindOfMark', favorite_mark.addFavoriteKindOfMark);

    app.post('/api/deleteFavoriteKindOfMark', favorite_mark.delete);

};