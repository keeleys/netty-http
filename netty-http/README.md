#netty http接口通用组件。

## 基于http+(xml/json)协议.

###使用方法

1. 导入`pom`项目
```xml
<dependency>
	<groupId>com.szmsd</groupId>
	<artifactId>netty-http</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. 在`src/main/resources`下面新建配置文件`ctrl.txt`,`jdbc.properties`,配置扫描的包和jdbc信息
 2.1. `ctrl.txt`
```java
//扫描的包
ctrl_scan_path = com.keeley.server
//xml/json 内容使用的参数
content_param = content
```
 2.2. `jdbc.properties`
 ```properties
driverClassName=oracle.jdbc.OracleDriver
db.oracle.url=XXX
db.oracle.username=XXX
db.oracle.password=XXX
 ```

3. 编写process处理类 处理接口服务，有3个接口 `JsonAware,XmlAware,RequestAware`.
 * 实现 `JsonAware` 对应传输json文本的请求
 * 实现 `XmlAware` 对应传输xml文本的请求
 * 实现 `RequestAware` 代表不解析请求，自己手动处理
 * process类必须实现注解*`MsdCtrl`* 必填参数请求路径，和处理方法名，默认处理方法名`execute`,返回`String`代表返回给调用方的字符串.
例子代码
```java
@MsdCtrl(url = "/selectOrder")
public class SelectOrderProcess implements JsonAware{
	private XJDataNode node ;
	
	/**
	 * 例子 http://127.0.0.1:8088/selectOrder?content={"order_code":"coc333"}
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
	public void setJsonDataNode(XJDataNode node) {
		this.node= node;
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

