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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diluka
 */
public class DefaultConfigBeanFactory {

    public String configFileLocation = "/staticizer-config.properties";

    public static final String COUNT = "count";

    public static final String MATCHER = "matcher";
    public static final String PARAMETER = "parameter";
    public static final String DIRECTORY = "directory";
    public static final String HOST = "host";
    public static final String PORT = "port";

    public DefaultConfigBeanFactory() {
    }

    public DefaultConfigBeanFactory(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }

    public void setConfigFileLocation(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }

    public List<IStaticizerConfigBean> getDefaultConfigBeans() {
        List<IStaticizerConfigBean> list = new ArrayList<IStaticizerConfigBean>();
        try {
            InputStream is = DefaultConfigBeanFactory.class.getResourceAsStream(configFileLocation);
            Properties prop = new Properties();
            prop.load(is);

            int count = Integer.parseInt(prop.getProperty(COUNT, "0"));

            if (count > 0) {
                for (int i = 1; i <= count; i++) {
                    list.add(new DefaultConfigBean(
                            prop.getProperty(MATCHER + i),
                            prop.getProperty(PARAMETER + i),
                            prop.getProperty(DIRECTORY + i)
                    ));
                }
            } else {
                list.add(new DefaultConfigBean(
                        prop.getProperty(MATCHER),
                        prop.getProperty(PARAMETER),
                        prop.getProperty(DIRECTORY)
                ));
            }

        } catch (IOException ex) {
            Logger.getLogger(DefaultConfigBeanFactory.class.getName()).log(Level.FINEST, "properties file not found", ex);
            Logger.getLogger(DefaultConfigBeanFactory.class.getName()).log(Level.INFO, "properties file not found");
        }
        return list;
    }
}
