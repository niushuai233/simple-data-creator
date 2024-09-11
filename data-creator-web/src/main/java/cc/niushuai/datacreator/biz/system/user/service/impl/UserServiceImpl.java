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

package cc.niushuai.datacreator.biz.system.user.service.impl;

import cc.niushuai.datacreator.base.service.impl.BaseServiceImpl;
import cc.niushuai.datacreator.biz.system.user.entity.User;
import cc.niushuai.datacreator.biz.system.user.mapper.UserMapper;
import cc.niushuai.datacreator.biz.system.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类
 *
 * @author niushuai233
 * @date 2024/09/03 17:03
 * @since 0.0.1
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User one(User user) {
        User one = mapper.one(user);
        return one;
    }
}
