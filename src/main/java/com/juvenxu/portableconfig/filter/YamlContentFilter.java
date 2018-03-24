/*
 * @date 2017年03月06日 10:37
 */
package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.ContentFilter;
import com.juvenxu.portableconfig.model.Replace;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 只支持key-value模式
 * 如要替换 aa.bb.cc: 123  则配置key为aa.bb.cc value为需要的值
 * //todo 支持数组等高级语法
 * @author june
 */
@Component(role = ContentFilter.class, hint = "yaml")
public class YamlContentFilter implements ContentFilter {

    @Override
    public boolean accept(String contentType) {
        return (".yaml").equals(contentType) || (".yml").equals(contentType);
    }

    @Override
    public void filter(InputStream fileIS, OutputStream tmpOS, List<Replace> replaces) throws IOException {
        String yamlText = IOUtils.toString(fileIS);
        Yaml yaml = new Yaml();
        Map<String,Object> initMap = (Map<String, Object>) yaml.load(yamlText);
        for (Replace replace : replaces) {
            String replaceKey = replace.getKey();
            String replaceValue = replace.getValue();
            replace(replaceKey,replaceValue,null,initMap);
        }

        //输出方法1
        //yaml.dump(initMap,new OutputStreamWriter(tmpOS));其格式化为
        /*
        hello2:
          hello21: {hello211: 222, hello212: 212}
        hello1: {hello11: ''}
        */

        //输出方法2
        Writer writer = new OutputStreamWriter(tmpOS);
        writer.write(yaml.dumpAsMap(initMap));
        writer.flush();
        //writer.close();
        //其格式化为
        /*
        hello2:
          hello21:
            hello211: 222
            hello212: 212
        hello1:
          hello11: 11
        */
    }


    private void replace(String replaceKey,String replaceValue,String resultKey,Map<String, Object> map){
        if(map == null || map.isEmpty()){
            return;
        }
        String tempKey;
        for (String key : map.keySet()) {
            if(resultKey == null){
                tempKey = key;
            }else{
                tempKey = resultKey+ "." + key;
            }
            Object obj = map.get(key);
            if (tempKey.equals(replaceKey)) {
                map.put(key, replaceValue);
                break;
            }
            if (obj instanceof Map) {
                Map<String, Object> map2 = (Map<String, Object>) obj;
                replace(replaceKey,replaceValue,tempKey,map2);
            }
        }
    }
}
