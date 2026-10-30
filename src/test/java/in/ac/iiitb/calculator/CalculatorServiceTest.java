package in.ac.iiitb.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceTest {
    private final CalculatorService calculator = new CalculatorService();

    @ParameterizedTest
    @CsvSource({
        "add, 2, 3, 5",
        "add, -2.5, 1, -1.5",
        "subtract, 2, 3, -1",
        "multiply, -2.5, 4, -10",
        "multiply, 0, 5, 0",
        "divide, 7, 2, 3.5",
        "divide, 0, 2, 0"
    })
    void calculatesEachOperation(String operation, double a, double b, double expected) {
        assertEquals(expected, calculator.calculate(operation, a, b));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.0})
    void rejectsDivisionByZero(double divisor) {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate("divide", 1, divisor));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void rejectsNonFiniteInputs(double value) {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate("add", value, 1));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate("add", 1, value));
    }

    @Test
    void rejectsOverflow() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate("multiply", Double.MAX_VALUE, 2));
    }

    @Test
    void rejectsUnknownOperations() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate("power", 2, 3));
    }
}
