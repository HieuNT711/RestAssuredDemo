package runners;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "D:\\Vix\\Contactless\\RADemo\\RADemo\\src\\test\\java\\resources\\functionalTests\\E2ETest.feature",
        glue = {"stepDefinitions"},
        strict = true
)

public class TestRunner {

}