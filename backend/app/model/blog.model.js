module.exports = (sequelize, Sequelize) => {
    var Blog = sequelize.define(
        'blog', // определяем имя таблицы
        {
            id: {
                type: Sequelize.INTEGER(10), // тип данных INTEGER
                autoIncrement: true, // тип данных INTEGER
                primaryKey: true, // поле является первичным ключом
                allowNull: false // Настройка allowNull со значением false запрещает запись в поле значений NULL
            },
            title: {
                type: Sequelize.STRING(255), // тип данных String (в MySQL Varchar)
                allowNull: false
            },
            description: {
                type: Sequelize.STRING(255),
                allowNull: false
            },
            link: {
                type: Sequelize.STRING(255),
                allowNull: false
            }
        });

       
    return Blog;
};