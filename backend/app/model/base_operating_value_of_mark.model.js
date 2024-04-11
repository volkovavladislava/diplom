module.exports = (sequelize, Sequelize) => {
    var BaseOperatingValueOfMark = sequelize.define(
        'base_operating_value_of_mark', // определяем имя таблицы
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
            min_value: {
                type: Sequelize.DOUBLE, // тип данных String (в MySQL Varchar)
                allowNull: false
            },
            max_value: {
                type: Sequelize.DOUBLE,
                allowNull: false
            },
            date: {
                type: Sequelize.DATEONLY,
                allowNull: false
            }
        });

        BaseOperatingValueOfMark.associate = (models) => {
        

        BaseOperatingValueOfMark.belongsTo(models.kind_of_mark, {
            foreignKey: 'kind_of_mark_id'
        });
    };
    return BaseOperatingValueOfMark;
};