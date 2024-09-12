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

package cc.niushuai.datacreator.biz.system.user.entity;

import cc.niushuai.datacreator.base.entity.BaseEntity;
import cc.niushuai.datacreator.common.valid.CreateValid;
import cc.niushuai.datacreator.common.valid.UpdateValid;
import com.fasterxml.jackson.annotation.JsonView;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys_user
 *
 * @author niushuai233
 * @date 2024/09/03 17:19
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_user")
public class User extends BaseEntity {

    @NotBlank(message = "用户名不能为空")
    @JsonView({CreateValid.class, UpdateValid.class})
    private String username;
    @NotBlank(message = "密码不能给空")
    @JsonView({CreateValid.class})
    private String password;
    @JsonView({CreateValid.class, UpdateValid.class})
    private Integer status;
}
