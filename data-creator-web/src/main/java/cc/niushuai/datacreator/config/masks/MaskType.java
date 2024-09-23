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

import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cc.niushuai.datacreator.common.exception.BizException;
import com.mybatisflex.core.mask.MaskProcessor;

/**
 * 自定义脱敏类型
 *
 * @author niushuai233
 * @date 2024/09/23 17:03
 * @since 0.0.1
 */
public interface MaskType {

    String PASSWORD_X = "passwordx";

    static MaskProcessor PASSWORD_X_PROCESSOR = data -> {
        if (data instanceof String) {
            return "********";
        }
        throw new BizException(ErrorCodeEnum.AUTH_LoginError);
    };
}
