package com.listeners;

import com.utils.TestLogger;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class FlakyTestListener implements ITestListener {

    private static final String RETRY_COUNT_ATTRIBUTE =
            "retryCount";

    @Override
    public void onTestFailure(ITestResult result) {

        Integer retryCount =
                (Integer) result.getAttribute(
                        RETRY_COUNT_ATTRIBUTE
                );

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount > 0) {

            TestLogger.warning(
                    FlakyTestListener.class,
                    "Potential flaky test detected: "
                            + result.getName()
                            + " | Retry count: "
                            + retryCount
            );
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        Integer retryCount =
                (Integer) result.getAttribute(
                        RETRY_COUNT_ATTRIBUTE
                );

        if (retryCount != null && retryCount > 0) {

            TestLogger.warning(
                    FlakyTestListener.class,
                    "Flaky test passed after retry: "
                            + result.getName()
                            + " | Retry count: "
                            + retryCount
            );
        }
    }
}