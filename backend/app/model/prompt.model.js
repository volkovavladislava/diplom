const { DataTypes } = require('sequelize')
module.exports = (sequelize, Sequelize) => {
    var Prompt = sequelize.define(
        'prompt', // определяем имя таблицы
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
            name: {
                type: Sequelize.STRING(150), // тип данных String (в MySQL Varchar)
                allowNull: false
            },
            description: {
                type:  Sequelize.STRING(255),
                allowNull: true
            },
            date: {
                // type: Sequelize.DATE,
                type:DataTypes.DATE,
                allowNull: false
            },
            calendar_id: {
                type: Sequelize.INTEGER(11),
                allowNull: true
            }
            
        });

        Prompt.associate = (models) => {
        
            Prompt.belongsTo(models.user, {
            foreignKey: 'user_id'
        });
    };
    return Prompt;
};