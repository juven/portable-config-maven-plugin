## TODO

* handle encoding
* more tests on replacing jar
* DefaultPortableConfigEngine is too big, refactor it!
* error handling

## From 雷卷:
1. 能否不声明executions
2. 版本号调整为 1.0.0
3. 添加plugin的xml 配置参数: portableConfig=hz-test
4. 调整为普通plugin声明模式，而不是在profile中声明，profile中可以声明 portableConfig property
5. xml能否精简如 <replace key="">xxx</replace>   <replace xpath=""></replace>