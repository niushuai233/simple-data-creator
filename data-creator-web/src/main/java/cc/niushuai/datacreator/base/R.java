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

package cc.niushuai.datacreator.base;

import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cc.niushuai.datacreator.common.exception.BizException;
import cc.niushuai.datacreator.common.util.TtlContext;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * 统一返回处理
 *
 * @author niushuai233
 * @date 2024/09/03 16:53
 * @since 0.0.1
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 3123225289302015397L;
    private Integer code;
    private T data;
    private String message;
    private String traceId = TtlContext.nullableGetTraceId();

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static R success() {
        return new R(20000, "success");
    }

    public static R success(String message) {
        return new R(20000, message);
    }

    public static <T> R success(T data) {
        return new R<T>(20000, data, "success");
    }

    public static R success(Integer code, String message) {
        return new R(code, message);
    }

    public static R error() {
        return new R(50000, "error");
    }

    public static R error(String message) {
        return new R(50000, message);
    }

    public static <T> R error(T data) {
        return new R<T>(50000, data, "error");
    }

    public static R error(ErrorCodeEnum errorCodeEnum) {
        return error(errorCodeEnum, "");
    }

    public static R error(ErrorCodeEnum errorCodeEnum, String extraMessage) {
        String msg = errorCodeEnum.getMessage();
        if (StrUtil.isNotEmpty(extraMessage)) {
            msg = msg + ": " + extraMessage;
        }
        return error(errorCodeEnum.getCode(), msg);
    }

    public static R error(BizException bizException) {
        return error(bizException.getCode(), bizException.getMessage());
    }

    public static R error(Integer code, String message) {
        return new R(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getTraceId() {
        return traceId;
    }
}
