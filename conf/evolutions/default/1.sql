# --- !Ups

create table "notes" ("id" text PRIMARY KEY, "contents" text NOT NULL)

# --- !Downs

drop table "notes";
