module.exports = (app) => {
    var { authJwt } = require("../middleware");
    const mark_value = require('../controller/mark_value.controller');
    

    app.get('/api/listMarkValue', [authJwt.verifyToken], mark_value.findAll);

    app.put('/api/addMark/:kind_of_mark_id', [authJwt.verifyToken], mark_value.create);

    app.put('/api/addMarkD', [authJwt.verifyToken], mark_value.createD);


    app.get('/api/marksForUser/userId=:userId/kindOfMarkId=:kindOfMarkId', [authJwt.verifyToken], mark_value.MarksForUser);


    app.get('/api/marksForUserByDate/userId=:userId/kindOfMarkId=:kindOfMarkId/date1=:date1/date2=:date2', [authJwt.verifyToken], mark_value.MarksForUserByDate);

    app.get('/api/markById/:markId', [authJwt.verifyToken], mark_value.findById);

    app.post('/api/deleteMark/:markId', [authJwt.verifyToken], mark_value.delete);

    app.post('/api/deleteMarkDavlenie', [authJwt.verifyToken], mark_value.deleteDavlenie);

    app.put('/api/updateMark/:markId', [authJwt.verifyToken], mark_value.update);

    app.put('/api/updateMarkDavlenie', [authJwt.verifyToken], mark_value.updateDavlenie);


    app.get('/api/marksForUserAverage/userId=:userId/kindOfMarkId=:kindOfMarkId', [authJwt.verifyToken], mark_value.MarksWithAverage);

    app.get('/api/marksForUserAverageByDate/userId=:userId/kindOfMarkId=:kindOfMarkId/date1=:date1/date2=:date2', [authJwt.verifyToken], mark_value.MarksWithAverageByDate);

    app.get('/api/marksForUserAverageByDateWithParam/userId=:userId/kindOfMarkId=:kindOfMarkId/date1=:date1/date2=:date2/myparam=:myparam', [authJwt.verifyToken], mark_value.MarksWithAverageByDateWithParametrs);



    app.get('/api/getAdvice/userId=:userId/kindOfMarkId=:kindOfMarkId/date1=:date1/date2=:date2', [authJwt.verifyToken], mark_value.getAdvice);

};