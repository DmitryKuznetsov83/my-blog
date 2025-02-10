-- DROP ALL OBJECTS;
--
-- DROP TABLE IF EXISTS posts_like_count;
-- DROP TABLE IF EXISTS comment_counter;
-- DROP TABLE IF EXISTS posts_tag;
-- DROP TABLE IF EXISTS comments;
-- DROP TABLE IF EXISTS tags;
-- DROP TABLE IF EXISTS posts;


create table if not exists posts
(
  id bigserial primary key,
  title varchar(256) not null,
  body varchar(1024) not null,
  short_body varchar(1024) not null,
  tags varchar(256),
  image blob
);

create table if not exists posts_like_count
(
    post_id bigserial primary key,
    count   INT not null,
    CONSTRAINT posts_like_count_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (id)
);

create table if not exists tags
(
    id bigserial primary key,
    name varchar(256) not null
);

create table if not exists post_tag
(
    tag_id  bigserial,
    post_id bigserial,
    CONSTRAINT posts_tag_tag_id_fk FOREIGN KEY (tag_id) REFERENCES tags (id),
    CONSTRAINT posts_tag_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (id),
    PRIMARY KEY (tag_id, post_id)
);

create table if not exists comments
(
    id  bigserial primary key,
    post_id bigserial,
    body varchar(1024) not null,
    CONSTRAINT comments_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (id)
);

create table if not exists comment_counter
(
    post_id bigserial primary key,
    count   INT not null,
    CONSTRAINT comment_counter_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (id)
);