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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 默认Decider工厂，效果和以前一样
 *
 * @author Diluka
 */
public class DefaultConfigDeciderFactory {
    public String configFileLocation = "/staticizer-config.json";

    public List<IStaticizeDecider> getDefaultDeciders() {

        List<IStaticizeDecider> list = new ArrayList<IStaticizeDecider>();
        try {
            InputStream is = DefaultConfigDeciderFactory.class.getResourceAsStream(configFileLocation);
            InputStreamReader reader = new InputStreamReader(is);
            Gson gson = new Gson();

            DefaultConfig[] beans = gson.fromJson(reader, DefaultConfig[].class);

            for (DefaultConfig config : beans) {
                list.add(new DefaultConfigDecider(config));
            }

        } catch (Exception ex) {
            Logger.getLogger(DefaultConfigDeciderFactory.class.getName()).log(Level.FINEST, "properties file not found", ex);
            Logger.getLogger(DefaultConfigDeciderFactory.class.getName()).log(Level.INFO, "properties file not found");
        }
        return list;
    }
}
