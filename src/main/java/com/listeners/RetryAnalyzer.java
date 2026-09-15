package com.listeners;

import com.utils.TestLogger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 1;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < MAX_RETRY_COUNT) {

            retryCount++;

            result.setAttribute(
                    "retryCount",
                    retryCount
            );

            TestLogger.warning(
                    RetryAnalyzer.class,
                    "Retrying failed test: "
                            + result.getName()
                            + " | Retry attempt: "
                            + retryCount
            );

            return true;
        }

        TestLogger.error(
                RetryAnalyzer.class,
                "Test failed after retry: "
                        + result.getName(),
                new RuntimeException(
                        "Maximum retry attempts exhausted."
                )
        );

        return false;
    }
}