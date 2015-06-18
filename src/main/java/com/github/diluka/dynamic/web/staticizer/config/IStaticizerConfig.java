/*
 * Copyright 2015 Diluka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.diluka.dynamic.web.staticizer.config;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置Bean接口
 *
 * @author Diluka
 */
public interface IStaticizerConfig {

    /**
     * URI正则表达式
     *
     * @return
     */
    String getURIPattern();

    /**
     * 唯一参数名称
     *
     * @return
     */
    String getParameterName();

    /**
     * 静态文件存放相对路径
     *
     * @return
     */
    String getStaticPageDirectory();

    /**
     * 静态化标记，GET参数中有这个标记才处理
     *
     * @return
     */
    String getStaticFlag();

    /**
     * 通过请求获取文件名
     * @param request
     * @return
     */
    String getFilename(HttpServletRequest request);

}
