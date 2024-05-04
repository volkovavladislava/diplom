module.exports = (sequelize, Sequelize) => {
    var UserOperatingValueOfMark = sequelize.define(
        'user_operating_value_of_mark', // определяем имя таблицы
        {
            id: {
                type: Sequelize.INTEGER(10), // тип данных INTEGER
                autoIncrement: true, // тип данных INTEGER
                primaryKey: true, // поле является первичным ключом
                allowNull: false // Настройка allowNull со значением false запрещает запись в поле значений NULL
            },
            kind_of_mark_id: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            user_id: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            value: {
                type: Sequelize.DOUBLE, 
                allowNull: false
            },
            date: {
                type: Sequelize.DATEONLY,
                allowNull: false
            }
        });

    UserOperatingValueOfMark.associate = (models) => {
        
        UserOperatingValueOfMark.belongsTo(models.user, {
            foreignKey: 'user_id'
        });

        UserOperatingValueOfMark.belongsTo(models.kind_of_mark, {
            foreignKey: 'kind_of_mark_id'
        });
    };
    return UserOperatingValueOfMark;
};