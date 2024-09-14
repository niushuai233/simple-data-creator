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

package cc.niushuai.datacreator.biz.system.login;

import cc.niushuai.datacreator.base.R;
import cc.niushuai.datacreator.biz.system.login.entity.LoginResult;
import cc.niushuai.datacreator.biz.system.login.entity.LoginVO;
import cc.niushuai.datacreator.biz.system.user.entity.User;
import cc.niushuai.datacreator.biz.system.user.service.UserService;
import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cc.niushuai.datacreator.common.exception.BizException;
import cc.niushuai.datacreator.common.valid.extra.Valid1;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登入登出控制器
 *
 * @author niushuai233
 * @date 2024/09/12 10:47
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/system")
@Validated
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     *
     * @param login 登陆参数
     * @return
     * @author niushuai233
     * @date 2024/09/12 10:54
     * @since 0.0.1
     */
    @PostMapping("/login")
    public R login(@JsonView(Valid1.class) @RequestBody LoginVO login) {

        User user = userService.queryChain()
                .eq(User::getUsername, login.getUsername())
                .oneOpt()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCodeEnum.AUTH_LoginError));

        if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {

            StpUtil.login(login.getUsername());

            return R.success(new LoginResult(user.getId(), user.getUsername(), StpUtil.getTokenValue()));
        }

        return R.error(ErrorCodeEnum.AUTH_LoginError);
    }

    /**
     * 注销登陆
     *
     * @return
     * @author niushuai233
     * @date 2024/09/13 10:36
     * @since 0.0.1
     */
    @RequestMapping("/logout")
    public R logout() {
        StpUtil.logout();
        return R.success();
    }

}
