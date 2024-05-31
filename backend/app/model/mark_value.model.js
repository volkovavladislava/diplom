module.exports = (sequelize, Sequelize) => {
    var MarkValue = sequelize.define(
        'mark_value', // определяем имя таблицы
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
            kind_of_mark_id: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            date: {
                type: Sequelize.DATE,
                allowNull: false
            },
            situation: {
                type: Sequelize.INTEGER(11),
                allowNull: true
            },
            value_number: {
                type: Sequelize.DOUBLE,
                allowNull: true
            },
            value_string: {
                type: Sequelize.STRING(150),
                allowNull: true
            },
            value_enum: {
                type: Sequelize.INTEGER(10),
                allowNull: true
            }
            
        });

        MarkValue.associate = (models) => {
        
        MarkValue.belongsTo(models.user, {
            foreignKey: 'user_id'
        });

        MarkValue.belongsTo(models.kind_of_mark, {
            foreignKey: 'kind_of_mark_id'
        });

        MarkValue.belongsTo(models.enumeration_value, {
            foreignKey: 'value_enum'
        });
    };
    return MarkValue;
};