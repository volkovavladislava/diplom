module.exports = (sequelize, Sequelize) => {
    var File = sequelize.define(
        'file', // определяем имя таблицы
        {
            id: {
                type: Sequelize.INTEGER(10), // тип данных INTEGER
                autoIncrement: true, // тип данных INTEGER
                primaryKey: true, // поле является первичным ключом
                allowNull: false // Настройка allowNull со значением false запрещает запись в поле значений NULL
            },
            user_id: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            link: {
                type: Sequelize.STRING(255), // тип данных String (в MySQL Varchar)
                allowNull: true
            },
            mime_type: {
                type: Sequelize.STRING(255),
                allowNull: true
            },
            date: {
                type: Sequelize.DATE,
                allowNull: false
            },
            name: {
                type: Sequelize.STRING(150),
                allowNull: false
            },
            comment: {
                type: Sequelize.STRING(255),
                allowNull: true
            }
        });

    File.associate = (models) => {
        
        File.belongsTo(models.user, {
            foreignKey: 'user_id'
        });
    };
    return File;
};