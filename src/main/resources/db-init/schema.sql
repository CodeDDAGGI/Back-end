CREATE TABLE IF NOT EXISTS user (
        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255) UNIQUE NOT NULL,
        password VARCHAR(255),  -- OAuth2 사용자는 NULL 가능
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        role ENUM('USER', 'OWNER') NOT NULL,  -- 일반회원, 기업회원(점주) 구분
        nickname VARCHAR(255) NOT NULL,
        img VARCHAR(1000) NOT NULL DEFAULT 'http://example.com/default-image.png',
        phoneNumber VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS owner (
         id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         user_id BIGINT NOT NULL,
         business_number VARCHAR(20) UNIQUE NOT NULL,  -- 사업자 등록번호
         company_name VARCHAR(255) NOT NULL,  -- 회사명
         company_address VARCHAR(255) NOT NULL,  -- 회사 주소
         FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
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
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     board_id BIGINT NOT NULL,
     user_id BIGINT NOT NULL,
     FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
     FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
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
    content TEXT
    );



CREATE TABLE IF NOT EXISTS cafe_like (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     cafe_id BIGINT NOT NULL,
     user_id BIGINT NOT NULL,
     FOREIGN KEY (cafe_id) REFERENCES cafe(id) ON DELETE CASCADE,
     FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE  TABLE  IF NOT EXISTS  comment (
      id BIGINT NOT NULL AUTO_INCREMENT primary key ,
      boardId BIGINT NOT NULL ,
      parentId BIGINT NOT NULL ,
      content TEXT,
      writer_id BIGINT NOT NULL ,
      write_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS message (
       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
       type VARCHAR(255) NOT NULL,
        content TEXT NOT NULL,
       userId BIGINT NOT NULL,
       messageDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       contentId BIGINT NOT NULL
    );

CREATE TABLE IF NOT EXISTS  oauth2_user(
   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   user_id BIGINT NOT NULL,
   oAuth2Name VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS report (
      id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
      content_id BIGINT NOT NULL,
      content TEXT,
      reportDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      reportType VARCHAR(255) NOT NULL,
      user_id BIGINT NOT NULL,  -- 신고한 사용자
      FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
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
   category_id BIGINT NOT NULL,
   FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);


