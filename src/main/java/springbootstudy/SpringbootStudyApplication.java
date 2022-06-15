package springbootstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication 来标注一个主程序类:作用：标注在某个类上说明这个类是SpringBoot的主配置类 ， SpringBoot就应该运行这个类的main方法来启动SpringBoot应用；
//进入这个注解：可以看到上面还有很多其他注解！
//说明这是一个Spring Boot应用
@SpringBootApplication
public class SpringbootStudyApplication {

	/**
	 * @ComponentScan
	 * 这个注解在Spring中很重要 ,它对应XML配置中的元素。
	 * 作用：自动扫描并加载符合条件的组件或者bean ， 将这个bean定义加载到IOC容器中
	 */

	/**
	 @SpringBootConfiguration
	 作用：SpringBoot的配置类 ，标注在某个类上 ， 表示这是一个SpringBoot的配置类；
	 我们继续进去这个注解查看:
	 这里的 @Configuration，说明这是一个配置类 ，配置类就是对应Spring的xml 配置文件；
	 里面的 @Component 这就说明，启动类本身也是Spring中的一个组件而已，负责启动应用！
	 */

	/**
	 @EnableAutoConfiguration ：开启自动配置功能
	 以前我们需要自己配置的东西，而现在SpringBoot可以自动帮我们配置 ；@EnableAutoConfiguration告诉SpringBoot开启自动配置功能，这样自动配置才能生效；
	 点进注解接续查看：
	 @AutoConfigurationPackage ：自动配置包
	 @import ：Spring底层注解@import ， 给容器中导入一个组件
	 Registrar.class 作用：将主启动类的所在包及包下面所有子包里面的所有组件扫描到Spring容器
	 AutoConfigurationImportSelector ：自动配置导入选择器，那么它会导入哪些组件的选择器呢？我们点击去这个类看源码：


	 // 获得候选的配置
	 protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
	 //这里的getSpringFactoriesLoaderFactoryClass（）方法
	 //返回的就是我们最开始看的启动自动导入配置文件的注解类；EnableAutoConfiguration
	 List<String> configurations = SpringFactoriesLoader.loadFactoryNames(this.getSpringFactoriesLoaderFactoryClass(), this.getBeanClassLoader());
	 Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
	 return configurations;

	 这个方法又调用了  SpringFactoriesLoader 类的静态方法！我们进入SpringFactoriesLoader类loadFactoryNames() 方法
	 //这里它又调用了 loadSpringFactories 方法
	 }

	 我们继续点击查看 loadSpringFactories 方法
	 //获得classLoader ， 我们返回可以看到这里得到的就是EnableAutoConfiguration标注的类本身
	 //去获取一个资源 "META-INF/spring.factories"
	 //将读取到的资源遍历，封装成为一个Properties

	 发现一个多次出现的文件：spring.factories，全局搜索它

	 我们根据源头打开spring.factories ， 看到了很多自动配置的文件；这就是自动配置根源所在

	 WebMvcAutoConfiguration

	 我们在上面的自动配置类随便找一个打开看看，比如 ：WebMvcAutoConfiguration
	 可以看到这些一个个的都是JavaConfig配置类，而且都注入了一些Bean，可以找一些自己认识的类，看着熟悉一下！

	 所以，自动配置真正实现是从classpath中搜寻所有的META-INF/spring.factories配置文件 ，并将其中对应的 org.springframework.boot.autoconfigure. 包下的配置项，通过反射实例化为对应标注了 @Configuration的JavaConfig形式的IOC容器配置类 ， 然后将这些都汇总成为一个实例并加载到IOC容器中。
	 */

	public static void main(String[] args) {
		//以为是启动了一个方法，没想到启动了一个服务
		SpringApplication.run(SpringbootStudyApplication.class, args);
	}


	/**
	 结论：

	 SpringBoot在启动的时候从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值

	 将这些值作为自动配置类导入容器 ， 自动配置类就生效 ， 帮我们进行自动配置工作；

	 整个J2EE的整体解决方案和自动配置都在springboot-autoconfigure的jar包中；

	 它会给容器中导入非常多的自动配置类 （xxxAutoConfiguration）, 就是给容器中导入这个场景需要的所有组件 ， 并配置好这些组件 ；

	 有了自动配置类 ， 免去了我们手动编写配置注入功能组件等的工作；
	 */

	/**
	 SpringApplication.run分析

	 分析该方法主要分两部分，一部分是SpringApplication的实例化，二是run方法的执行；

	 SpringApplication
	 这个类主要做了以下四件事情：

	 1、推断应用的类型是普通的项目还是Web项目

	 2、查找并加载所有可用初始化器 ， 设置到initializers属性中

	 3、找出所有的应用程序监听器，设置到listeners属性中

	 4、推断并设置main方法的定义类，找到运行的主类
	 */
}
