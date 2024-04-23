package com.myprojects.savemoney.appconfig;

import com.myprojects.savemoney.audit.ApplicationAuditAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class SpringBootAuditing {

    @Bean
    public AuditorAware<String>auditorAware() {
        return new ApplicationAuditAware();
    }
}
