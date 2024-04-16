const express = require('express')
const mysql = require('mysql')
const multer = require('multer');

const app = express()

const port = 3000

// Устанавливаем часовой пояс для приложения
process.env.TZ = 'UTC+8';

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

var db = require('./app/config/db.config.js'); // подключение настроек базы данных
db.sequelize.sync({force: false});





const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'files');
  },
  filename: (req, file, cb) => {
    cb(null, Date.now() + '-' + path.extname(file.originalname));
  },
});

exports.upload = multer({ 
  storage: storage,
  limits: {fileSize: '1000000'},
  fileFilter: (req, file, cb) =>{
      const fileTypes = /jpeg|jpg|png|gif/
      const mimeType = fileTypes.test(file.mimetype)
      const extname = fileTypes.test(path.extname(file.originalname))

      if(mimeType && extname){
          return cb(null, true)
      }
      cb('give proper files formate to upload')
  }
  }
).single('file')


// var cors = require('cors');
// var corsOptions = {
//     origin: 'http://localhost:4200', // указываем, откуда будут приходить запросы
//     credentials: true, // разрешаем обрабатывать запросы
//     optionSuccessStatus: 200 // при успешной обработке запроса будет возвращён статус 200
// };
// app.use(cors(corsOptions));

app.use('/files', express.static('files'));


var kind_of_mark = require('./app/route/kind_of_mark.route.js');
kind_of_mark(app);

var enum_kind_of_mark = require('./app/route/enum_kind_of_mark.route.js');
enum_kind_of_mark(app);

var user = require('./app/route/user.route.js');
user(app);

var prompt = require('./app/route/prompt.route.js');
prompt(app);

var file = require('./app/route/file.route.js');
file(app);

var blog = require('./app/route/blog.route.js');
blog(app);

var base_operating_value_of_mark = require('./app/route/base_operating_value_of_mark.route.js');
base_operating_value_of_mark(app);

var user_operating_value_of_mark = require('./app/route/user_operating_value_of_mark.route.js');
user_operating_value_of_mark(app);

var enumeration_value = require('./app/route/enumeration_value.route.js');
enumeration_value(app);

var favorite_mark = require('./app/route/favorite_mark.route.js');
favorite_mark(app);


var mark_value = require('./app/route/mark_value.route.js');
mark_value(app);

app.listen(port, () =>{
    console.log("sdsd")
    // const KindOfMarkController = require('./app/controller/kind_of_mark.controller.js');
    // KindOfMarkController.findAll();
    // const EnumKindOfMarkController = require('./app/controller/enum_kind_of_mark.controller.js');
    // EnumKindOfMarkController.findAll();
})

// app.get('/listKindOfMark' , (req, res) =>{
//     console.log(res)
// })



