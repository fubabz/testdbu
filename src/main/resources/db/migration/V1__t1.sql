DROP TABLE if exists "public"."t1" ;

CREATE TABLE "public"."t1"
(
   id int NOT NULL,
   name varchar(100) NOT NULL,
   age int,
   division varchar(200),
   joined timestamp
)
;

DROP TABLE if exists "public"."t2";

CREATE TABLE "public"."t2"
(
   id int NOT NULL,
   t1_id int,
   name varchar(100)
)
;
