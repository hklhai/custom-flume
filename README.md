# Unicom IOT Flume
自定义Http采集
 
 
 
---
> HK  
> linh@bjhxqh.com

## 注意项目务必不要使用springboot模板建立，否则flume无法找到自定义处理类


### flume 远程Debug开启
修改配置文件中flume-env.sh
```
JAVA_OPTS="-Xmx20m -Xdebug -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"
```

### flume 启动
```
bin/flume-ng agent --conf-file conf/http-kafka-conf.properties --name a1 -Dflume.root.logger=INFO,console
bin/flume-ng agent --conf ./conf/ --conf-file conf/http-kafka-conf.properties --name a1 -Dflume.root.logger=INFO,console
```
### IDEA配置
增加remote，配置host、port即可，对remote进行debug（原理：JMX交互）

command:
```
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787
```
