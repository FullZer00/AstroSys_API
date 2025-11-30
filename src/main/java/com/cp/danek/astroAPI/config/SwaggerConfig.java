package com.cp.danek.astroAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI astroObservatoryOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription("Локальный сервер разработки");

        Contact contact = new Contact();
        contact.setEmail("support@astro-observatory.com");
        contact.setName("Astronomical Observatory API Support");
        contact.setUrl("https://www.astro-observatory.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Astronomical Observatory API")
                .version("1.0.0")
                .contact(contact)
                .description("""
                        REST API для информационной системы астрономической обсерватории.
                        
                        ## Основные возможности:
                        - Управление пользователями и ролями
                        - Каталогизация астрономических объектов
                        - Управление телескопами и оборудованием
                        - Планирование и проведение наблюдений
                        - Обработка заявок на наблюдения
                        - Техническое обслуживание оборудования
                        - Хранение и управление данными наблюдений
                        
                        ## Роли пользователей:
                        - **ADMIN** - полный доступ ко всем функциям
                        - **ASTRONOMER** - проведение наблюдений, подача заявок
                        - **ENGINEER** - техническое обслуживание оборудования
                        - **STUDENT** - ограниченный доступ для обучения
                        - **GUEST** - базовый просмотр каталога объектов
                        """)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme()));
    }
}