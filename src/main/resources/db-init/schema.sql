CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) not null,
    name VARCHAR(255) not null,
    email VARCHAR(255) not null,
    role VARCHAR(255) not null,
    nickname VARCHAR(255) not null,
    img VARCHAR(1000) not null DEFAULT  'http://example.com/default-image.png',
    oauth2 VARCHAR(255),
    phoneNumber VARCHAR(255) not null
    );

CREATE TABLE IF NOT EXISTS board (
                                     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    content TEXT ,
    writer_id BIGINT NOT NULL,
    img VARCHAR(1000) not null DEFAULT  'http://example.com/default-image.png',
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    category VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS board_like(
                                         id       BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                         board_id BIGINT NOT NULL,
                                         user_id  BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS cafe(
                                   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   owner_id BIGINT NOT NULL,
                                   cafeName VARCHAR(255) NOT NULL,
    address VARCHAR(1000) NOT NULL,
    lat DOUBLE NOT NULL ,
    lng DOUBLE NOT NULL ,
    category VARCHAR(255),
    img VARCHAR(1000) DEFAULT 'http://example.com/default-image.png'
    );

CREATE TABLE IF NOT EXISTS cafe_menu (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     cafe_id BIGINT NOT NULL,
     img VARCHAR(1000) DEFAULT 'http://example.com/default-image.png',
    content TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS cafe_like (
                                         id BIGINT NOT NULL AUTO_INCREMENT primary key ,
                                         cafe_id BIGINT NOT NULL ,
                                         user_id BIGINT NOT NULL
);

CREATE  TABLE  IF NOT EXISTS  comment (
                                          id BIGINT NOT NULL AUTO_INCREMENT primary key ,
                                          board_id BIGINT NOT NULL ,
                                          parent_id BIGINT NOT NULL ,
                                          content TEXT,
                                          writer_id BIGINT NOT NULL ,
                                          write_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS message (
                                       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       type VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    message_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content_id BIGINT NOT NULL
    );

CREATE TABLE IF NOT EXISTS  oauth2_user(
   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id BIGINT NOT NULL,
   oauth2_name VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS report (
      id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
      content_id BIGINT NOT NULL,
      content TEXT ,
      report_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      report_type VARCHAR(255) NOT NULL,
    report_id BIGINT NOT NULL
    );

CREATE TABLE IF NOT EXISTS review (
      id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
      cafe_id BIGINT NOT NULL,
      writer_id BIGINT NOT NULL,
      rating DECIMAL(2, 1) NOT NULL,
    review TEXT NOT NULL,
    write_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS review_category (
       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
       review_id BIGINT NOT NULL,
       category_id BIGINT NOT NULL
);

