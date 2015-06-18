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

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 默认配置Bean工厂，从属性文件加载配置
 *
 * @author Diluka
 */
@Deprecated
public class DefaultConfigFactory {

    public String configFileLocation = "/staticizer-config.json";

//    public static final String COUNT = "count";
//
//    public static final String MATCHER = "matcher";
//    public static final String PARAMETER = "parameter";
//    public static final String DIRECTORY = "directory";
//    public static final String FLAG = "flag";
//    public static final String HOST = "host";
//    public static final String PORT = "port";

    public DefaultConfigFactory() {
    }

    public DefaultConfigFactory(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }

    public void setConfigFileLocation(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }

    public List<IStaticizerConfig> getDefaultConfigBeans() {
        List<IStaticizerConfig> list = new ArrayList<IStaticizerConfig>();
        try {
            InputStream is = DefaultConfigFactory.class.getResourceAsStream(configFileLocation);
            InputStreamReader reader = new InputStreamReader(is);
            Gson gson = new Gson();

            DefaultConfig[] beans = gson.fromJson(reader, DefaultConfig[].class);

            list.addAll(Arrays.asList(beans));

        } catch (Exception ex) {
            Logger.getLogger(DefaultConfigFactory.class.getName()).log(Level.FINEST, "properties file not found", ex);
            Logger.getLogger(DefaultConfigFactory.class.getName()).log(Level.INFO, "properties file not found");
        }
        return list;
    }
}
