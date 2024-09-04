/*
 * Copyright (C) 2023 niushuai233 niushuai.cc
 *
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

package cc.niushuai.datacreator.common.exception;

import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author niushuai233
 * @date 2024/09/04 9:36
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {

    private Integer code;
    private String message;

    public BizException(ErrorCodeEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }

    public BizException(ErrorCodeEnum errorCodeEnum, Object... params) {
        this.code = errorCodeEnum.getCode();
        this.message = StrUtil.format(errorCodeEnum.getMessage(), params);
    }

    public BizException(String message) {
        this.code = ErrorCodeEnum.InterServerException.getCode();
        this.message = message;
    }

    public BizException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(Integer code, String message, Object... params) {
        this.code = code;
        this.message = StrUtil.format(message, params);;
    }

    public static BizException newInstance(ErrorCodeEnum errorCodeEnum, Object... params) {
        return new BizException(errorCodeEnum, params);
    }
}
