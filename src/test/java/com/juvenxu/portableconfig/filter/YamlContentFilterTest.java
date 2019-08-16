package com.juvenxu.portableconfig.filter;

import com.juvenxu.portableconfig.model.Replace;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author june
 */
public class YamlContentFilterTest extends AbstractContentFilterTest{

    @Override
    public void setUp() throws Exception {
        sut = new YamlContentFilter();
    }

    public void testFilter() throws Exception{

        String input = "";

        input += "---\n";
        input += "traits:\n";
        input += "  - ONE_HAND\n";
        input += "  - 中文\n";
        input += "hello2:\n";
        input += "  hello22:\n";
        input += "      hello3: 3220\n";
        input += "hello: 30\n";
        System.out.println(input);
        System.out.println("===============================");
        List<Replace> replaces = new ArrayList<Replace>();
        replaces.add(new Replace("traits", null, "[\"事实事实上上\",\"事实上\"]"));
        replaces.add(new Replace("hello", null, "22230"));
        replaces.add(new Replace("hello2.hello22.hello3", null, "322220"));
        String output = doFilter(input, replaces);
        System.out.println(output);

    }


    public void testDump2() {
        Map<String, Object> data = new HashMap<String, Object>();
        //data.put("name", "Silenthand Olleander");
        data.put("race", "20");
        //data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
        String str = new Yaml().dump(data);
        System.out.println(str);
    }

}