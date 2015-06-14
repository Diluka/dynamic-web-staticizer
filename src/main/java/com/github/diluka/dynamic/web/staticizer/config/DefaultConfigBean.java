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

/**
 * 默认配置Bean
 *
 * @author Diluka
 */
public class DefaultConfigBean implements IStaticizerConfigBean {

    private String URIPattern;
    private String parameterName;
    private String staticPageDirectory;
    private String staticFlag;

    public DefaultConfigBean() {
    }

    public DefaultConfigBean(String matcher, String param, String dir, String flag) {
        this.URIPattern = matcher;
        this.parameterName = param;
        this.staticPageDirectory = dir;
        this.staticFlag = flag;
    }

    @Override
    public String getURIPattern() {
        return URIPattern;
    }

    public void setURIPattern(String URIPattern) {
        this.URIPattern = URIPattern;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String getStaticPageDirectory() {
        return staticPageDirectory;
    }

    public void setStaticPageDirectory(String staticPageDirectory) {
        this.staticPageDirectory = staticPageDirectory;
    }

    @Override
    public String getStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(String staticFlag) {
        this.staticFlag = staticFlag;
    }

}
