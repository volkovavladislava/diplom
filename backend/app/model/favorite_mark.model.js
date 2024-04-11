module.exports = (sequelize, Sequelize) => {
    var FavoriteMark = sequelize.define(
        'favorite_mark', // определяем имя таблицы
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
            }
        });

        FavoriteMark.associate = (models) => {

            FavoriteMark.belongsTo(models.user, {
                foreignKey: 'user_id'
            });

            FavoriteMark.belongsTo(models.kind_of_mark, {
                foreignKey: 'kind_of_mark_id'
            });
       
    };
    return FavoriteMark;
};