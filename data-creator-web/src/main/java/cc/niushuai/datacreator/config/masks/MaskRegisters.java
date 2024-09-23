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

package cc.niushuai.datacreator.config.masks;

import com.mybatisflex.core.mask.MaskManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 密码脱敏自定义处理器
 *
 * @author niushuai233
 * @date 2024/09/23 17:00
 * @since 0.0.1
 */
public class MaskRegisters implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 注册自定义策略处理器
        MaskManager.registerMaskProcessor(MaskType.PASSWORD_X, MaskType.PASSWORD_X_PROCESSOR);


    }
}
