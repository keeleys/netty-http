# netty http接口通用组件。

## 基于http+(xml/json)协议.

### 使用方法

1. 导入`pom`项目
```xml
<dependency>
	<groupId>com.szmsd</groupId>
	<artifactId>netty-http</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. 在`src/main/resources`下面新建配置文件`conf.properties`,`jdbc.properties`,配置扫描的包和jdbc信息
 2.1. `conf.properties`
```java
//扫描的包
ctrl_scan_path = com.keeley.server
//xml/json 内容使用的参数
content_param = content
//加密之后的字符串
sign_param = sign
//签名密钥
key = szmsd
```
 2.2. `jdbc.properties`
 ```java
driverClassName=oracle.jdbc.OracleDriver
db.oracle.url=XXX
db.oracle.username=XXX
db.oracle.password=XXX
 ```
 2.3 ` logback.xml`
 ```xml
<configuration debug="false">
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>

  <root level="${logLevel:-info}">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>

 ```

3. 编写process处理类 处理接口服务，
有3个接口 `JsonAware`,`XmlAware`,`RequestAware`,`ValidAware`
有2个实现 `NettyJsonProcess`,`NettyXmlProcess`
 * 实现 `JsonAware` 对应传输json文本的请求
 * 实现 `XmlAware` 对应传输xml文本的请求
 * 实现 `ValidAware` 对应处理加密
 * 实现 `RequestAware` 代表不解析请求，自己手动处理
 * `NettyJsonProcess`默认实现 `JsonAware`+`ValidAware`
 * `NettyXmlProcess`默认实现 `XmlAware`+`ValidAware`
 * process类必须实现注解*`MsdCtrl`* 必填参数请求路径，和处理方法名，默认处理方法名`execute`,返回`String`代表返回给调用方的字符串.
例子代码
```java
@MsdCtrl(url = "/selectOrder")
public class SelectOrderProcess extends NettyJsonProcess{
	/**
	* 例子 http://127.0.0.1:8088/selectOrder?sign=N2E3ZjdlN2UxOGE4Yzk3YWQwOGE4ZDk4MWQzNzlkMjc=&content={"order_code":"coc333"}
	* {"order_code":"coc333"}szmsd
	* md5 32位小写加密：7a7f7e7e18a8c97ad08a8d981d379d27
	* base64加密md5 ：N2E3ZjdlN2UxOGE4Yzk3YWQwOGE4ZDk4MWQzNzlkMjc=
	* @Description:
	* @author TianJun
	* @date 2015年3月5日
	* @return
	*/
	public String execute(){
		final String orderCode = node.get("order_code");
		List<Record> result = Db.find("select * from tab_vip_order where order_code =?", orderCode);
		return JsonKit.toJson(result);
	}
}
```

4. 运行nettyhttp服务,测试
```java
public class Client {
	public static void main(String[] args) throws Exception {
		NettyServer.start(8088);//传入端口号
	}
}
```

