var db = require('../config/db.config.js');
var File = db.file;
var globalFunctions = require('../config/global.functions.js');

const multer = require('multer');
const path = require('path');

var multiparty = require('multiparty');
var fs = require('fs');
var uuid = require('uuid');

const moment = require('moment-timezone');

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, 'files');
    },
    filename: (req, file, cb) => {
    //   cb(null, Date.now() + '-' + file.originalname);
    //   cb(null, Date.now() + '-' + path.extname(file.originalname));

      const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
        const ext = path.extname(file.originalname);
        cb(null, file.fieldname + '-' + uniqueSuffix + ext);
    },
  });

  exports.upload = multer({ 
    storage: storage,
    limits: {fileSize: '1000000'},
    fileFilter: (req, file, cb) =>{
        const fileTypes = /jpeg|jpg|png|gif/
        const mimeType = fileTypes.test(file.mimetype)
        // const extname = fileTypes.test(path.extname(file.originalname))

        // if(mimeType && extname){
            return cb(null, true)
        // }
        // cb('give proper files formate to upload')
    }
    }
).single('file')


exports.findAll = (req, res) => {
    File.findAll()
        .then(objects => {
            globalFunctions.sendResult(res, objects);
            
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};

exports.findByUserId = (req, res) => {
    File.findAll({
        where: {
            user_id: req.params.userId
        },
        order: [['id', 'DESC']]
    }).then(object => {
            
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


// // exports.uploadFile = (req, res) => {
//     exports.uploadFile = async (req, res) => {
//         // if (err) {
//         //     console.error('Ошибка загрузки файла:', err);
//         //     globalFunctions.sendError(res, "Произошла ошибка при загрузке файла");
//         // } else {
//             try {
//                 const filePath = req.file.path;
//                 const newRecord = await File.create({
//                     user_id: req.params.userId,
//                     link: filePath,
//                     mime_type: req.file.mimetype,
//                     date: req.body.date,
//                     name: req.body.name,
//                     comment: req.body.comment
//                 });
//                 // globalFunctions.sendResult(res, "Файл успешно загружен и сохранен в базе данных");
//                 return res.send(`File has been uploaded.`);
//             } catch (error) {
//                 console.error("Ошибка сохранения файла в базе данных:", error);
//                 // globalFunctions.sendError(res, "Произошла ошибка при сохранении файла в базе данных");
//                 return res.send(`Error when trying upload images: ${error}`);
//             }
//         }
//     // }
// // };


exports.uploadFile = (req, res) => {
    try {
        const filePath = req.file.filename;

        const inputTime = req.body.date;
        const localTimezone = 'Asia/Singapore'; 
        const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
        const utcMoment = localMoment.clone().tz('UTC');
        const utcTime = utcMoment.toISOString();

        File.create({
            user_id: req.params.userId,
            link: filePath,
            mime_type: req.body.mime_type,
            date: utcTime,
            name: req.body.name,
            comment: req.body.comment
        });
        // globalFunctions.sendResult(res, "Файл успешно загружен и сохранен в базе данных");
        return res.send(`File has been uploaded.`);
    } catch (error) {
        console.error("Ошибка сохранения файла в базе данных:", error);
        // globalFunctions.sendError(res, "Произошла ошибка при сохранении файла в базе данных");
        return res.send(`Error when trying upload images: ${error}`);
    }
}

exports.updateFile = (req, res) => {
    try {
        const filePath = req.file.filename;

        const inputTime = req.body.date;
        const localTimezone = 'Asia/Singapore'; 
        const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
        const utcMoment = localMoment.clone().tz('UTC');
        const utcTime = utcMoment.toISOString();

        File.update({
            link: filePath,
            mime_type: req.body.mime_type,
            date: utcTime,
            name: req.body.name,
            comment: req.body.comment
        },
        {
            where: {
                id: req.params.fileId
            }
        });
        // globalFunctions.sendResult(res, "Файл успешно загружен и сохранен в базе данных");
        return res.send(`File has been uploaded.`);
    } catch (error) {
        console.error("Ошибка сохранения файла в базе данных:", error);
        // globalFunctions.sendError(res, "Произошла ошибка при сохранении файла в базе данных");
        return res.send(`Error when trying upload images: ${error}`);
    }
}



exports.findById = (req, res) => {
    File.findByPk(req.params.fileId)
        .then(object => {
            globalFunctions.sendResult(res, object);
        })
        .catch(err => {
            globalFunctions.sendError(res, err);
        })
};


exports.delete = (req, res) => {
    File.destroy({
        where: {
            id: req.params.fileId
        }
    }).then(() => {
        globalFunctions.sendResult(res, 'Запись удалена');
    }).catch(err => {
        globalFunctions.sendError(res, err);
    });
};




exports.createC = async (req, res) => {
    // создаём объект для чтения данных, переданных со стороны клиента (передавали объект FormData)
    var form = new multiparty.Form();

    // читаем данные
    await form.parse(req, async (err, fields, files) => {
        if (!err) {
            var mimeType = files.file[0].headers['content-type']; // тип файла указывается так: image/png
            expansion = mimeType.split('/')[1]; // из "image/png" нужно извлечь только расширение

            var newName = uuid.v4() + "." + expansion; // вызываем функцию v4() для того, чтобы уникальный идентификатор был сгенерирован случайным образом
            var link = 'file-' + newName;
            var a = './files/' + link;
            fs.copyFile(files.file[0].path, a, (err) => {
                if (err) {
                    throw err;
                }
            });

            const inputTime = fields.date[0];
            const localTimezone = 'Asia/Singapore'; 
            const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
            const utcMoment = localMoment.clone().tz('UTC');
            const utcTime = utcMoment.toISOString();


            var name = fields.name[0];
            var date = utcTime;
            var comment = fields.comment[0];
            
            File.create({
                user_id: req.params.userId,
                link: link,
                mime_type: mimeType,
                date: date,
                name: name,
                comment: comment
            }).then(object => {
                globalFunctions.sendResult(res, object);
            }).catch(err => {
                globalFunctions.sendError(res, err);
            })
        }
        else{
            globalFunctions.sendError(res, err);
        }
    });
};


exports.updateFileC = async (req, res) => {
    // создаём объект для чтения данных, переданных со стороны клиента (передавали объект FormData)
    var form = new multiparty.Form();

    // читаем данные
    await form.parse(req, async (err, fields, files) => {
        if (!err) {
            var mimeType = files.file[0].headers['content-type']; // тип файла указывается так: image/png
            expansion = mimeType.split('/')[1]; // из "image/png" нужно извлечь только расширение

            var newName = uuid.v4() + "." + expansion; // вызываем функцию v4() для того, чтобы уникальный идентификатор был сгенерирован случайным образом
            var link = 'file-' + newName;
            var a = './files/' + link;
            console.log(a)

            fs.copyFile(files.file[0].path, a, (err) => {
                if (err) {
                    throw err;
                }
            });

            const inputTime = fields.date[0];
            const localTimezone = 'Asia/Singapore'; 
            const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
            const utcMoment = localMoment.clone().tz('UTC');
            const utcTime = utcMoment.toISOString();


            var name = fields.name[0];
            var date = utcTime;
            var comment = fields.comment[0];
            
            File.update({
                user_id: req.params.userId,
                link: link,
                mime_type: mimeType,
                date: date,
                name: name,
                comment: comment
            },
            {
                where: {
                    id: req.params.fileId
                }
            }).then(object => {
                globalFunctions.sendResult(res, object);
            }).catch(err => {
                globalFunctions.sendError(res, err);
            })
        }
        else{
            globalFunctions.sendError(res, err);
        }
    });
};



exports.deleteC = (req, res) => {
    File.findByPk(req.params.fileId)
    .then(async (object) => {
        // удаляем файл
        await fs.unlinkSync( './files/' + object.dataValues.link);
        await File.destroy({
            where: {
                id: req.params.fileId
            }
        }).then(() => {
            globalFunctions.sendResult(res, 'Запись удалена');
        }).catch(err => {
            globalFunctions.sendError(res, err);
        });
    }).catch(err => {
        globalFunctions.sendError(res, err);
    });
};



exports.findByIdC = (req, res) => {
    File.findByPk(req.params.id)
    .then(async objects => {
            var object = objects.dataValues;
            // читаем содержимое файла для отправки на сторону клиентского приложения
            var contents = fs.readFileSync('./files/' + object.link, {encoding: 'base64'});
            console.log("contents " + contents)
            var data = {
                id: object.id,
                name: object.name,
                file: contents,
                mime_type: object.mime_type,
                user_id: object.user_id,
                date: object.date,
                comment: object.comment
            };
            globalFunctions.sendResult(res, data);
    })
    .catch(err => {
        globalFunctions.sendError(res, err);
    })
};



exports.updateWithoutFileC = async (req, res) => {

    const inputTime = req.body.date;
    const localTimezone = 'Asia/Singapore'; 
    const localMoment = moment.tz(inputTime, 'YYYY-MM-DD HH:mm', localTimezone);
    const utcMoment = localMoment.clone().tz('UTC');
    const utcTime = utcMoment.toISOString();

    File.update({
        user_id: req.body.userId,
        date: utcTime,
        name: req.body.name,
        comment: req.body.comment
    },
    {
        where: {
            id: req.params.fileId
        }
    }).then(object => {
        globalFunctions.sendResult(res, object);
    }).catch(err => {
        globalFunctions.sendError(res, err);
    })
        
};