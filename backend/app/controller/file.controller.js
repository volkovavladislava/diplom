var db = require('../config/db.config.js');
var File = db.file;
var globalFunctions = require('../config/global.functions.js');

const multer = require('multer');
const path = require('path');

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
        }
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
        File.create({
            user_id: req.params.userId,
            link: filePath,
            mime_type: req.body.mime_type,
            date: req.body.date,
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
        File.update({
            link: filePath,
            mime_type: req.body.mime_type,
            date: req.body.date,
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
