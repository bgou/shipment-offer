CREATE TABLE shipment (id int primary key auto_increment, capacity int not null);
CREATE TABLE driver (id int primary key auto_increment, capacity int);
CREATE TABLE offer (
   id int primary key auto_increment,
   shipmentId int NOT NULL,
   driverId varchar(255) NOT NULL,
   accepted tinyint DEFAULT 0)