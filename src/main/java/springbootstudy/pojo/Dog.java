package springbootstudy.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @projectName: springboot-study
 * @package: com.lin.boot.pojo
 * @className: Dog
 * @author: linyao
 * @description:
 * @date: 2022/6/15 19:45
 * @version: 1.0。0
 */

@Data
@Component  //注册bean到容器中
public class Dog {

    //思考，我们原来是如何给bean注入属性值的！@Value
    @Value("阿黄")
    private String name;
    @Value("18")
    private Integer age;

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
