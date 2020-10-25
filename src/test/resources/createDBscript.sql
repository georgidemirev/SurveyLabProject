--CREATE TABLE `user` (
--  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
--  `created_on` timestamp NULL DEFAULT NULL,
--  `email` varchar(255) DEFAULT NULL,
--  `first_name` varchar(255) DEFAULT NULL,
--  `last_name` varchar(255) DEFAULT NULL,
--) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
--
--
--CREATE TABLE `poll` (
--  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
--  `created_on` timestamp NULL DEFAULT NULL,
--  `allow_multiple_answers` tinyint(1) DEFAULT NULL,
--  `is_anonymous` tinyint(1) DEFAULT NULL,
--  `is_completed` tinyint(1) DEFAULT NULL,
--  `poll_name` varchar(255) DEFAULT NULL,
--  `user_id` bigint(20) DEFAULT NULL,
--  KEY `user_id` (`user_id`),
--  CONSTRAINT `poll_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
--CREATE TABLE `poll_option` (
--  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
--  `created_on` timestamp NULL DEFAULT NULL,
--  `number_of_votes` int(11) DEFAULT NULL,
--  `text` varchar(255) DEFAULT NULL,
--  `poll_id` bigint(20) DEFAULT NULL,
--  KEY `poll_id` (`poll_id`),
--  CONSTRAINT `poll_option_ibfk_1` FOREIGN KEY (`poll_id`) REFERENCES `poll` (`id`)
--) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
--
--
--CREATE TABLE `user_poll_option` (
--  `user_id` bigint(20) NOT NULL,
--  `poll_option_id` bigint(20) NOT NULL,
--  PRIMARY KEY (`poll_option_id`,`user_id`),
--  KEY `user_id` (`user_id`),
--  CONSTRAINT `user_poll_option_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
--  CONSTRAINT `user_poll_option_ibfk_2` FOREIGN KEY (`poll_option_id`) REFERENCES `poll_option` (`id`)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE `poll` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP,
    allow_multiple_answers BOOLEAN,
    is_anonymous BOOLEAN,
    is_completed BOOLEAN,
    poll_name VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT `fk_poll_user_user_id` FOREIGN KEY (user_id)
    REFERENCES user(id)
);

CREATE TABLE `poll_option` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP,
    number_of_votes INT,
    text VARCHAR(255),
    poll_id BIGINT,
    CONSTRAINT `fk_poll_option_poll_poll_id` FOREIGN KEY (poll_id)
    REFERENCES poll(id)
);

CREATE TABLE `user_poll_option` (
	user_id BIGINT,
    poll_option_id BIGINT,
    CONSTRAINT `pk_user_poll_option` PRIMARY KEY (user_id, poll_option_id),
    CONSTRAINT `fk_user_poll_option_user_id` FOREIGN KEY (user_id)
    REFERENCES user(id),
    CONSTRAINT `fk_user_poll_option_poll_option_id` FOREIGN KEY (poll_option_id)
    REFERENCES poll_option(id)
);