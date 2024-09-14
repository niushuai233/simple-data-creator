/*
 * Copyright (C) 2024 niushuai233 niushuai.cc
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

    InterServerException(50000, "未知异常: {}"),
    AUTH_NotLogin(40001, "登录已失效, 请重新登陆"),
    AUTH_LoginError(40101, "用户名密码错误"),
    AUTH_RePasswordMismatch(40102, "两次密码输入不一致"),
    AUTH_TokenUnauthorized(41001, "未授权操作: {}"),
    WEB_HttpMethodNotAllowed(40005, "请求方式不匹配"),
    WEB_RequestParameterError(53001, "必填参数缺失: {}"),
    WEB_IllegalArgument(53000, "非法参数: {}"),
    WEB_VALID_EXCEPTION(53003, "必填参数校验失败: {}"),
    TTL_CONTENT_NOT_FOUND(53010, "TTL上下文异常: {}"),
    ST_TokenUnknownEx(53020, "未知ST异常: {}"),
    DB_SqlException(55000, "数据库操作失败: {}"),
    DB_DuplicateKey(55003, "主键重复: {}"),
    DB_MybatisFlexException1(55001, "MybatisFlexException: {}"),
    DB_MybatisFlexException2(55002, "ORM Failed: {}"),

    DEFAULT(99999, "default"),
    ;

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
