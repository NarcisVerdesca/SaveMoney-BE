package com.myprojects.savemoney.appconfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class AppConfig {

    @Bean
    public ModelMapper modelMapperStandard() {
        return new ModelMapper();
    }

}
