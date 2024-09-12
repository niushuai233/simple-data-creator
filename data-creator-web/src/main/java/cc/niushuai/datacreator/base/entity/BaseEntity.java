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

package cc.niushuai.datacreator.base.entity;

import cc.niushuai.datacreator.common.valid.UpdateValid;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * entity 基类
 *
 * @author niushuai233
 * @date 2024/09/03 17:23
 * @since 0.0.1
 */
@Data
public class BaseEntity {

    /**
     * 主键id 数据库自增
     */
    @Id(keyType = KeyType.Auto)
    @JsonView({UpdateValid.class})
    private Long id;

    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    private Integer deleted;


    /**
     * 创建人
     */
    @Column(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(value = "update_time")
    private Date updateTime;
}
