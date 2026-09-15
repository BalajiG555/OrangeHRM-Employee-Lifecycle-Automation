package com.api;

import com.config.ConfigReader;
import com.utils.TestLogger;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EmployeeApiClient {

    private String accessToken;

    public EmployeeApiClient() {

        RestAssured.baseURI =
                ConfigReader.getRequired("api.base.url");

        TestLogger.info(
                EmployeeApiClient.class,
                "API base URI configured for environment: "
                        + ConfigReader.getEnvironment()
        );
    }

    /**
     * Authenticates using OAuth2 Authorization Code flow
     * and stores the returned access token for subsequent APIs.
     */
    public void authenticate() {

        String authorizationCode =
                ConfigReader.getRequired(
                        "api.authorization.code"
                );

        String clientId =
                ConfigReader.getRequired(
                        "api.client.id"
                );

        String clientSecret =
                ConfigReader.getRequired(
                        "api.client.secret"
                );

        String redirectUri =
                ConfigReader.getRequired(
                        "api.redirect.uri"
                );

        TestLogger.info(
                EmployeeApiClient.class,
                "Starting OAuth2 authentication."
        );

        Response response =
                given()
                        .contentType(
                                "application/x-www-form-urlencoded"
                        )
                        .formParam(
                                "grant_type",
                                "authorization_code"
                        )
                        .formParam(
                                "code",
                                authorizationCode
                        )
                        .formParam(
                                "client_id",
                                clientId
                        )
                        .formParam(
                                "client_secret",
                                clientSecret
                        )
                        .formParam(
                                "redirect_uri",
                                redirectUri
                        )
                        .when()
                        .post("/oauth2/token");

        TestLogger.info(
                EmployeeApiClient.class,
                "OAuth token API status code: "
                        + response.statusCode()
        );

        if (response.statusCode() != 200) {

            TestLogger.error(
                    EmployeeApiClient.class,
                    "OAuth token API failed.",
                    new RuntimeException(
                            response.asPrettyString()
                    )
            );

            throw new RuntimeException(
                    "OAuth token API failed. Status code: "
                            + response.statusCode()
            );
        }

        accessToken =
                response.jsonPath()
                        .getString("access_token");

        if (accessToken == null
                || accessToken.isBlank()) {

            throw new IllegalStateException(
                    "Access token was not returned from OAuth API."
            );
        }

        TestLogger.info(
                EmployeeApiClient.class,
                "Access token received successfully."
        );
    }

    /**
     * Fetch all employees using the OAuth access token.
     */
    public Response getEmployees() {

        validateAccessToken();

        Response response =
                given()
                        .redirects()
                        .follow(false)
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .queryParam("limit", 1000)
                        .queryParam("offset", 0)
                        .when()
                        .get("/api/v2/pim/employees");

        TestLogger.info(
                EmployeeApiClient.class,
                "Get Employees API status code: "
                        + response.statusCode()
        );

        validateSuccessfulResponse(
                response,
                "Get Employees"
        );

        return response;
    }

    /**
     * Find employee number using Employee ID.
     */
    public String findEmployeeNumber(
            String employeeId) {

        Response response =
                getEmployees();

        return response.jsonPath()
                .getString(
                        "data.find { it.employeeId == '"
                                + employeeId
                                + "' }.empNumber"
                );
    }

    /**
     * Fetch a specific employee using the employee number.
     */
    public Response getEmployee(
            String empNumber) {

        validateAccessToken();

        Response response =
                given()
                        .redirects()
                        .follow(false)
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .queryParam(
                                "model",
                                "detailed"
                        )
                        .when()
                        .get(
                                "/api/v2/pim/employees/"
                                        + empNumber
                        );

        TestLogger.info(
                EmployeeApiClient.class,
                "Get Employee API status code: "
                        + response.statusCode()
        );

        validateSuccessfulResponse(
                response,
                "Get Employee"
        );

        return response;
    }

    /**
     * Check whether an employee exists through API.
     */
    public boolean employeeExists(
            String employeeId) {

        String empNumber =
                findEmployeeNumber(employeeId);

        return empNumber != null
                && !empNumber.isBlank()
                && !empNumber.equals("null");
    }

    private void validateAccessToken() {

        if (accessToken == null
                || accessToken.isBlank()) {

            throw new IllegalStateException(
                    "Access token is missing. "
                            + "Call authenticate() first."
            );
        }
    }

    private void validateSuccessfulResponse(
            Response response,
            String operation) {

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            TestLogger.error(
                    EmployeeApiClient.class,
                    operation
                            + " API failed with status code: "
                            + response.statusCode(),
                    new RuntimeException(
                            response.asPrettyString()
                    )
            );

            throw new RuntimeException(
                    operation
                            + " API failed. Status code: "
                            + response.statusCode()
            );
        }
    }
}