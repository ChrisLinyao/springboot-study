package springbootstudy.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

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
@PropertySource(value = "classpath:pojo-person.yml")//加载指定的配置文件
public class PersonWithClasspath {
   @Value("${name}")
    private String name;//从配置文件中取值
    @Value("#{9*2}")  // #{SPEL} Spring表达式
    private Integer age;
 @Value("${happy}")
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
    @Value("男")  // 字面量
    private String sex;
}
