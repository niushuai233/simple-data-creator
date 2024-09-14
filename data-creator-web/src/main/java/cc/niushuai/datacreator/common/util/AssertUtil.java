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

package cc.niushuai.datacreator.common.util;

import cc.niushuai.datacreator.common.exception.BizException;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Map;

/**
 * @author niushuai233
 * @date 2023/1/12 13:48
 * @since 0.0.1
 */
public class AssertUtil {

    private static final Object[] NULL_OBJECTS = new Object[0];


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // isTrue  Start
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 为假时抛出自定义异常
     *
     * @param expression 表达式
     * @param exMsg      错误信息
     * @return
     * @author niushuai233
     * @date 2023/1/12 14:12
     * @since 0.0.1
     */
    public static void isTrue(boolean expression, String exMsg) {
        isTrue(expression, exMsg, NULL_OBJECTS);
    }

    /**
     * 为假时抛出自定义异常
     *
     * @param expression 表达式
     * @param exMsg      错误信息
     * @param params     异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/12 14:12
     * @since 0.0.1
     */
    public static void isTrue(boolean expression, String exMsg, Object... params) {
        isTrue(true, expression, exMsg, params);
    }

    /**
     * 为假时抛出自定义异常
     *
     * @param condition  是否校验 expression
     * @param expression 表达式
     * @param exMsg      错误信息
     * @param params     异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/12 14:12
     * @since 0.0.1
     */
    public static void isTrue(boolean condition, boolean expression, String exMsg, Object... params) {
        isTrue(condition, expression, new BizException(StrUtil.format(exMsg, params)));
    }

    /**
     * 为假时抛出自定义异常
     *
     * @param expression 表达式
     * @param e          指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/12 14:12
     * @since 0.0.1
     */
    public static void isTrue(boolean expression, RuntimeException e) {
        isTrue(true, expression, e);
    }

