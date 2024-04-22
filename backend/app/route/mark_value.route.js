module.exports = (app) => {

    const mark_value = require('../controller/mark_value.controller');
    

    app.get('/api/listMarkValue', mark_value.findAll);

    app.put('/api/addMark/:kind_of_mark_id', mark_value.create);

    app.get('/api/marksForUser/userId=:userId/kindOfMarkId=:kindOfMarkId', mark_value.MarksForUser);

    app.get('/api/marksForUserByDate/userId=:userId/kindOfMarkId=:kindOfMarkId/date1=:date1/date2=:date2', mark_value.MarksForUserByDate);
};