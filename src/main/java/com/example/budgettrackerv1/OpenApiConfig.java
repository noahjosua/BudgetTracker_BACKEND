package com.example.budgettrackerv1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Budget Tracker API",
                description = """
                        The Budget Tracker API facilitates the management of expenses and incomes through RESTful endpoints.
                        It allows users to retrieve, save, update and delete expenses and incomes.
                        The API leverages Spring Boot, integrates with Swagger for documentation, and ensures robust error handling for various scenarios.""",
                contact = @Contact(
                        name = "Team 7"
                ),
                version = "v1"
        ),
        servers = {
                @Server(
                        description = "dev",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "prod",
                        url = "https://budgettracker-backend-pudu.onrender.com"
                )
        }
)
public class OpenApiConfig {

}
