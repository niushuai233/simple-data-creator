/*
 * Copyright (C) 2019-2024 niushuai233 niushuai.cc
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

drop table if exists sys_user;
create table sys_user
(
    id          int primary key auto_increment comment '主键id',
    username    varchar(50) comment '用户名',
    password    varchar(50) comment '密码',
    status      int       default 1 comment '状态 默认正常',
    deleted     int       default 0 comment '是否删除 默认未删除',
    create_by   varchar(50) comment '创建人',
    create_time timestamp default now() comment '创建时间',
    update_by   varchar(50) comment '更新人',
    update_time timestamp default now() comment '更新时间'
);

drop table if exists dat_database_info;
create table dat_database_info
(
    id           int primary key auto_increment comment '主键id',
    type         int comment '数据库类型',
    driver_class varchar(100) comment '数据库驱动类',
    url          varchar(255) comment '数据库url',
    username     varchar(50) comment '数据库用户名',
    password     varchar(50) comment '数据库密码',
    status       int       default 1 comment '状态 默认正常',
    deleted      int       default 0 comment '是否删除 默认未删除',
    create_by    varchar(50) comment '创建人',
    create_time  timestamp default now() comment '创建时间',
    update_by    varchar(50) comment '更新人',
    update_time  timestamp default now() comment '更新时间'
);