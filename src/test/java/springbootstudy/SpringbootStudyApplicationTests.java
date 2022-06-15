package springbootstudy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springbootstudy.pojo.Dog;

@SpringBootTest
class SpringbootStudyApplicationTests {

	@Autowired //将狗狗自动注入进来
	Dog dog;

	@Test
	void contextLoads() {
		System.out.println(dog); //打印看下狗狗对象
	}

}
