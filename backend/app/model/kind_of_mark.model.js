module.exports = (sequelize, Sequelize) => {
    var KindOfMark = sequelize.define(
        'kind_of_mark', // определяем имя таблицы
        {
            id: {
                type: Sequelize.INTEGER(10), // тип данных INTEGER
                autoIncrement: true, // тип данных INTEGER
                primaryKey: true, // поле является первичным ключом
                allowNull: false // Настройка allowNull со значением false запрещает запись в поле значений NULL
            },
            name: {
                type: Sequelize.STRING(150), // тип данных String (в MySQL Varchar)
                allowNull: false
            },
            enum_kind_of_mark_id: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            }
        });

    KindOfMark.associate = (models) => {

        KindOfMark.belongsTo(models.enum_kind_of_mark, {
            foreignKey: 'enum_kind_of_mark_id'
        });

        KindOfMark.hasMany(models.user_operating_value_of_mark, {
            foreignKey: 'kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });

        KindOfMark.hasMany(models.base_operating_value_of_mark, {
            foreignKey: 'kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });

        KindOfMark.hasMany(models.enumeration_value, {
            foreignKey: 'kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });

        KindOfMark.hasMany(models.favorite_mark, {
            foreignKey: 'kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });

        KindOfMark.hasMany(models.mark_value, {
            foreignKey: 'kind_of_mark_id',
            onDelete: 'RESTRICT',
            onUpdate: 'CASCADE',
            sourceKey: 'id'
        });
    };
    return KindOfMark;
};