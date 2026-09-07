package com.api;

import com.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EmployeeApiClient {

    private String accessToken;

    public EmployeeApiClient() {

        RestAssured.baseURI =
                ConfigReader.get("api.base.url");
    }

    // =========================================================
    // API 1: Get OAuth Access Token
    // =========================================================
    public void authenticate(String authorizationCode) {

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
                                ConfigReader.get("api.client.id")
                        )
                        .formParam(
                                "client_secret",
                                ConfigReader.get("api.client.secret")
                        )
                        .formParam(
                                "redirect_uri",
                                ConfigReader.get("api.redirect.uri")
                        )
                        .when()
                        .post("/oauth2/token");

        System.out.println("========================================");
        System.out.println("API 1 - OAuth Token API");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body:");
        System.out.println(response.asPrettyString());
        System.out.println("========================================");

        if (response.statusCode() != 200) {

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

            throw new RuntimeException(
                    "Access token was not returned from OAuth API."
            );
        }
    }


    // =========================================================
    // API 2: Get Employees
    // =========================================================
    public Response getEmployees() {

        validateAccessToken();

        Response response =
                given()
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .queryParam("limit", 1000)
                        .when()
                        .get("/api/v2/pim/employees");

        System.out.println("========================================");
        System.out.println("API 2 - Get Employees API");
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body:");
        //System.out.println(response.asPrettyString());
        System.out.println("========================================");

        return response;
    }


    // =========================================================
    // Find Employee Number from API 2 response
    // =========================================================
    public String findEmployeeNumber(String employeeId) {

        Response response = getEmployees();

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Get Employees API failed. Status code: "
                            + response.statusCode()
            );
        }

        String empNumber =
                response.jsonPath()
                        .getString(
                                "data.find { it.employeeId == '"
                                        + employeeId
                                        + "' }.empNumber"
                        );

        if (empNumber == null
                || empNumber.isBlank()
                || empNumber.equals("null")) {

            throw new RuntimeException(
                    "Employee ID "
                            + employeeId
                            + " was not found in API 2 response."
            );
        }

        System.out.println("========================================");
        System.out.println("Employee Found");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Emp Number: " + empNumber);
        System.out.println("========================================");

        return empNumber;
    }


    // =========================================================
    // API 3: Get Employee Details
    // =========================================================
    public Response getEmployee(String empNumber) {

        validateAccessToken();

        Response response =
                given()
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .when()
                        .get(
                                "/api/v2/pim/employees/"
                                        + empNumber
                        );

        System.out.println("========================================");
        System.out.println("API 3 - Get Employee Details API");
        System.out.println("Emp Number: " + empNumber);
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body:");
        System.out.println(response.asPrettyString());
        System.out.println("========================================");

        return response;
    }


    // =========================================================
    // Validate Access Token
    // =========================================================
    private void validateAccessToken() {

        if (accessToken == null
                || accessToken.isBlank()) {

            throw new IllegalStateException(
                    "Access token is missing. "
                            + "Please authenticate first."
            );
        }
    }
}