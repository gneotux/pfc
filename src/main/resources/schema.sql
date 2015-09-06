create table "users"
("id" SERIAL NOT NULL PRIMARY KEY,
"email" VARCHAR(254) NOT NULL,
"first_name" VARCHAR(254),
"last_name" VARCHAR(254),
"twitter_id" VARCHAR(254),
"linkedin_id" VARCHAR(254),
"bio" VARCHAR(254),
"permission" VARCHAR(254) NOT NULL,
"password_id" integer
);

create table "activities"
("id" SERIAL NOT NULL PRIMARY KEY,
"event_id" integer NOT NULL,
"location_id" integer NOT NULL,
"activity_type_id" integer NOT NULL,
"title" VARCHAR(254),
"description" VARCHAR(254),
"objective" VARCHAR(254),
"start_time" timestamp without time zone NOT NULL,
"end_time" timestamp without time zone NOT NULL,
"resources" VARCHAR(254)
);

create table "events"
("id" SERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOT NULL,
"description" VARCHAR(254),
"website" VARCHAR(254),
"twitter_hashtag" VARCHAR(254),
"logo_url" VARCHAR(254)
);

create table "locations"
("id" SERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOT NULL,
"code" VARCHAR(254),
"latitude" double precision NOT NULL,
"longitude" double precision NOT NULL,
"capacity" integer,
"description" VARCHAR(254),
"photo_url" VARCHAR(254)
);

create table "atendees"
("id" SERIAL NOT NULL PRIMARY KEY,
"activity_id" integer NOT NULL,
"user_id" integer NOT NULL
);

create table "speakers"
("id" SERIAL NOT NULL PRIMARY KEY,
"activity_id" integer NOT NULL,
"user_id" integer NOT NULL
);

create table "activityTags"
("id" SERIAL NOT NULL PRIMARY KEY,
"tag_id" integer NOT NULL,
"activity_id" integer NOT NULL
);

create table "activityTypes"
("id" SERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOT NULL
);

create table "companies"
("id" SERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOT NULL,
"email" VARCHAR(254) NOT NULL,
"phone" VARCHAR(254) NOT NULL,
"description" VARCHAR(254),
"website" VARCHAR(254),
"logo_url" VARCHAR(254)
);

create table "tags"
("id" SERIAL NOT NULL PRIMARY KEY,
"name" VARCHAR(254) NOt NULL,
"color" VARCHAR(254),
"short_name" VARCHAR(254)
);

create table "sponsors"
("id" SERIAL NOT NULL PRIMARY KEY,
"company_id" integer NOT NULL,
"event_id" integer NOT NULL
);

create table "eventdays"
("id" SERIAL NOT NULL PRIMARY KEY,
"event_id" integer NOT NULL,
"start_time" timestamp without time zone NOT NULL,
"end_time" timestamp without time zone NOT NULL
);


create table "passwords"
("id" SERIAL NOT NULL PRIMARY KEY,
"hashed_password" VARCHAR(254),
"salt" VARCHAR(254));

-- User: test1@test.com, Password: password1
INSERT INTO passwords(
id, hashed_password, salt)
VALUES (1,'$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC', '$2a$10$U3gBQ50FY5qiQ5XeQKgWwO');
INSERT INTO passwords(
id, hashed_password, salt)
VALUES (1,'$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC', '$2a$10$U3gBQ50FY5qiQ5XeQKgWwO');

INSERT INTO users(
id, email, first_name, last_name, twitter_id, linkedin_id, bio, permission,  password_id)
VALUES (1, 'giancarlo@pfc.com', 'Giancarlo', 'Munoz', 'gneotux', 'giancarlo_munoz', 'Im just a developer...', 'ADMIN',1);

INSERT INTO users(
id, email, first_name, last_name, twitter_id, linkedin_id, bio, permission,  password_id)
VALUES (1, 'alejandro@pfc.com', 'Alejandro', 'Baldominos', '', '', '', 'ADMIN',2);

INSERT INTO activities(
id, event_id, location_id, activity_type_id, title, description, objective, start_time, end_time, resources
) VALUES (1, 1, 1, 1, 'Welcome talk', 'This is the meeting and greetings for the scala days', 'Meeting and saying hellos', TIMESTAMP '2015-07-16 15:00:00', TIMESTAMP '2015-07-19 21:00:00', 'github/gneotux');
INSERT INTO activities(
id, event_id, location_id, activity_type_id, title, description, objective, start_time, end_time, resources
) VALUES (2, 1, 2, 5, 'Is Java Alive?', 'Do we need Java anymore?', 'Discuss about the future of Scala in the JVM', TIMESTAMP '2015-07-17 15:00:00', TIMESTAMP '2015-07-17 21:00:00', 'github/gneotux');

INSERT INTO events(id, name, description, website, twitter_hashtag, logo_url) VALUES (1, 'ScalaDays', 'The most important event in the scala world', 'www.scaladays.com', '#scaladays', 'http://scaladays.jpg');
INSERT INTO events(id, name, description, website, twitter_hashtag, logo_url) VALUES (2, 'ScalaWorld', 'The second most important event in the scala world', 'www.scala-world.com', '#scalaworld', 'http://scalaworld.jpg');
INSERT INTO events(id, name, description, website, twitter_hashtag, logo_url) VALUES (3, 'E3', 'The event for the videogames lovers', 'www.e3.com', '#e3', 'http://e3.jpg');

INSERT INTO locations(id, name, code, latitude, longitude, capacity, description, photo_url) VALUES (1, 'Auditorium UC3M', 'ROOM2', 1231231111.22312, 12312333123.123123, 200, 'University Carlos III space for acts', 'www.uc3m.es');
INSERT INTO locations(id, name, code, latitude, longitude, capacity, description, photo_url) VALUES (2, 'Leganes F.C. Stadium', 'ROOM$', 1231231111.22312, 12312333123.123123, 2000, 'Leganes football club stadium', 'www.leganes.es');

INSERT INTO "activityTypes" (id, name) VALUES (1, 'Presentation');
INSERT INTO "activityTypes" (id, name) VALUES (2, 'Webinar');
INSERT INTO "activityTypes" (id, name) VALUES (3, 'Code kata');
INSERT INTO "activityTypes" (id, name) VALUES (4, 'Workshop');
INSERT INTO "activityTypes" (id, name) VALUES (5, 'Discussion');

INSERT INTO tags (id, name, color, short_name) VALUES (1, 'introductory', 'yellow', 'INTRO');
INSERT INTO tags (id, name, color, short_name) VALUES (2, 'beginner/amateur', 'green', 'BEGINNER');
INSERT INTO tags (id, name, color, short_name) VALUES (3, 'advanced users', 'red', 'ADVANCE');

INSERT INTO "activityTags" (id, tag_id, activity_id) VALUES (1, 1, 1);
INSERT INTO "activityTags" (id, tag_id, activity_id) VALUES (2, 2, 1);
INSERT INTO "activityTags" (id, tag_id, activity_id) VALUES (3, 3, 1);

INSERT INTO atendees (id, activity_id, user_id) VALUES (1, 1 , 2);
INSERT INTO speakers (id, activity_id, user_id) VALUES (1, 1 , 1);

INSERT INTO companies(id, email, phone, description, website, logo_url) VALUES (1, 'rrhh@commodityvectors.com', '+341234567', 'We do some cool stuff', 'www.commodityvectors.com', 'commodityvectors.com/logo.jpg');

INSERT INTO sponsors (id, company_id, event_id) VALUES (1, 1, 1);

