package com.project.marginal.tax.calculator.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taxApiOpenAPI() {
        return new OpenAPI()
          .info(new Info()
            .title("Marginal Tax Rate Calculator API")
            .version("v1")
            .description("Historical income tax rates and calculation endpoints"));
    }
}
