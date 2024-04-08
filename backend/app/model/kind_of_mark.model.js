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

    // // Определяем связи таблицы category с другими таблицами
    // KindOfMark.associate = (models) => {
    //     // Определение связи один-ко-многим с таблицей user_category. Это определение связи с одной стороны.
    //     // Связь также определена со второй стороны (со стороны таблицы user_category): в файле user_category.model.js
    //     KindOfMark.hasMany(models.user_category, {
    //         foreignKey: 'category_id',
    //         onDelete: 'CASCADE',
    //         onUpdate: 'CASCADE',
    //         sourceKey: 'id'
    //     });
    // };
    return KindOfMark;
};