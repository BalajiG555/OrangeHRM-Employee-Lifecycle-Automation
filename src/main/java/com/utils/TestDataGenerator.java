package com.utils;

import com.models.Employee;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TestDataGenerator {

    private static final DateTimeFormatter ID_FORMAT =
            DateTimeFormatter.ofPattern("ddHHmmss");

    private TestDataGenerator() {
    }

    public static Employee createEmployee() {

        Employee employee =
                JsonDataReader.readEmployeeData();

        String uniqueValue =
                LocalDateTime.now()
                        .format(ID_FORMAT);

        employee.setFirstName(
                employee.getFirstName() + uniqueValue
        );

        employee.setLastName(
                employee.getLastName() + uniqueValue
        );

        employee.setEmployeeId(
                "A" + uniqueValue
        );

        return employee;
    }

    public static String createEssUsername() {

        String uniqueValue =
                LocalDateTime.now()
                        .format(ID_FORMAT);

        return "essuser" + uniqueValue;
    }

    public static String createEssPassword() {

        String uniqueValue =
                LocalDateTime.now()
                        .format(ID_FORMAT);

        return "Ess@" + uniqueValue;
    }
}