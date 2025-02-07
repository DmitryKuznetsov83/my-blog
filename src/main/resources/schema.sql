create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  body varchar(1024) not null,
  short_body varchar(1024) not null
);