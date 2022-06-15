package springbootstudy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springbootstudy.pojo.Dog;
import springbootstudy.pojo.Person;
import springbootstudy.pojo.PersonWithClasspath;

@SpringBootTest
class SpringbootStudyApplicationTests {

	@Autowired //将狗狗自动注入进来
	Dog dog;
	@Autowired
	Person person;
	@Autowired
	PersonWithClasspath personWithClasspath;

	@Test
	void contextLoads() {
		System.out.println(dog); //打印看下狗狗对象
		System.out.println(person);
		System.out.println(personWithClasspath);
	}

}
