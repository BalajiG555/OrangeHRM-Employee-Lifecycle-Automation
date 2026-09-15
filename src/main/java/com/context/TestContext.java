package com.context;

import com.models.Employee;

import io.restassured.response.Response;

public class TestContext {

    private static final ThreadLocal<TestContext> CURRENT =
            new ThreadLocal<>();

    private Employee employee;
    private Response apiResponse;
    private boolean employeeCreated;
    private boolean employeeDeleted;

    public TestContext() {
        CURRENT.set(this);
    }

    public static TestContext current() {
        return CURRENT.get();
    }

    public static void clearCurrent() {
        CURRENT.remove();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Response getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(Response apiResponse) {
        this.apiResponse = apiResponse;
    }

    public boolean isEmployeeCreated() {
        return employeeCreated;
    }

    public void markEmployeeCreated() {
        this.employeeCreated = true;
    }

    public boolean isEmployeeDeleted() {
        return employeeDeleted;
    }

    public void markEmployeeDeleted() {
        this.employeeDeleted = true;
    }

    public void clear() {
        employee = null;
        apiResponse = null;
        employeeCreated = false;
        employeeDeleted = false;
    }
}