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

package cc.niushuai.datacreator.biz.system.login.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 登陆成功返回结果
 *
 * @author niushuai233
 * @date 2024/09/13 9:47
 * @since 0.0.1
 */
@Data
public class LoginResult implements Serializable {

    private static final long serialVersionUID = -8302382626150713374L;

    private Long id;
    private String username;
    private String token;

    public LoginResult(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }
}
