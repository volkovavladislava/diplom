module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const file = require('../controller/file.controller');
    

    app.get('/api/listFile', [authJwt.verifyToken], file.findAll);

    app.get('/api/fileByUser/:userId', [authJwt.verifyToken], file.findByUserId);

    app.post('/api/addFile/:userId', [authJwt.verifyToken],file.upload, file.uploadFile);

    app.post('/api/updateFile/:fileId', [authJwt.verifyToken],file.upload, file.updateFile);

    app.get('/api/fileById/:fileId', [authJwt.verifyToken], file.findById);

    app.post('/api/deleteFile/:fileId', [authJwt.verifyToken], file.delete);


    app.post('/api/addFileC/:userId', [authJwt.verifyToken], file.createC);

    app.post('/api/updateFileC/:fileId', [authJwt.verifyToken], file.updateFileC);

    app.post('/api/deleteFileC/:fileId', [authJwt.verifyToken], file.deleteC);

    app.get('/api/fileC/:id', [authJwt.verifyToken], file.findByIdC);

    app.post('/api/updateWithoutFileC/:fileId', [authJwt.verifyToken], file.updateWithoutFileC);
};