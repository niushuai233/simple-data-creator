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

package cc.niushuai.datacreator.common.enums;

/**
 * 错误代码枚举
 *
 * @author niushuai233
 * @date 2024/09/04 9:40
 * @since 0.0.1
 */
public enum ErrorCodeEnum {

    /**
     * 请求方式不匹配
     */
    InterServerException(50000, "未知异常: {}"),

    /**
     * 请求方式不匹配
     */
    HttpMethodNotAllowed(40005, "请求方式不匹配"),

    /**
     * 必填参数缺失
     */
    RequestParameterError(13001, "必填参数缺失"),

    /**
     * 非法参数
     */
    IllegalArgument(13000, "非法参数"),

    /**
     * 数据库操作失败
     */
    SqlException(14000, "数据库操作失败"),

    /**
     * 登陆失效
     */
    NotLogin(40001, "登录已失效, 请重新登陆"),

    /**
     * 未授权操作
     */
    TokenUnauthorized(15001, "未授权操作: {}"),

    /**
     * 未知satoken异常
     */
    TokenUnknownEx(15002, "未知ST异常: {}"),

    DEFAULT(99999, "default");

    private Integer code;
    private String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
