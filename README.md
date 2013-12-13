[![Build Status](https://api.travis-ci.org/juven/portable-config-maven-plugin.png)](https://travis-ci.org/juven/portable-config-maven-plugin)


## Why Portable Config Maven Plugin?

When you want to deploy your application into different environments, say typical dev/test/staging/production, often you will want to change
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

 Here the _config_file_ is the file you want to change for testing environment, and _path_ is its relative path in __final war__.
 Then you can use _replace_ to change the configurations.

3. Configure Maven to use portable-config-maven-plugin and apply your portable config:

  ```xml
     <plugin>
       <groupId>com.juvenxu.portable-config-maven-plugin</groupId>
       <artifactId>portable-config-maven-plugin</artifactId>
       <version>1.1.5</version>
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

  You can also use maven property __portableConfig__ to specify your portable config file:
 `$ mvn clean package -DportableConfig="src/main/portable/test.xml"`

4. After running Maven, the file _WEB-INF/classes/db.properties_ in the __final war__ is now having:

  ```
  database.jdbc.username=test
  database.jdbc.password=test_pwd
  ```

## Supported Configuration File Format

### .properties

Properties files are most used configuration file format:

  ```
  database.jdbc.username=dev
  database.jdbc.password=dev_pwd
  ```

with

  ```xml
  <replace key="database.jdbc.username">test</replace>
  <replace key="database.jdbc.password">test_pwd</replace>
  ```

become

  ```
  database.jdbc.username=test
  database.jdbc.password=test_pwd
  ```

### .sh

Unquoted, single quoted, double quoted and exported shell variables can be replaced:

  ```bash
  BIN_HOME=/tmp/bin
  OUT_HOME="/tmp/out"
  LOG_HOME='/tmp/log'
  export APP_HOME="/tmp/app"
  ```

with

  ```xml
  <replace key="BIN_HOME">/home/juven/bin</replace>
  <replace key="OUT_HOME">/home/juven/out</replace>
  <replace key="LOG_HOME">/home/juven/log</replace>
  <replace key="APP_HOME">/home/juven/app</replace>
  ```

become

  ```bash
  BIN_HOME=/home/juven/bin
  OUT_HOME="/home/juven/out"
  LOG_HOME='/home/juven/log'
  export APP_HOME="/home/juven/app"
  ```

### .xml

Xml elements/attributes can be replaced via xPath:

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <server>
    <port>8080</port>
    <hosts>
      <host id="1">localhost</host>
      <host id="2">localhost</host>
    </hosts>
    <mode value="debug">
  </server>"
  ```

with

  ```xml
  <replace xpath="/server/port">80</replace>
  <replace xpath="//host/[@id='1']">192.168.1.1</replace>
  <replace xpath="//host/[@id='2']">192.168.1.2</replace>
  <replace xpath="/server/mode/@value">run</replace>
  ```
become


  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <server>
    <port>80</port>
    <hosts>
      <host id="1">192.168.1.1</host>
      <host id="2">192.168.1.2</host>
    </hosts>
    <mode value="run">
  </server>"
  ```

## FAQ

__Q: I have properties file but it's extension is not .properties, how to make this plugin to recognize it?__

A. You can specify file type in your portable config xml like this:

  ```xml
  <?xml version="1.0" encoding="utf-8" ?>
  <portable-config>
    <config-file path="db.ini" type=".properties">
      <replace key="mysql.host">192.168.1.100</replace>
    </config-file>
  </portable-config>
  ```

## Tutorial From Users

* [portable-config-maven-plugin 与 resource filter 对比](http://fuxueliang.com/tool/2013/08/16/portable-config-maven-plugin-vs-resource-or-war-filter/)

## Acknowledgments

* Thanks to [Jacky Chan](https://github.com/linux-china) for initially inspiring me to write this tool with thoughtful ideas.