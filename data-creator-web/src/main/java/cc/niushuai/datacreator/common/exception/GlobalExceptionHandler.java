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


package cc.niushuai.datacreator.common.exception;

import cc.niushuai.datacreator.base.R;
import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.exception.MybatisFlexException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final String SQL_EX_TEMPLATE = "exception: {}, SQL: {}";

    public static String getSqlFromException(String message) {
        try {
            String SQL_SEG = "### SQL:";
            String CAUSE_SEG = "### Cause:";
            return message.substring(message.indexOf(SQL_SEG) + SQL_SEG.length(), message.indexOf(CAUSE_SEG));
        } catch (Exception exception) {
            log.error("解析SQL异常: {}", exception.getMessage(), exception);
            return message;
        }
    }

    /**
     * 兜底的异常处理机制
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/10 16:51
     * @since 0.0.1
     */
    @ExceptionHandler(Exception.class)
    public R exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.InterServerException, exception.getMessage());
    }

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
     * satoken checklogin throw
     *
     * @param notLoginException
     * @return
     * @author niushuai233
     * @date 2024/09/13 9:59
     * @since 0.0.1
     */
    @ExceptionHandler(NotLoginException.class)
    public R notLoginException(NotLoginException notLoginException) {
        log.error(notLoginException.getMessage(), notLoginException);
        return R.error(ErrorCodeEnum.AUTH_NotLogin);
    }

    /**
     * sql语法异常
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/10 17:43
     * @since 0.0.1
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public R sqlGrammarException(BadSqlGrammarException exception) {
        log.error("SQL执行异常", exception);

        // split目的是为了兼容h2数据库重复出现sql问题 mysql不会再message中存在sql h2则会 其他不详
        String[] messages = exception.getSQLException().getMessage().split(";");
        return R.error(StrUtil.format(SQL_EX_TEMPLATE, messages[0], getSqlFromException(exception.getMessage())));
    }


    /**
     * key重复异常捕捉处理
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/10 17:43
     * @since 0.0.1
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public R DuplicateKeyExceptionHandler(DuplicateKeyException exception) {
        log.error(exception.getMessage(), exception);

        Throwable throwable = matchException(exception, SQLIntegrityConstraintViolationException.class);

        return R.error(ErrorCodeEnum.DB_DuplicateKey, throwable.getMessage());
    }

    /**
     * 递归查询出异常堆栈中想要的那个
     *
     * @param sourceException 源异常
     * @param targetException 匹配异常
     * @return
     * @author niushuai233
     * @date 2024/09/10 17:43
     * @since 0.0.1
     */
    private Throwable matchException(Throwable sourceException, Class<? extends Throwable> targetException) {

        if (targetException.isAssignableFrom(sourceException.getClass())) {
            return sourceException;
        }

        Throwable cause = sourceException.getCause();
        if (targetException.isAssignableFrom(cause.getClass())) {
            return cause;
        }

        return matchException(cause, targetException);
    }


    /**
     * mybatis异常
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/10 17:58
     * @since 0.0.1
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public R myBatisSystemException(MyBatisSystemException exception) {
        log.error(exception.getCause().getMessage(), exception);

        return R.error(deepestCauseMessage(exception));
    }


    /**
     * 防止mp中的异常无法抛出
     *
     * @param exception
     * @author niushuai
     */
    private BizException deepestCauseMessage(Exception exception) {
        // 保存原始信息
        String sourceMessage = exception.getMessage();
        // 计数器 超过一定次数也不再继续循环下去
        int count = 0;

        Throwable throwable = exception;
        while (true) {
            // 非BizException才走到这里来 因此最外层异常可以不做判断 必不可能为BizException
            // 除非人为
            Throwable tmp = throwable.getCause();
            // 第一次获取cause即为空 直接返回原异常message
            // 太深了, 不找了 直接返回原异常message
            if (10 == count++) {
                return new BizException(sourceMessage);
            }

            // 查看每一层数据是否匹配BizException 匹配直接返回
            if (tmp instanceof BizException) {
                return (BizException) tmp;
            }

            // 如果未匹配到BizException 且没有最后一层了 判定是否为MybatisFlexException
            if (null == tmp) {
                if (throwable instanceof MybatisFlexException) {
                    return new BizException(ErrorCodeEnum.DB_MybatisFlexException2, throwable.getMessage());
                }
            }

            // 重置throwable引用对象为tmp对象
            throwable = tmp;
        }
    }

    @ExceptionHandler(MybatisFlexException.class)
    public R mybatisFlexException(MybatisFlexException exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.DB_MybatisFlexException1, exception.getMessage());
    }

    /**
     * sql异常处理器
     *
     * @param sqlException
     * @author niushuai
     * @date: 2022/4/13 9:35
     * @return: {@link R}
     */
    @ExceptionHandler(value = {SQLException.class})
    public R sqlExceptionHandler(SQLException sqlException) {
        log.error(sqlException.getMessage(), sqlException);
        return R.error(ErrorCodeEnum.DB_SqlException, sqlException.getMessage());
    }

    /**
     * valid校验异常
     *
     * @param exception
     * @return
     * @author niushuai233
     * @date 2024/09/11 14:16
     * @since 0.0.1
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R constraintViolationException(ConstraintViolationException exception) {

        String message = exception.getConstraintViolations().stream().map(item -> item.getMessage())
                .collect(Collectors.joining(";"));

        log.error(message, exception);
        return R.error(ErrorCodeEnum.WEB_VALID_EXCEPTION, message);
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
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public R missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error(exception.getMessage(), exception);
        return R.error(ErrorCodeEnum.WEB_RequestParameterError, exception.getMessage());
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
        return R.error(ErrorCodeEnum.WEB_IllegalArgument, exception.getMessage());
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
        return R.error(ErrorCodeEnum.WEB_HttpMethodNotAllowed);
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
