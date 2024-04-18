module.exports = (app) => {

    const mark_value = require('../controller/mark_value.controller');
    

    app.get('/api/listMarkValue', mark_value.findAll);

    app.put('/api/addMark/:kind_of_mark_id', mark_value.create);

    app.get('/api/marksForUser/userId=:userId/kindOfMarkId=:kindOfMarkId', mark_value.MarksForUser);

};