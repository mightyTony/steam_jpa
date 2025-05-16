create table if not exists mysteam.game
(
    discount         int                                 not null,
    dislike_count    int                                 not null,
    like_count       int                                 not null,
    on_sale          tinyint(1)                          not null,
    price            int                                 not null,
    sales            int                                 not null,
    total_price      int                                 not null,
    created_at       timestamp default CURRENT_TIMESTAMP null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    developer        varchar(50)                         not null,
    publisher        varchar(50)                         not null,
    name             varchar(100)                        not null,
    content          text                                not null,
    picture_url      varchar(255)                        null,
    release_date     varchar(255)                        not null,
    constraint UKjare70vqqti665ds3b2eh7rk8
        unique (name)
);

create table if not exists mysteam.game_genre
(
    game_id    bigint       null,
    id         bigint auto_increment
        primary key,
    genre_name varchar(255) null,
    constraint FKj47t9lfhtj14lsg346bo3vujv
        foreign key (game_id) references mysteam.game (id)
);

create table if not exists mysteam.users
(
    deleted           bit                                 not null,
    created_at        timestamp default CURRENT_TIMESTAMP null,
    id                bigint auto_increment
        primary key,
    last_modified_at  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    email             varchar(255)                        null,
    nickname          varchar(255)                        null,
    password          varchar(255)                        null,
    profile_image_url varchar(255)                        null,
    user_name         varchar(255)                        null,
    role              enum ('ADMIN', 'USER')              null,
    constraint UK2ty1xmrrgtn89xt7kyxx6ta7h
        unique (nickname),
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UKk8d0f2n7n88w1a16yhua64onx
        unique (user_name)
);

