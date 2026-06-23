package com.jp.SecutiryApp.SecutiryApplication;

import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecutiryApplicationTests {
	@Autowired
	private JwtService jwtService;
	@Test
	void contextLoads() {
		UserEntity user = new UserEntity(4L, "jp@gmail.com", "1234");

		String token = jwtService.generateToken(user);

		System.out.println(token);

//		Long id = jwtService.getUserIdFromToken(token);
		Long id = jwtService.getUserIdFromToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0IiwiZW1haWwiOiJqcEBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIiwiQURNSU4iXSwiaWF0IjoxNzgyMjIzODMxLCJleHAiOjE3ODIyMjM4OTF9.8Zk9GAQCJPZQFlMRa9_CcMGNnzlX0Fflp4UWhIhE__rW6vZTW5IER-SUrqd4zxh5");

		System.out.println(id);
	}

}
