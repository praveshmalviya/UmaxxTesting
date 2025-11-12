package org.umaxxtesting.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "org.umaxxtesting.stepdefinitions",
        plugin = {"pretty","html:target/cucumber-report.html"}
)
public class Runnertest extends AbstractTestNGCucumberTests {
}