create table if not exists mysteam.cart
(
    game_id bigint not null,
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    constraint UKffj5acmoq86esmtl8p39wxl6k
        unique (user_id, game_id),
    constraint FKg5r4nv4v89vl7hpfibbovu6v5
        foreign key (game_id) references mysteam.game (id),
    constraint FKg5uhi8vpsuy0lgloxk2h4w5o6
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.friendship
(
    created_at       timestamp default CURRENT_TIMESTAMP      null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP      null on update CURRENT_TIMESTAMP,
    receiver_id      bigint                                   not null,
    sender_id        bigint                                   not null,
    status           enum ('ACCEPTED', 'PENDING', 'REJECTED') null,
    constraint UKphs9e9eny7tr069j58tcw8qx3
        unique (sender_id, receiver_id),
    constraint FK5kuaxbwscap950h164t23sgby
        foreign key (receiver_id) references mysteam.users (id),
    constraint FKljirh3vtab8kelivnofrfyueo
        foreign key (sender_id) references mysteam.users (id)
);

create table if not exists mysteam.my_game
(
    created_at       timestamp default CURRENT_TIMESTAMP null,
    game_id          bigint                              not null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    user_id          bigint                              not null,
    constraint FK4h8e557ragn8wn8ow00hebreg
        foreign key (game_id) references mysteam.game (id),
    constraint FKpa4a3wap02f6ahu5634wfoqsr
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.orders
(
    total_price      int                                               not null,
    created_at       timestamp default CURRENT_TIMESTAMP               null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP               null on update CURRENT_TIMESTAMP,
    user_id          bigint                                            not null,
    tid              varchar(255)                                      null,
    status           enum ('CANCELED', 'COMPLETED', 'FAILED', 'READY') not null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.order_item
(
    price    int    not null,
    game_id  bigint not null,
    id       bigint auto_increment
        primary key,
    order_id bigint not null,
    constraint FK9tokeo13v94je3hkblb92jyr1
        foreign key (game_id) references mysteam.game (id),
    constraint FKt4dc2r9nbvbujrljv3e23iibt
        foreign key (order_id) references mysteam.orders (id)
);

create table if not exists mysteam.profile
(
    created_at       timestamp default CURRENT_TIMESTAMP null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    user_id          bigint                              not null,
    content          text                                null,
    constraint UKc1dkiawnlj6uoe6fnlwd6j83j
        unique (user_id),
    constraint FKs14jvsf9tqrcnly0afsv0ngwv
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.comment
(
    id               bigint auto_increment
        primary key,
    created_at       timestamp default CURRENT_TIMESTAMP null,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    content          varchar(255)                        not null,
    profile_id       bigint                              not null,
    writer_user_id   bigint                              null,
    constraint FKa926jdw9ofp44fheygercoe4n
        foreign key (profile_id) references mysteam.profile (id),
    constraint FKr3686cl45n8dyobltvswr4r9t
        foreign key (writer_user_id) references mysteam.users (id)
);

create table if not exists mysteam.review
(
    deleted          bit                                 not null,
    recommend        bit                                 not null,
    created_at       timestamp default CURRENT_TIMESTAMP null,
    game_id          bigint                              not null,
    id               bigint auto_increment
        primary key,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    user_id          bigint                              not null,
    content          varchar(1000)                       not null,
    constraint UK463q1l0jth5fu5ylg04whbdf3
        unique (user_id, game_id),
    constraint FK6cpw2nlklblpvc7hyt7ko6v3e
        foreign key (user_id) references mysteam.users (id),
    constraint FKqqxxwdsxhhxc7t2ho2acg2m4p
        foreign key (game_id) references mysteam.game (id)
);

create table if not exists mysteam.review_comment
(
    deleted           bit                                 not null,
    created_at        timestamp default CURRENT_TIMESTAMP null,
    id                bigint auto_increment
        primary key,
    last_modified_at  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    parent_comment_id bigint                              null,
    review_id         bigint                              not null,
    user_id           bigint                              not null,
    content           text                                not null,
    constraint FK9j7pkcpuestrwjre1a1902biu
        foreign key (review_id) references mysteam.review (id),
    constraint FKmh0o5lpwf7uf1b318udd89vh4
        foreign key (parent_comment_id) references mysteam.review_comment (id),
    constraint FKpgr2q1cxlrcy0i08wjp10lke3
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.review_like
(
    id        bigint auto_increment
        primary key,
    review_id bigint not null,
    user_id   bigint not null,
    constraint UKkv2edtmavhobw9aeu18khjer5
        unique (user_id, review_id),
    constraint FK68am9vk1s1e8n1v873meqkk0k
        foreign key (review_id) references mysteam.review (id),
    constraint FKkvtrymuejm49631rif0aasg5e
        foreign key (user_id) references mysteam.users (id)
);

create table if not exists mysteam.wish
(
    game_id bigint not null,
    id      bigint auto_increment
        primary key,
    user_id bigint not null,
    constraint FKi7qgb8omr3fmf1fsn3k32ilwv
        foreign key (game_id) references mysteam.game (id),
    constraint FKlno18nhiscr74f8ccpsvajias
        foreign key (user_id) references mysteam.users (id)
);

# user
CREATE UNIQUE INDEX idx_users_username ON users (user_name);
CREATE UNIQUE INDEX idx_users_nickname ON users (nickname);
CREATE UNIQUE INDEX idx_users_username_nickname ON users (user_name, nickname);

# game
CREATE INDEX idx_game_name ON game (name);
CREATE INDEX idx_game_developer ON game (developer);
CREATE INDEX idx_game_price ON game (price);
CREATE INDEX idx_game_sales ON game (sales DESC);
CREATE INDEX idx_game_created_at ON game (created_at DESC);
CREATE INDEX idx_game_discount ON game (discount DESC);

# game_genre
CREATE INDEX idx_game_genre_name ON game_genre (genre_name);

#review
CREATE INDEX idx_review_user_game ON review (user_id, game_id);

#review_comment
CREATE INDEX idx_review_comment_review_id ON review_comment (review_id);
CREATE INDEX idx_review_comment_user_id ON review_comment (user_id);
CREATE INDEX idx_review_comment_parent_id ON review_comment (parent_comment_id);

#review_like
CREATE UNIQUE INDEX idx_review_like_user_review ON review_like (user_id, review_id);

#friendship
CREATE UNIQUE INDEX idx_friendship_sender_receiver ON friendship (sender_id, receiver_id);
CREATE INDEX idx_friendship_receiver_status ON friendship (receiver_id, status);
CREATE INDEX idx_friendship_sender_status ON friendship (sender_id, status);

#profile
CREATE INDEX idx_profile_user_id ON profile (user_id);

#mygame
CREATE INDEX idx_my_game_user_game ON my_game (user_id, game_id);

#order
CREATE INDEX idx_orders_user_id_created_at ON orders (user_id, created_at);

#orderitem
CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_order_item_game_id ON order_item (game_id);

#cart
#CREATE UNIQUE INDEX idx_cart_user_game ON cart (user_id, game_id);

#notification
CREATE INDEX idx_notification_user_id_read ON notification (user_id, is_read);
