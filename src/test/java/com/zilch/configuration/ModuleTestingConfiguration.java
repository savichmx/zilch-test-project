package com.zilch.configuration;

import com.zilch.repository.CustomerRepository;
import com.zilch.repository.TransactionRepository;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan({"com.zilch.mapper", "com.zilch.controller", "com.zilch.service"})
@EnableAutoConfiguration(exclude = {  DataSourceAutoConfiguration.class })
public class ModuleTestingConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return Mockito.mock(TransactionRepository.class);
    }

    @Bean
    @Primary
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

}
