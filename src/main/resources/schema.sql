create table "users"
("id" SERIAL NOT NULL PRIMARY KEY,
"email" VARCHAR(254) NOT NULL,
"first_name" VARCHAR(254),
"last_name" VARCHAR(254),
"twitter_id" VARCHAR(254),
"linkedin_id" VARCHAR(254),
"bio" VARCHAR(254),
"permission" VARCHAR(254),
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

create table "passwords"
("id" SERIAL NOT NULL PRIMARY KEY,
"hashed_password" VARCHAR(254),
"salt" VARCHAR(254));

-- User: test1@test.com, Password: password1
INSERT INTO passwords(
id, hashed_password, salt)
VALUES (1,'$2a$10$U3gBQ50FY5qiQ5XeQKgWwO6AADKjaGqh/6l3RzWitAWelWCQxffUC', '$2a$10$U3gBQ50FY5qiQ5XeQKgWwO');

INSERT INTO users(
id, email, first_name, last_name, twitter_id, linkedin_id, bio,  password_id)
VALUES (1, 'giancarlo@pfc.com', 'Giancarlo', 'Munoz', 'gneotux', 'giancarlo_munoz', 'Im just a developer...',1);

INSERT INTO activities(
id, event_id, location_id, activity_type_id, title, description, objective, start_time, end_time, resources
) VALUES (1, 1, 1, 1, 'Test activity', 'This is a test for the events API', 'Create new apis', TIMESTAMP '2015-07-16 15:00:00', TIMESTAMP '2015-07-19 21:00:00', 'github/gneotux');