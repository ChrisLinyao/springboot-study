# 1.yaml配置注入

![image-20220615202400204](images/image-20220615202400204.png)

1、@ConfigurationProperties只需要写一次即可 ， @Value则需要每个字段都添加

2、松散绑定：这个什么意思呢? 比如我的yml中写的last-name，这个和lastName是一样的， - 后面跟着的字母默认是大写的。这就是松散绑定。可以测试一下

3、JSR303数据校验 ， 这个就是我们可以在字段是增加一层过滤器验证 ， 可以保证数据的合法性

4、复杂类型封装，yml中可以封装对象 ， 使用value就不支持

**结论：**

配置yml和配置properties都可以获取到值 ， 强烈推荐 yml；

如果我们在某个业务中，只需要获取配置文件中的某个值，可以使用一下 @value；

如果说，我们专门编写了一个JavaBean来和配置文件进行一一映射，就直接@configurationProperties，不要犹豫！

# 2.自动配置原理

## 2.1 HttpEncodingAutoConfiguration

我们以**HttpEncodingAutoConfiguration（Http编码自动配置）**为例解释自动配置原理：

```java

//表示这是一个配置类，和以前编写的配置文件一样，也可以给容器中添加组件；
@Configuration 

//启动指定类的ConfigurationProperties功能；
  //进入这个HttpProperties查看，将配置文件中对应的值和HttpProperties绑定起来；
  //并把HttpProperties加入到ioc容器中
@EnableConfigurationProperties({HttpProperties.class}) 

//Spring底层@Conditional注解
  //根据不同的条件判断，如果满足指定的条件，整个配置类里面的配置就会生效；
  //这里的意思就是判断当前应用是否是web应用，如果是，当前配置类生效
@ConditionalOnWebApplication(
    type = Type.SERVLET
)

//判断当前项目有没有这个类CharacterEncodingFilter；SpringMVC中进行乱码解决的过滤器；
@ConditionalOnClass({CharacterEncodingFilter.class})

//判断配置文件中是否存在某个配置：spring.http.encoding.enabled；
  //如果不存在，判断也是成立的
  //即使我们配置文件中不配置pring.http.encoding.enabled=true，也是默认生效的；
@ConditionalOnProperty(
    prefix = "spring.http.encoding",
    value = {"enabled"},
    matchIfMissing = true
)

public class HttpEncodingAutoConfiguration {
    //他已经和SpringBoot的配置文件映射了
    private final Encoding properties;
    //只有一个有参构造器的情况下，参数的值就会从容器中拿
    public HttpEncodingAutoConfiguration(HttpProperties properties) {
        this.properties = properties.getEncoding();
    }
    
    //给容器中添加一个组件，这个组件的某些值需要从properties中获取
    @Bean
    @ConditionalOnMissingBean //判断容器没有这个组件？
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.properties.getCharset().name());
        filter.setForceRequestEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.REQUEST));
        filter.setForceResponseEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.RESPONSE));
        return filter;
    }
    //。。。。。。。
}
```

总结：根据当前不同的条件判断，决定这个配置类是否生效。

- 一旦这个配置类生效，该配置类就会跟容器中添加各种组件；
- 这些组件的属性是从对应的properties类中获取的，这些类里面的每个属性又是和配置文件绑定的；
- 所有在配置文件中能配置的属性都是在***Properties类中封装着；
- 配置文件能配置什么就可以参照某个功能对应的这个属性类

```java

//从配置文件中获取指定的值和bean的属性进行绑定
@ConfigurationProperties(prefix = "spring.http") 
public class HttpProperties {
    // ....
}
```

去配置文件试试前缀，看提示！

![image-20220616124326696](images/image-20220616124326696.png)

这就是自动封装的原理！

总结：

- SpringBoot启动会加载大量的自动配置类

- 我们看我们需要的功能有米有在SpringBoot默认写好的自动配置类中；

- 再来看这个自动配置类中到底配置了哪些组件；（只要我们要用的组件存在其中，就不需要再动手配置了）

- 给容器中自动配置类添加组件的时候，会从propeirties类中获取某些属性。我们只需要在配置文件中指定这些属性的值即可。

  XXXXAutoConfiguration：自动配置类；给容器中添加组件

  XXXXProperties:封装配置文件中相关属性

  

## 2.2 @Conditional

自动配置类必须在一定的条件下才能生效。

**Conditional派生注解（Spring注解版原生的@Conditional作用）**

作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置配里面的所有内容才生效；

![image-20220616140947608](images/image-20220616140947608.png)

