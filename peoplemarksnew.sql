-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Май 01 2024 г., 18:52
-- Версия сервера: 10.4.32-MariaDB
-- Версия PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `peoplemarksnew`
--

-- --------------------------------------------------------

--
-- Структура таблицы `base_operating_value_of_mark`
--

CREATE TABLE `base_operating_value_of_mark` (
  `id` int(10) NOT NULL,
  `kind_of_mark_id` int(10) NOT NULL,
  `min_value` double NOT NULL,
  `max_value` double NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `blog`
--

CREATE TABLE `blog` (
  `id` int(10) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `enumeration_value`
--

CREATE TABLE `enumeration_value` (
  `id` int(10) NOT NULL,
  `kind_of_mark_id` int(10) NOT NULL,
  `value` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `enumeration_value`
--

INSERT INTO `enumeration_value` (`id`, `kind_of_mark_id`, `value`) VALUES
(1, 5, 'положительно'),
(2, 5, 'отрицательно');

-- --------------------------------------------------------

--
-- Структура таблицы `enum_kind_of_mark`
--

CREATE TABLE `enum_kind_of_mark` (
  `id` int(10) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `enum_kind_of_mark`
--

INSERT INTO `enum_kind_of_mark` (`id`, `name`) VALUES
(1, 'Number'),
(2, 'String'),
(3, 'Bool'),
(4, 'Enumeration'),
(5, 'Number2');

-- --------------------------------------------------------

--
-- Структура таблицы `favorite_mark`
--

CREATE TABLE `favorite_mark` (
  `id` int(10) NOT NULL,
  `kind_of_mark_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `file`
--

CREATE TABLE `file` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL,
  `name` varchar(150) NOT NULL,
  `comment` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Структура таблицы `kind_of_mark`
--

CREATE TABLE `kind_of_mark` (
  `id` int(10) NOT NULL,
  `name` varchar(150) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `enum_kind_of_mark_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `kind_of_mark`
--

INSERT INTO `kind_of_mark` (`id`, `name`, `user_id`, `enum_kind_of_mark_id`) VALUES
(1, 'Давление', NULL, 5),
(2, 'Пульс', NULL, 1),
(3, 'Сахар', NULL, 1),
(4, 'Холестерин', NULL, 1),
(5, 'Тест на что-то', NULL, 4),
(6, 'my', 1, 1),
(9, 'ее', 1, 1),
(10, 'ккк', 1, 1),
(11, 'uuuuu', 1, 2),
(14, 'rr', 1, 2),
(15, 'rr', 1, 2),
(16, 'rr', 1, 2),
(17, 'rr', 1, 2),
(18, 'eeeeeeeee', 1, 2);

-- --------------------------------------------------------

--
-- Структура таблицы `mark_value`
--

CREATE TABLE `mark_value` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `kind_of_mark_id` int(10) NOT NULL,
  `date` datetime NOT NULL,
  `value_number1` double DEFAULT NULL,
  `value_number2` double DEFAULT NULL,
  `value_bool` tinyint(1) DEFAULT NULL,
  `value_string` varchar(150) DEFAULT NULL,
  `value_enum` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `mark_value`
--

INSERT INTO `mark_value` (`id`, `user_id`, `kind_of_mark_id`, `date`, `value_number1`, `value_number2`, `value_bool`, `value_string`, `value_enum`) VALUES
(1, 1, 1, '2024-05-01 07:40:00', 120, 78, NULL, NULL, NULL),
(2, 1, 2, '2024-05-01 07:41:00', 75, NULL, NULL, NULL, NULL),
(3, 1, 5, '2024-05-01 07:41:00', NULL, NULL, NULL, NULL, 1),
(4, 1, 9, '2024-05-01 18:18:36', NULL, NULL, NULL, '123333', NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `prompt`
--

CREATE TABLE `prompt` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `name` varchar(150) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `prompt`
--

INSERT INTO `prompt` (`id`, `user_id`, `name`, `description`, `date`) VALUES
(1, 1, 'r', 'r', '2024-05-01 07:41:00'),
(2, 1, 'r', 'r', '2024-05-01 15:07:00');

-- --------------------------------------------------------

--
-- Структура таблицы `user`
--

CREATE TABLE `user` (
  `id` int(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  `height` int(10) NOT NULL,
  `weight` int(10) NOT NULL,
  `date_birth` date NOT NULL,
  `gender` int(10) NOT NULL,
  `password` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Дамп данных таблицы `user`
--

INSERT INTO `user` (`id`, `name`, `height`, `weight`, `date_birth`, `gender`, `password`, `login`) VALUES
(1, 'Иван Иванов', 176, 76, '2006-05-04', 1, '', '');

-- --------------------------------------------------------

--
-- Структура таблицы `user_operating_value_of_mark`
--

CREATE TABLE `user_operating_value_of_mark` (
  `id` int(10) NOT NULL,
  `kind_of_mark_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `min_value` double NOT NULL,
  `max_value` double NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `base_operating_value_of_mark`
--
ALTER TABLE `base_operating_value_of_mark`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kind_of_mark_id` (`kind_of_mark_id`);

--
-- Индексы таблицы `blog`
--
ALTER TABLE `blog`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `enumeration_value`
--
ALTER TABLE `enumeration_value`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kind_of_mark_id` (`kind_of_mark_id`);

--
-- Индексы таблицы `enum_kind_of_mark`
--
ALTER TABLE `enum_kind_of_mark`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `favorite_mark`
--
ALTER TABLE `favorite_mark`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kind_of_mark_id` (`kind_of_mark_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Индексы таблицы `file`
--
ALTER TABLE `file`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Индексы таблицы `kind_of_mark`
--
ALTER TABLE `kind_of_mark`
  ADD PRIMARY KEY (`id`),
  ADD KEY `enum_kind_of_mark_id` (`enum_kind_of_mark_id`),
  ADD KEY `kind_of_mark_ibfk_2` (`user_id`);

--
-- Индексы таблицы `mark_value`
--
ALTER TABLE `mark_value`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `kind_of_mark_id` (`kind_of_mark_id`),
  ADD KEY `value_enum` (`value_enum`);

--
-- Индексы таблицы `prompt`
--
ALTER TABLE `prompt`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Индексы таблицы `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `user_operating_value_of_mark`
--
ALTER TABLE `user_operating_value_of_mark`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kind_of_mark_id` (`kind_of_mark_id`),
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `base_operating_value_of_mark`
--
ALTER TABLE `base_operating_value_of_mark`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `blog`
--
ALTER TABLE `blog`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `enumeration_value`
--
ALTER TABLE `enumeration_value`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `enum_kind_of_mark`
--
ALTER TABLE `enum_kind_of_mark`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблицы `favorite_mark`
--
ALTER TABLE `favorite_mark`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `file`
--
ALTER TABLE `file`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT для таблицы `kind_of_mark`
--
ALTER TABLE `kind_of_mark`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT для таблицы `mark_value`
--
ALTER TABLE `mark_value`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `prompt`
--
ALTER TABLE `prompt`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT для таблицы `user_operating_value_of_mark`
--
ALTER TABLE `user_operating_value_of_mark`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `base_operating_value_of_mark`
--
ALTER TABLE `base_operating_value_of_mark`
  ADD CONSTRAINT `base_operating_value_of_mark_ibfk_1` FOREIGN KEY (`kind_of_mark_id`) REFERENCES `kind_of_mark` (`id`) ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `enumeration_value`
--
ALTER TABLE `enumeration_value`
  ADD CONSTRAINT `enumeration_value_ibfk_1` FOREIGN KEY (`kind_of_mark_id`) REFERENCES `kind_of_mark` (`id`) ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `favorite_mark`
--
ALTER TABLE `favorite_mark`
  ADD CONSTRAINT `favorite_mark_ibfk_1` FOREIGN KEY (`kind_of_mark_id`) REFERENCES `kind_of_mark` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `favorite_mark_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `file`
--
ALTER TABLE `file`
  ADD CONSTRAINT `file_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `kind_of_mark`
--
ALTER TABLE `kind_of_mark`
  ADD CONSTRAINT `kind_of_mark_ibfk_1` FOREIGN KEY (`enum_kind_of_mark_id`) REFERENCES `enum_kind_of_mark` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `kind_of_mark_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ограничения внешнего ключа таблицы `mark_value`
--
ALTER TABLE `mark_value`
  ADD CONSTRAINT `mark_value_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mark_value_ibfk_2` FOREIGN KEY (`kind_of_mark_id`) REFERENCES `kind_of_mark` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `mark_value_ibfk_3` FOREIGN KEY (`value_enum`) REFERENCES `enumeration_value` (`id`) ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `prompt`
--
ALTER TABLE `prompt`
  ADD CONSTRAINT `prompt_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `user_operating_value_of_mark`
--
ALTER TABLE `user_operating_value_of_mark`
  ADD CONSTRAINT `user_operating_value_of_mark_ibfk_1` FOREIGN KEY (`kind_of_mark_id`) REFERENCES `kind_of_mark` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `user_operating_value_of_mark_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
