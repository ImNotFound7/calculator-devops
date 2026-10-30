package in.ac.iiitb.calculator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    private final CalculatorService calculator;

    public CalculatorController(CalculatorService calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/api/v1/{operation:add|subtract|multiply|divide}")
    public Calculation calculate(@PathVariable String operation,
                                 @RequestParam double a, @RequestParam double b) {
        return new Calculation(operation, a, b, calculator.calculate(operation, a, b));
    }

    public record Calculation(String operation, double a, double b, double result) {}
}
