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

package cc.niushuai.datacreator.base.service.impl;

import cc.niushuai.datacreator.base.service.BaseService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * service实现类基类 便于后续扩展
 *
 * @author niushuai233
 * @date 2024/09/03 17:29
 * @since 0.0.1
 */
public class BaseServiceImpl<Mapper extends BaseMapper<Entity>, Entity> extends ServiceImpl<Mapper, Entity> implements BaseService<Entity> {

    protected Mapper mapper;

    public Mapper getMapper() {
        return mapper;
    }

    @Lazy
    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

}
