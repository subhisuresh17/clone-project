# Pro-Collab
First Add this code into mysql and run the mysql file 
create database Pro_Collab;
use Pro_Collab;
CREATE TABLE role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255) NOT NULL
);
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    photo VARCHAR(255)
);
CREATE TABLE user_role (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE TABLE task (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    creator_name VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    description TEXT NOT NULL,
    is_completed BOOLEAN NOT NULL,
    name VARCHAR(255) NOT NULL,
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES user(user_id)
);



