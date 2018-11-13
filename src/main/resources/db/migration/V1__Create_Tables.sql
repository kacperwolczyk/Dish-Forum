


CREATE TABLE dish (
  
id bigint auto_increment primary key,
  
category varchar(20) not null,
  
name varchar(20) not null);  

CREATE TABLE recipe (
  
id bigint auto_increment primary key,  
avg_rating double not null,
  
content varchar(300) not null,
  
user varchar(20) not null,
  
dish_id bigint not null);

CREATE TABLE comment (
  
id bigint auto_increment primary key,
content varchar(200) not null,
 
rating double default not null,
  
user varchar(20) not null,
  
recipe_id bigint not null);