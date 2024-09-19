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

package cc.niushuai.datacreator.config.mybatisflex.listener;

import cc.niushuai.datacreator.base.entity.BaseEntity;
import cc.niushuai.datacreator.common.util.Constants;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

import java.util.Date;

/**
 * 插入或更新时填充公共字段
 *
 * @author niushuai233
 * @date 2024/09/19 14:22
 * @since 0.0.1
 */
public class BaseEntityOnOptListener implements InsertListener, UpdateListener {

    @Override
    public void onInsert(Object entity) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setCreateBy(StpUtil.getLoginId().toString());
            ((BaseEntity) entity).setCreateTime(new Date());
        } else {
            if (ReflectUtil.hasField(entity.getClass(), Constants.CreateBy)) {
                ReflectUtil.setFieldValue(entity, Constants.CreateBy, StpUtil.getLoginId().toString());
            } else if (ReflectUtil.hasField(entity.getClass(), Constants.CreateTime)) {
                ReflectUtil.setFieldValue(entity, Constants.CreateTime, new Date());
            }
        }
    }

    @Override
    public void onUpdate(Object entity) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setUpdateBy(StpUtil.getLoginId().toString());
            ((BaseEntity) entity).setUpdateTime(new Date());
        } else {
            if (ReflectUtil.hasField(entity.getClass(), Constants.CreateBy)) {
                ReflectUtil.setFieldValue(entity, Constants.UpdateBy, StpUtil.getLoginId().toString());
            } else if (ReflectUtil.hasField(entity.getClass(), Constants.UpdateTime)) {
                ReflectUtil.setFieldValue(entity, Constants.UpdateTime, new Date());
            }
        }
    }
}
