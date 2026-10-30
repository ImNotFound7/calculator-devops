package in.ac.iiitb.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorApiTest {
    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @CsvSource({"add, 5.0", "subtract, -1.0", "multiply, 6.0", "divide, 0.6666666666666666"})
    void returnsOperationInputsAndResult(String operation, double result) throws Exception {
        mvc.perform(get("/api/v1/" + operation).param("a", "2").param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value(operation))
                .andExpect(jsonPath("$.a").value(2.0))
                .andExpect(jsonPath("$.b").value(3.0))
                .andExpect(jsonPath("$.result").value(result));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/api/v1/add?b=2",
        "/api/v1/add?a=1",
        "/api/v1/add?a=&b=2",
        "/api/v1/add?a=abc&b=2",
        "/api/v1/add?a=1&b=abc",
        "/api/v1/add?a=NaN&b=2",
        "/api/v1/add?a=1&b=Infinity",
        "/api/v1/add?a=1e309&b=2",
        "/api/v1/multiply?a=1e308&b=10",
        "/api/v1/divide?a=1&b=0",
        "/api/v1/divide?a=1&b=-0.0"
    })
    void rejectsInvalidRequestsWithStructuredErrors(String path) throws Exception {
        mvc.perform(get(path))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail", not(emptyOrNullString())));
    }

    @Test
    void exposesActuatorHealth() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void doesNotRouteUnknownOperations() throws Exception {
        mvc.perform(get("/api/v1/power?a=2&b=3"))
                .andExpect(status().isNotFound());
    }
}
