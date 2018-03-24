package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.model.Replace;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author june, lihao
 */
public class YamlContentFilterTest extends AbstractContentFilterTest{

    @Override
    public void setUp() throws Exception {
        sut = new YamlContentFilter();
    }

    public void testFilter() throws Exception{
        String input = "";
        input += "#---\n";
        input += "hello2:\n";
        input += "  hello21:\n";
        input += "    hello211: 222\n";
        input += "    hello212: 212\n";
        input += "hello1:\n" +
                "  hello11: 11\n";
        System.out.println(input);
        System.out.println("===============================");
        List<Replace> replaces = new ArrayList<Replace>();
        replaces.add(new Replace("hello1.hello11", null, "value11"));
        replaces.add(new Replace("hello2.hello21.hello211", null, "value211"));
        replaces.add(new Replace("hello2.hello21.hello212", null, "value212"));
        String output = doFilter(input, replaces);
        System.out.println(output);

    }


    @SuppressWarnings("unchecked")
    public void testDump2() throws Exception{
        String input = "";
        input += "#---\n";
        /*input += "traits: !java.lang.String[]\n";
        input += "  - ONE_HAND\n";
        input += "  - 中文\n";*/
        input += "hello2:\n";
        input += "  hello21:\n";
        input += "    hello211: 222\n";
        input += "    hello212: 212\n";
        input += "hello1:\n" +
                "  hello11: 11\n";
        System.out.println(input);
        System.out.println("===============================");

        BufferedOutputStream f = new BufferedOutputStream(System.out);
        Yaml yaml = new Yaml();
        Map<String,Object> initMap = (Map<String, Object>) yaml.load(input);
        f.write(yaml.dumpAsMap(initMap).getBytes());
        f.flush();
        f.close();
    }

}