这么多的自动配置类，必须在一定的条件下才能生效；也就是说，加载了这么多的配置类，但不是

所有的都生效了。

可以通过启用debug=true属性；来让控制台打印自动生效配置的报告。



```
#开启springboot的调试类
debug=true
```

**Positive matches:（自动配置类启用的：正匹配）**

**Negative matches:（没有启动，没有匹配成功的自动配置类：负匹配）**

**Unconditional classes: （没有条件的类）**

![image-20220616142423788](images/image-20220616142423788.png)

# 3.自定义starter

## 3.1 基本

启动器模块：是一个空jar文件，仅提供辅助性依赖管理，这些依赖可能用于自动装配或其他类库。

命名规约：

官方命名：

- 前缀：spring-boot-starter-xxx
- 比如：spring-boot-starter-web....

自定义命名：

- xxx-spring-boot-starter
- 比如：mybatis-spring-boot-starter



## 3.2编写启动器

1. 在IDEA新建一个空项目: spring-boot-starter-diy

2. 新建一个普通Maven模块: lin-spring-boot-starter

3. 新建一个Springboot模块：lin-spring-boot-starter-autoconfigure

   ![image-20220616183917199](images/image-20220616183917199.png)

4. 在我们的 starter 中 导入  autoconfigure 的依赖！

```xml

<!-- 启动器 -->
<dependencies>
    <!--  引入自动配置模块 -->
    <dependency>
        <groupId>com.lin</groupId>
        <artifactId>lin-spring-boot-starter-autoconfigure</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

5.将 autoconfigure 项目下多余的文件都删掉，Pom中只留下一个 starter，这是所有的启动器基本配置！

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.lin</groupId>
    <artifactId>lin-spring-boot-starter-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>lin-spring-boot-starter-autoconfigure</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
    </properties>
<!--    将 autoconfigure 项目下多余的文件都删掉，Pom中只留下一个 starter，这是所有的启动器基本配置-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>

```

6.编写一个自己的服务

```java
package com.lin.linspringbootstarterautoconfigure;

/**
 * @projectName: springboot-starter-diy
 * @package: com.lin.linspringbootstarterautoconfigure
 * @className: HelloService
 * @author: linyao
 * @description: TODO
 * @date: 2022/6/16 18:48
 * @version: 1.0。0
 */
public class HelloService {

	HelloProperties helloProperties;

	public HelloProperties getHelloProperties() {
		return helloProperties;
	}

	public void setHelloProperties(HelloProperties helloProperties) {
		this.helloProperties = helloProperties;
	}

	public String sayHello(String name){
		return helloProperties.getPrefix() + name + helloProperties.getSuffix();
	}
}

```

7.编写HelloProperties 配置类

```java
package com.lin.linspringbootstarterautoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @projectName: springboot-starter-diy
 * @package: com.lin.linspringbootstarterautoconfigure
 * @className: HelloProperties
 * @author: linyao
 * @description: TODO
 * @date: 2022/6/16 18:48
 * @version: 1.0。0
 */

// 前缀 lin.hello
@ConfigurationProperties(prefix = "lin.hello")
public class HelloProperties {

	private String prefix;
	private String suffix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}

```

8.编写我们的自动配置类并注入bean，测试！

 ```java
package com.lin.linspringbootstarterautoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: springboot-starter-diy
 * @package: com.lin.linspringbootstarterautoconfigure
 * @className: HelloServiceAutoConfiguration
 * @author: linyao
 * @description: TODO
 * @date: 2022/6/16 18:50
 * @version: 1.0。0
 */
@Configuration
@ConditionalOnWebApplication //web应用生效
@EnableConfigurationProperties(HelloProperties.class)
public class HelloServiceAutoConfiguration {

	@Autowired
	HelloProperties helloProperties;

	@Bean
	public HelloService helloService() {
		HelloService service = new HelloService();
		service.setHelloProperties(helloProperties);
		return service;
	}
}

 ```

9.在resources编写一个自己的 META-INF\spring.factories

```yml
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\com.lin.linspringbootstarterautoconfigure

```

10.编写完成后，可以安装到maven仓库中！

![image-20220616192755767](images/image-20220616192755767.png)



![image-20220616192826569](images/image-20220616192826569.png)

![image-20220616193101589](images/image-20220616193101589.png)

11.新建一个项目（springboot-study），导入我们自己写的启动器

```xml
        <dependency>
            <groupId>com.lin</groupId>
            <artifactId>lin-spring-boot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

12.编写一个 HelloController  进行测试我们自己的写的接口！