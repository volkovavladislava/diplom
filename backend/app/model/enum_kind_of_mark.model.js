module.exports = (sequelize, Sequelize) => {
    var EnumKindOfMark = sequelize.define(
        'enum_kind_of_mark', // определяем имя таблицы
        {
            id: {
                type: Sequelize.INTEGER(10), // тип данных INTEGER
                autoIncrement: true, // тип данных INTEGER
                primaryKey: true, // поле является первичным ключом
                allowNull: false // Настройка allowNull со значением false запрещает запись в поле значений NULL
            },
            name: {
                type: Sequelize.STRING(255), // тип данных String (в MySQL Varchar)
                allowNull: false
            }
        });

  
    EnumKindOfMark.associate = (models) => {

        EnumKindOfMark.hasMany(models.kind_of_mark, {
            foreignKey: 'enum_kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });
    };
    return EnumKindOfMark;
};