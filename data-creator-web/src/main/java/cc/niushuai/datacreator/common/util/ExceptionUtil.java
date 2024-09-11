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

/**
 * 异常堆栈工具类
 *
 * @author niushuai233
 * @date 2024/09/05 9:52
 * @since 0.0.1
 */
public class ExceptionUtil {

    /**
     * 获取当前调用堆栈数据
     *
     * @return
     * @author niushuai233
     * @date 2024/09/05 9:52
     * @since 0.0.1
     */
    public static String getPrintStackTraceInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StringBuffer buffer = new StringBuffer();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (buffer.length() > 0) {
                buffer.append(System.lineSeparator());
                buffer.append("    ");
            }
            buffer.append(
                    java.text.MessageFormat.format("{0}.{1}({2}:{3})",
                            stackTraceElement.getClassName(),
                            stackTraceElement.getMethodName(),
                            stackTraceElement.getFileName(),
                            stackTraceElement.getLineNumber()));
        }
        return buffer.toString();
    }
}
