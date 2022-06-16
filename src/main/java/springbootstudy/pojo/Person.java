package springbootstudy.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @projectName: springboot-study
 * @package: springbootstudy.pojo
 * @className: Person
 * @author: linyao
 * @description:
 * @date: 2022/6/15 19:55
 * @version: 1.0。0
 */

/**
 *@ConfigurationProperties作用：
 * 将配置文件中配置的每一个属性的值，映射到这个组件中；
 * 告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 * 参数prefix = "person"：将配置文件中person下面的所有属性一一对应
 */
@Data
@Component //注册bean到容器中
@ConfigurationProperties(prefix = "person")//默认从全局配置文件中获取值
@Validated //Springboot中可以用@Validated来校验数据，如果数据异常会统一抛出异常，方便异常中心统一处理。
public class Person {

    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
    private String email;
}
