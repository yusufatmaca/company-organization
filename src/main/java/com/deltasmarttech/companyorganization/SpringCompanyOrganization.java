package com.deltasmarttech.companyorganization;

import com.deltasmarttech.companyorganization.models.Role;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCompanyOrganization {

    public static void main(String[] args) {
        SpringApplication.run(SpringCompanyOrganization.class, args);
    }

}