    /**
     * 为假时抛出自定义异常
     *
     * @param condition  是否校验 expression
     * @param expression 表达式
     * @param e          指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/12 14:12
     * @since 0.0.1
     */
    public static void isTrue(boolean condition, boolean expression, RuntimeException e) {
        // 是否继续对expression进行校验
        if (condition) {
            if (expression) {
                return;
            }
            // expression为false throw e
            throw e;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // isTrue  Over
    // notEmpty Start ==> 单一对象
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 校验对象是否非空
     *
     * @param obj   待校验对象
     * @param exMsg 抛出异常信息
     * @return
     * @author niushuai233
     * @date 2023/1/13 14:18
     * @since 0.0.1
     */
    public static <T> void notEmpty(T obj, String exMsg) {
        notEmpty(true, obj, exMsg);
    }

    /**
     * 校验对象是否非空
     *
     * @param obj    待校验对象
     * @param exMsg  抛出异常信息
     * @param params 可对exMsg参数格式化
     * @return
     * @author niushuai233
     * @date 2023/2/13 14:44
     * @since 0.0.1
     */
    public static <T> void notEmpty(T obj, String exMsg, Object... params) {
        notEmpty(true, obj, exMsg, params);
    }

    /**
     * 校验对象是否非空
     *
     * @param condition 是否校验的条件
     * @param obj       待校验对象
     * @param exMsg     抛出异常信息
     * @return
     * @author niushuai233
     * @date 2023/1/13 14:19
     * @since 0.0.1
     */
    public static <T> void notEmpty(boolean condition, T obj, String exMsg) {
        notEmpty(condition, obj, exMsg, NULL_OBJECTS);
    }

    /**
     * 校验对象是否非空
     *
     * @param condition 是否校验的条件
     * @param obj       待校验对象
     * @param exMsg     抛出异常信息
     * @param params    可对exMsg参数格式化
     * @return
     * @author niushuai233
     * @date 2023/1/13 14:19
     * @since 0.0.1
     */
    public static <T> void notEmpty(boolean condition, T obj, String exMsg, Object... params) {
        boolean expression = false;
        if (obj instanceof String) {
            // 字符串类型 要求非null 且长度大于0
            expression = !(null == obj || ((String) obj).length() == 0);
        } else {
            expression = !(null == obj);
        }
        isTrue(condition, expression, exMsg, params);
    }

    /**
     * 校验对象是否非空
     *
     * @param obj 待校验对象
     * @param e   指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/13 14:19
     * @since 0.0.1
     */
    public static <T> void notEmpty(T obj, RuntimeException e) {
        notEmpty(true, obj, e);
    }

    /**
     * 校验对象是否非空
     *
     * @param condition 是否校验的条件
     * @param obj       待校验对象
     * @param e         指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/13 14:19
     * @since 0.0.1
     */
    public static <T> void notEmpty(boolean condition, T obj, RuntimeException e) {
        boolean expression = false;
        if (obj instanceof String) {
            // 字符串类型 要求非null 且长度大于0
            expression = !(null == obj || ((String) obj).length() == 0);
        } else {
            expression = !(null == obj);
        }
        isTrue(condition, expression, e);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // notEmpty Over ==> 单一对象
    // notEmpty Start ==> Collection对象
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 是否非空
     *
     * @param collection 集合
     * @param exMsg      异常信息
     * @return
     * @author niushuai233
     * @date 2023/1/12 15:52
     * @since 0.0.1
     */
    public static <T> void notEmpty(Collection<T> collection, String exMsg) {
        notEmpty(collection, exMsg, NULL_OBJECTS);
    }

    /**
     * 是否非空
     *
     * @param collection 集合
     * @param exMsg      异常信息
     * @param params     异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/12 15:52
     * @since 0.0.1
     */
    public static <T> void notEmpty(Collection<T> collection, String exMsg, Object... params) {
        notEmpty(true, collection, exMsg, params);
    }

    /**
     * 是否非空
     *
     * @param condition  继续校验条件
     * @param collection 集合
     * @param exMsg      异常信息
     * @param params     异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/12 15:52
     * @since 0.0.1
     */
    public static <T> void notEmpty(boolean condition, Collection<T> collection, String exMsg, Object... params) {
        isTrue(condition, !(collection == null || collection.isEmpty()), exMsg, params);
    }

    /**
     * 是否非空
     *
     * @param collection 集合
     * @param e          指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/12 15:52
     * @since 0.0.1
     */
    public static <T> void notEmpty(Collection<T> collection, RuntimeException e) {
        isTrue(true, !(collection == null || collection.isEmpty()), e);
    }

    /**
     * 是否非空
     *
     * @param condition  继续校验条件
     * @param collection 集合
     * @param e          指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/12 15:52
     * @since 0.0.1
     */
    public static <T> void notEmpty(boolean condition, Collection<T> collection, RuntimeException e) {
        isTrue(condition, !(collection == null || collection.isEmpty()), e);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // notEmpty Over ==> Collection对象
    // notEmpty Start ==> Map对象
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 校验map是否为空
     *
     * @param map   待校验map
     * @param exMsg 异常信息
     * @return
     * @author niushuai233
     * @date 2023/1/13 17:18
     * @since 0.0.1
     */
    public static <K, V> void notEmpty(Map<K, V> map, String exMsg) {
        notEmpty(map, exMsg, NULL_OBJECTS);
    }

    /**
     * 校验map是否为空
     *
     * @param map    待校验map
     * @param exMsg  异常信息
     * @param params 异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/13 17:18
     * @since 0.0.1
     */
    public static <K, V> void notEmpty(Map<K, V> map, String exMsg, Object... params) {
        notEmpty(true, map, exMsg, params);
    }

    /**
     * 校验map是否为空
     *
     * @param condition 是否校验调教
     * @param map       待校验map
     * @param exMsg     异常信息
     * @param params    异常信息参数, 用于格式化
     * @return
     * @author niushuai233
     * @date 2023/1/13 17:18
     * @since 0.0.1
     */
    public static <K, V> void notEmpty(boolean condition, Map<K, V> map, String exMsg, Object... params) {
        isTrue(condition, !(map == null || map.isEmpty()), exMsg, params);
    }

    /**
     * 校验map是否为空
     *
     * @param map 待校验map
     * @param e   指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/13 17:18
     * @since 0.0.1
     */
    public static <K, V> void notEmpty(Map<K, V> map, RuntimeException e) {
        notEmpty(true, map, e);
    }

    /**
     * 校验map是否为空
     *
     * @param condition 是否校验调教
     * @param map       待校验map
     * @param e         指定抛出的异常, 要求为{@link RuntimeException}子类
     * @return
     * @author niushuai233
     * @date 2023/1/13 17:18
     * @since 0.0.1
     */
    public static <K, V> void notEmpty(boolean condition, Map<K, V> map, RuntimeException e) {
        isTrue(condition, !(map == null || map.isEmpty()), e);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // notEmpty Over ==> Map对象
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
