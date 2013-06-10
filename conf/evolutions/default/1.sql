# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tweet (
  id                        bigint not null,
  text                      varchar(255),
  user_id                   bigint,
  constraint pk_tweet primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_user primary key (id))
;

create sequence tweet_seq;

create sequence user_seq;

alter table tweet add constraint fk_tweet_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_tweet_user_1 on tweet (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists tweet;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists tweet_seq;

drop sequence if exists user_seq;

