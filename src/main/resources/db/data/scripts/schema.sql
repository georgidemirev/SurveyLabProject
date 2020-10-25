
create TABLE `user` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

create TABLE `poll` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP NOT NULL,
    text VARCHAR(250) NOT NULL ,
    is_multiple_choice BOOLEAN DEFAULT FALSE,
    is_completed BOOLEAN DEFAULT FALSE,
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT `fk_poll_user_user_id` FOREIGN KEY (user_id)
    REFERENCES user(id)
);

create TABLE `poll_option` (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_on TIMESTAMP NOT NULL,
    number_of_votes INT DEFAULT 0,
    text VARCHAR(255) NOT NULL,
    poll_id BIGINT NOT NULL,
    CONSTRAINT `fk_poll_option_poll_poll_id` FOREIGN KEY (poll_id)
    REFERENCES poll(id)
);

create TABLE `user_poll_option` (
	user_id BIGINT NOT NULL,
    poll_option_id BIGINT NOT NULL,
    CONSTRAINT `pk_user_poll_option` PRIMARY KEY (user_id, poll_option_id),
    CONSTRAINT `fk_user_poll_option_user_id` FOREIGN KEY (user_id)
    REFERENCES user(id),
    CONSTRAINT `fk_user_poll_option_poll_option_id` FOREIGN KEY (poll_option_id)
    REFERENCES poll_option(id)
);