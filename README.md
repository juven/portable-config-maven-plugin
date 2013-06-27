[![Build Status](https://api.travis-ci.org/juven/portable-config-maven-plugin.png)](https://travis-ci.org/juven/portable-config-maven-plugin)


## Why Portable Config Maven Plugin?

When you want to deploy your application into different environemnts, say typical dev/test/staging/production, often you will want to change
some configuration at build time. In Maven, this is a feature called [Resource Filtering](http://maven.apache.org/plugins/maven-resources-plugin/examples/filter.html),
which works quite well except you have to use Maven Property for every configuration you want to change. So you will have:

```
database.jdbc.connectionURL=${db.connectionURL}
database.jdbc.username=${db.username}
database.jdbc.password=${db.password}
```

So there's _no_ default configuration in this case. With portable-config-maven-plugin, all configurations have default value, and you configure build
to change the configurations _only when_ you need to.

## How to Use?

1. You have a configuration file _src/main/resources/db.properties_ with default values:

  ```
database.jdbc.username=dev
database.jdbc.password=dev_pwd
  ```

2. You want to change it in testing environment, so prepare a file for this change _src/main/portable/test.xml_:

  ```xml
    <?xml version="1.0" encoding="utf-8" ?>
    <portable-config>
      <config-file path="WEB-INF/classes/db.properties">
        <replace key="database.jdbc.username">test</replace>
        <replace key="database.jdbc.password">test_pwd</replace>
      </config-file>
    </portable-config>
  ```

 Here the _config_file_ is the file you want to change for testing environment, and it's _path_ is the relative path in final war.
 Then you can use _replace_ to change the configurations you want to change.

 Besides properties file, you can also change configurations in XML files with xpath:

  ```xml
    <?xml version="1.0" encoding="utf-8" ?>
    <portable-config>
      <config-file path="WEB-INF/web.xml">
        <replace xpath="/web-app/display-name">awesome app</replace>
      </config-file>
    </portable-config>
  ```
3. Configuration Maven to use portable-config-maven-plugin and apply your portable config:

  ```xml
     <plugin>
       <groupId>com.juvenxu.portable-config-maven-plugin</groupId>
       <artifactId>portable-config-maven-plugin</artifactId>
       <version>1.0.1</version>
         <executions>
           <execution>
             <goals>
               <goal>replace-package</goal>
             </goals>
           </execution>
         </executions>
         <configuration>
           <portableConfig>src/main/portable/test.xml</portableConfig>
         </configuration>
     </plugin>
   ```

 You can also use maven property __portableConfig__:
 `$ mvn clean package -DportableConfig="src/main/portable/test.xml"`

4. And further wrapper the property into Maven profile:

  ```xml
     <profiles>
       <profile>
         <id>test</id>
         <properties>
           <portableConfig>src/main/portable/test.xml</portableConfig>
         </properties>
       </profiles>
     </profiles>
  ```

## Acknowledgments

* Thanks to [Jacky Chan](https://github.com/linux-china) for initially inspiring me to write this tool with thoughtful ideas.