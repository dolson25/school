drop table if exists user;

create table user
(
	id integer not null primary key autoincrement,
	username text not null,
	password text not null,
	firstname text not null,
	lastname text not null,
	email text not null,
	numberRecordsIndexed integer not null,
	batchCheckedOut integer not null,
	batchNumberAssigned integer not null
);


drop table if exists project;

create table project
(
	id integer not null primary key autoincrement,
	title text not null,
	recordsPerImage integer not null,
	firstYCoordinate integer not null,
	recordHeight integer not null
);


drop table if exists image;

create table image
(
	id integer not null primary key autoincrement,
	filepath text not null,
	project_id int not null,
	available integer not null
);


drop table if exists field;

create table field
(
	id integer not null primary key autoincrement,
	title text not null,
	xCoordinate integer not null,
	width integer not null,
	fieldHelpPath text not null,
	knownDataPath text,
	project_id int not null,
	columnNumber integer not null
);


drop table if exists record;

create table record
(
	id integer not null primary key autoincrement,
	image_id int not null,
	rowNumber integer not null
);


drop table if exists cell;

create table cell
(
	id integer not null primary key autoincrement,
	field_id int not null,
	record_id int not null,
	value text
);
