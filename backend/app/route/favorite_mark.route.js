module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const favorite_mark = require('../controller/favorite_mark.controller');
    

    app.get('/api/listFavoriteMark', [authJwt.verifyToken], favorite_mark.findAll);

    app.put('/api/addFavoriteKindOfMark',  favorite_mark.addFavoriteKindOfMark);

    app.post('/api/deleteFavoriteKindOfMark',  favorite_mark.delete);

};