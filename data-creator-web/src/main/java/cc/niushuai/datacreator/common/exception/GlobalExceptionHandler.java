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

package cc.niushuai.datacreator.common.exception;

import cc.niushuai.datacreator.base.R;
import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cn.hutool.core.text.StrPool;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;

/**
 * 全局异常处理器
 *
 * @author niushuai233
 * @date 2024/09/04 9:35
 * @since 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 业务异常处理接口
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/04 9:48
     * @since 0.0.1
     */
    @ExceptionHandler(BizException.class)
    public R bizException(BizException exception) {
        log.error(exception.getMessage(), exception);
        return R.error(exception.getCode(), exception.getMessage());
    }


    /**
     * sql异常处理器
     *
     * @param sqlException
     * @author niushuai
     * @date: 2022/4/13 9:35
     * @return: {@link R}
     */
    @ExceptionHandler(SQLException.class)
    public R sqlExceptionHandler(SQLException sqlException) {
        log.error(sqlException.getMessage(), sqlException);
        return R.error(ErrorCodeEnum.SqlException, sqlException.getMessage());
    }


    /**
     * 必填参数缺失
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/04 9:58
     * @since 0.0.1
     */
    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public R missingServletRequestParameterException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.RequestParameterError, exception.getMessage());
    }


    /**
     * 非法参数错误
     *
     * @param exception
     * @author niushuai
     * @date: 2022/4/13 9:35
     * @return: {@link R}
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public R illegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.IllegalArgument, exception.getMessage());
    }

    /**
     * 请求方式不匹配异常处理
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/04 9:55
     * @since 0.0.1
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.HttpMethodNotAllowed);
    }

    /**
     * 字段校验异常处理
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/04 9:57
     * @since 0.0.1
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public R methodArgumentNotValidException(Exception exception) {
        log.error(exception.getMessage(), exception);

        BindException bindException = (BindException) exception;

        List<FieldError> fieldErrors = bindException.getFieldErrors();

        StringBuffer buffer = new StringBuffer();
        fieldErrors.forEach(item -> buffer.append(StrPool.BRACKET_START).append(item.getField()).append(StrPool.BRACKET_END)
                .append(item.getDefaultMessage()).append(StrPool.BRACKET_END).append(StrPool.COMMA));

        return R.error(buffer.substring(0, buffer.length() - 1));
    }

    @PostConstruct
    public void init() {
        log.info("Init Global Exception Handler Success.");
    }

}
