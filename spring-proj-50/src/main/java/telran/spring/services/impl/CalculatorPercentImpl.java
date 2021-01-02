package telran.spring.services.impl;

import static telran.spring.api.ApiConstant.PER;

import org.springframework.stereotype.Service;

import telran.spring.services.interfaces.Calculator;

@Service
public class CalculatorPercentImpl implements Calculator {

	@Override
	public int calculate(int op1, int op2, String operation) {
		try {
			return Math.abs(op1 * 100 / op2);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String[] getSupportedOperations() {
		return new String[] { PER };
	}

}
