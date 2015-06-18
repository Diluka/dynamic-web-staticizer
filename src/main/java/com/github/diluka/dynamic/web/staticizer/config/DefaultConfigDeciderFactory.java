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
