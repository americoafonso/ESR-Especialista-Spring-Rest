package com.algaworks.algafood;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testng.annotations.Test;

import static org.junit.Assert.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlgafoodApiApplicationTests {

	@Test
	void contextLoads() {
		assertFalse(false);
	}

}
