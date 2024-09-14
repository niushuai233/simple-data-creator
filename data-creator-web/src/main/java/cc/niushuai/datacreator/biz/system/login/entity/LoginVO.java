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

import cc.niushuai.datacreator.common.valid.extra.Valid1;
import cc.niushuai.datacreator.common.valid.extra.Valid2;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 登陆控制器
 *
 * @author niushuai233
 * @date 2024/09/12 10:49
 * @since 0.0.1
 */
@Data
public class LoginVO {

    @JsonView({Valid1.class, Valid2.class})
    @NotBlank(message = "用户不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @JsonView({Valid1.class, Valid2.class})
    private String password;

    @JsonView(Valid2.class)
    @NotBlank(message = "重复密码不能为空", groups = Valid2.class)
    private String rePassword;

    @JsonView(Valid2.class)
    @NotEmpty(message = "ID不能为空", groups = Valid2.class)
    private Long id;
}
