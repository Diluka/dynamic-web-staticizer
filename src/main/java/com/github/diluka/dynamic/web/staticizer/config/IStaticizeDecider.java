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
 * 判断器，是否静态化由{@link #decide}方法的返回值决定
 *
 * @author Diluka
 */
public interface IStaticizeDecider extends IStaticizerConfig {
    /**
     * 静态化条件判定
     * @param request HttpServletRequest
     * @return
     */
    boolean decide(HttpServletRequest request);
}
