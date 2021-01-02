package telran.spring.controllers;

import static telran.spring.api.ApiConstant.CALCULATOR;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import telran.spring.api.dto.CalculateData;
import telran.spring.services.interfaces.Calculator;

@RestController
public class FlexibleCalculatorControler {
	@Autowired
	Map<String, Calculator> calculators;

	@PostMapping(CALCULATOR)
	ResponseEntity<?> calculate(@RequestBody CalculateData data) {
		Calculator calc = calculators.get(data.operation);
		if (calc == null) {
			return ResponseEntity.badRequest().body("unknown operation");
		}
		try {
			return ResponseEntity.ok(calc.calculate(data.op1, data.op2, data.operation));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(501).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostConstruct
	public void displaySupportedOpperations() {
		Map<String, Calculator> new_map = new HashMap<String, Calculator>();

		for (Calculator calc : calculators.values()) {
			String[] operations = calc.getSupportedOperations();
			if (operations == null)
				continue;
			for (String operation : operations) {
				new_map.putIfAbsent(operation, calc);
			}
		}
		calculators.clear();
		calculators.putAll(new_map);
		System.out.println("Supported Operations: " + calculators.keySet());

	}
}
