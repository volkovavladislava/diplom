module.exports = (sequelize, Sequelize) => {
    var EnumerationValue = sequelize.define(
        'enumeration_value', // определяем имя таблицы
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
            value: {
                type: Sequelize.STRING(50), // тип данных String (в MySQL Varchar)
                allowNull: false
            }
        });

  
        EnumerationValue.associate = (models) => {

            EnumerationValue.belongsTo(models.kind_of_mark, {
                foreignKey: 'kind_of_mark_id'
            });

            EnumerationValue.hasMany(models.mark_value, {
                foreignKey: 'value_enum',
                onDelete: 'RESTRICT',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });
    };
    return EnumerationValue;
};