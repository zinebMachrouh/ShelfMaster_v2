CREATE TABLE users (
  id uuid PRIMARY KEY,
  name varchar(50),
  email varchar(150),
);

CREATE TABLE students(
    studyProgram varchar(100),
    borrowLimit int default 150,
) inherits (users);

CREATE TABLE professors(
    department varchar(100),
) inherits (users);

CREATE TABLE documents(
    id uuid PRIMARY KEY,
    title varchar(250),
    author varchar(100),
    releaseDate date,
    pages int,
    borrowedBy uuid,
    reservedBy uuid,
    FOREIGN KEY (borrowedBy) REFERENCES users(id),
    FOREIGN KEY (reservedBy) REFERENCES users(id),
);

CREATE TABLE books(
    ISBN varchar(13) ,
) inherits (documents);

CREATE TABLE magazines(
    number int,
) inherits (documents);

CREATE TABLE scientificJournal(
    researchField varchar(100),
    editor varchar(50),
) inherits (documents);

CREATE TABLE universityTheisis(
    university varchar(250),
    fieldOfStudy varchar(100),
    submitedYear int,
) inherits (documents);