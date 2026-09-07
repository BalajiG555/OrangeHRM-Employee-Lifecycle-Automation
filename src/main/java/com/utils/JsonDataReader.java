package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Employee;

import java.io.InputStream;

public final class JsonDataReader {

    private JsonDataReader() {
    }

    public static Employee readEmployeeData() {

        try (InputStream inputStream =
                     JsonDataReader.class
                             .getClassLoader()
                             .getResourceAsStream(
                                     "testdata/employee.json"
                             )) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "employee.json not found"
                );
            }

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(
                    inputStream,
                    Employee.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read employee test data",
                    e
            );
        }
    }
}