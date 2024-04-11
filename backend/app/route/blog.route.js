module.exports = (app) => {

    const blog = require('../controller/blog.controller');
    

    app.get('/api/listBlog', blog.findAll);

};