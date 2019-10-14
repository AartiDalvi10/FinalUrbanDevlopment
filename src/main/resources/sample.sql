create table Area (
areaCode INT PRIMARY KEY,
areaName varchar(100) not null,
Density INT  
);

insert into Area values (1,'Sector 3',2000);

create table Administrator (
adminCode bigint  PRIMARY KEY,
adminName varchar(100) not null,
adminEmail varchar(100) not null,
department varchar(100) 
);

insert into Administrator values (1,'Abhijit Roy','Abhijit.Roy@syntelinc.com','School');

create table Transaction (
id bigint  PRIMARY KEY,
transactionId bigint not null ,
department varchar(100) not null,
areaCode bigint REFERENCES Area(areaCode) not null,
adminCode bigint REFERENCES Administrator(adminCode) not null,
longitude numeric  not null,
latitude  numeric  not null,
Timestamp TIMESTAMP
);


insert into Transaction values (1,1,'School',1,1,151.2152,-33.8569, '2011-05-16 15:36:38');

create table Suggestions (
id bigint  PRIMARY KEY,
transactionId bigint REFERENCES Transaction(id) not null,
suggestionId bigint not null,
suggestion varchar(1000) not null,
Notify varchar(100) 
);

insert into Suggestions values (1,1,1,'open more School', 'Notify');


CREATE TABLE Traffic
(
    trafficId    serial primary key,
    duration        VARCHAR(200) not null,
    sourceLng  numeric  not null,      
    sourceLat numeric  not null,
    distance VARCHAR(200) not null,
    start_address VARCHAR(200) not null,
    endLng numeric  not null,
    endLat numeric  not null,
    duration_in_traffic  VARCHAR(200) not null,
    end_address  VARCHAR(200) not null
);


String duration, Double sourceLng, Double sourceLat, String distance,
			String start_address, Double endLng, Double endLat, String duration_in_traffic, String end_address) {
		// TODO Auto-generated method 


create table Administrator (
adminCode bigint  PRIMARY KEY,
adminName varchar(100) not null,
adminEmail varchar(100) not null,
department varchar(100) 
);