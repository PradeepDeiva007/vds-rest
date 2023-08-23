package com.ford.vds;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nl.martijndwars.webpush.PushService;

@SpringBootApplication
public class VdsRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(VdsRestApplication.class, args);
	}

	@Bean
	ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	PushService getPushService() {
		return new PushService();
	}
}
