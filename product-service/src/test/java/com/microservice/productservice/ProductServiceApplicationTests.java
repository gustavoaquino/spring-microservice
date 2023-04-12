package com.microservice.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ProductRepository productRepository;


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		final ProductRequest productRequest = getProductRequest();

		String productRequestString = new ObjectMapper().writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequestString))
				.andExpect(status().isCreated());

		Assertions.assertEquals(1, this.productRepository.findAll().size());
	}

	@Test
	void shouldReturnSuccessful() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest
				.builder()
				.name("Iphone 13")
				.description("Descrição")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
