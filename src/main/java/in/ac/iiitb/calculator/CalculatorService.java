package in.ac.iiitb.calculator;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    public double calculate(String operation, double a, double b) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            throw new IllegalArgumentException("Parameters 'a' and 'b' must be finite numbers.");
        }
        double result = switch (operation) {
            case "add" -> a + b;
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown calculator operation.");
        };
        if (!Double.isFinite(result)) {
            throw new IllegalArgumentException("The result exceeds the supported numeric range.");
        }
        return result;
    }
}
