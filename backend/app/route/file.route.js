module.exports = (app) => {

    const file = require('../controller/file.controller');
    

    app.get('/api/listFile', file.findAll);

    app.get('/api/fileByUser/:userId', file.findByUserId);

    app.post('/api/addFile/:userId',file.upload, file.uploadFile);

    app.post('/api/updateFile/:fileId',file.upload, file.updateFile);

    app.get('/api/fileById/:fileId', file.findById);

    app.post('/api/deleteFile/:fileId', file.delete);


    app.post('/api/addFileC/:userId', file.createC);

    app.post('/api/updateFileC/:fileId', file.updateFileC);

    app.post('/api/deleteFileC/:fileId', file.deleteC);

    app.get('/api/fileC/:id', file.findByIdC);

    app.post('/api/updateWithoutFileC/:fileId', file.updateWithoutFileC);
};