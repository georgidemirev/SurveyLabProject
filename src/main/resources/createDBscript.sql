create SCHEMA survey_lab;
USE survey_lab;

create TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` timestamp NULL DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create TABLE `poll` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` timestamp NULL DEFAULT NULL,
  `allow_multiple_answers` tinyint(1) DEFAULT NULL,
  `is_anonymous` tinyint(1) DEFAULT NULL,
  `is_completed` tinyint(1) DEFAULT NULL,
  `poll_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `poll_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create TABLE `poll_option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_on` timestamp NULL DEFAULT NULL,
  `number_of_votes` int(11) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `poll_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `poll_id` (`poll_id`),
  CONSTRAINT `poll_option_ibfk_1` FOREIGN KEY (`poll_id`) REFERENCES `poll` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create TABLE `user_poll_option` (
  `user_id` bigint(20) NOT NULL,
  `poll_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`poll_option_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_poll_option_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_poll_option_ibfk_2` FOREIGN KEY (`poll_option_id`) REFERENCES `poll_option` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;