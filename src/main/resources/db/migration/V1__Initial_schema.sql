CREATE TABLE IF NOT EXISTS user (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(64) NOT NULL,
  username VARCHAR(32) UNIQUE NOT NULL,
  password VARCHAR(32) NOT NULL,
  type VARCHAR(16) NOT NULL,
  score INT NOT NULL,
  is_banned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
  question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  title VARCHAR(128) NOT NULL,
  text VARCHAR(1000) NOT NULL,
  creation_date_time DATETIME NOT NULL,
  score INT NOT NULL,

  INDEX(user_id),
  FOREIGN KEY(user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tag (
  tag_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY NOT NULL,
  name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS question_tag (
  question_tag_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  question_id INT NOT NULL,
  tag_id INT NOT NULL,

  INDEX(question_id, tag_id),
  FOREIGN KEY(question_id) REFERENCES question(question_id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(tag_id) REFERENCES tag(tag_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS answer (
  answer_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  question_id INT NOT NULL,
  text VARCHAR(128) NOT NULL,
  creation_date_time DATETIME NOT NULL,
  score INT NOT NULL,

  INDEX(user_id, question_id),
  FOREIGN KEY(user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(question_id) REFERENCES question(question_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS question_vote (
  question_vote_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  question_id INT NOT NULL,
  user_id INT NOT NULL,
  vote INT NOT NULL,

  INDEX(user_id, question_id),
  FOREIGN KEY(question_id) REFERENCES question(question_id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS answer_vote (
  answer_vote_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  answer_id INT NOT NULL,
  user_id INT NOT NULL,
  vote INT NOT NULL,

  INDEX(user_id, answer_id),
  FOREIGN KEY(answer_id) REFERENCES answer(answer_id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

