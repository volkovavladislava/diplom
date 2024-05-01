module.exports = (sequelize, Sequelize) => {
    var User = sequelize.define(
        'user', // определяем имя таблицы
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
            },
            height: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            weight: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            date_birth: {
                type: Sequelize.DATEONLY,
                allowNull: false
            },
            gender: {
                type: Sequelize.INTEGER(10),
                allowNull: false
            },
            password: {
                type: Sequelize.STRING(255),
                allowNull: false
            },
            login: {
                type: Sequelize.STRING(255),
                allowNull: false
            }
        });

        User.associate = (models) => {
        
        //     User.belongsTo(models.enum_kind_of_mark, {
        //     foreignKey: 'enum_kind_of_mark_id'
        // });
            User.hasMany(models.prompt, {
                foreignKey: 'user_id',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });

            User.hasMany(models.kind_of_mark, {
                foreignKey: 'user_id',
                onDelete: 'RESTRICT',
                onUpdate: 'RESTRICT',
                sourceKey: 'id'
            });


            User.hasMany(models.file, {
                foreignKey: 'user_id',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });

            User.hasMany(models.user_operating_value_of_mark, {
                foreignKey: 'user_id',
                onDelete: 'RESTRICT',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });

            User.hasMany(models.favorite_mark, {
                foreignKey: 'user_id',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });

            User.hasMany(models.mark_value, {
                foreignKey: 'user_id',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                sourceKey: 'id'
            });
    };
    return User;
};