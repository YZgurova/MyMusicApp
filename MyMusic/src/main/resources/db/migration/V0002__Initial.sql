CREATE DATABASE IF NOT EXISTS myMusic_db;

USE myMusic_db;

CREATE TABLE IF NOT EXISTS user(
`id` INT PRIMARY KEY,
`first_name` VARCHAR(20) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`username` VARCHAR(40) NOT NULL UNIQUE,
`email` VARCHAR(50) NOT NULL UNIQUE,
`password` VARCHAR(60) NOT NULL,
`created_on` timestamp DEFAULT current_timestamp,
FOREIGN KEY (id) REFERENCES credentials(id),
constraint `ch_email` check (`email` regexp '[A-Za-z._0-9]+@[A-Za-z.]')
) ;

CREATE TABLE IF NOT EXISTS song(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL,
`url` VARCHAR(1000) NOT NULL,
`author_id` INT,
`price` DECIMAL(19,2) NOT NULL,
`votes` INT default 0,
foreign key(`author_id`) references user(id)
);

CREATE TABLE IF NOT EXISTS loved_song_to_user(
user_id INT NOT NULL,
song_id INT NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(id),
FOREIGN KEY (song_id) REFERENCES song(id),
PRIMARY KEY (user_id, song_id)
);

CREATE TABLE IF NOT EXISTS bought_song_to_user(
`user_id` INT NOT NULL,
`song_id` INT NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(id),
FOREIGN KEY (song_id) REFERENCES song(id),
PRIMARY KEY (user_id, song_id)
);

CREATE TABLE IF NOT EXISTS transactions(
`id` INT AUTO_iNCREMENT PRIMARY KEY NOT NULL,
`user_id` INT NOT NULL,
`transaction` VARCHAR(100) NOT NULL,
`status` VARCHAR(10) NOT NULL,
FOREIGN KEY(`user_id`) REFERENCES user(id));

CREATE TABLE IF NOT EXISTS wallet(
`owner_id` INT PRIMARY KEY NOT NULL,
`resources` DECIMAL(19,2) DEFAULT 0,
FOREIGN KEY (`owner_id`) REFERENCES credentials(id));

INSERT INTO wallet (owner_id, resources) VALUES (1, 0);



