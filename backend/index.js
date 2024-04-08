const express = require('express')
const mysql = require('mysql')

const app = express()

const port = 3000

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

var db = require('./app/config/db.config.js'); // подключение настроек базы данных
db.sequelize.sync({force: false});


// var cors = require('cors');
// var corsOptions = {
//     origin: 'http://localhost:4200', // указываем, откуда будут приходить запросы
//     credentials: true, // разрешаем обрабатывать запросы
//     optionSuccessStatus: 200 // при успешной обработке запроса будет возвращён статус 200
// };
// app.use(cors(corsOptions));

var kind_of_mark = require('./app/route/kind_of_mark.route.js');
kind_of_mark(app);



app.listen(port, () =>{
    console.log("sdsd")
    // const KindOfMarkController = require('./app/controller/kind_of_mark.controller.js');
    // KindOfMarkController.findAll();
})

// app.get('/listKindOfMark' , (req, res) =>{
//     console.log(res)
// })
