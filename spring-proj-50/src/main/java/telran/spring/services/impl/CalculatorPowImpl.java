package telran.spring.services.impl;

import static telran.spring.api.ApiConstant.POW;

import org.springframework.stereotype.Service;

import telran.spring.services.interfaces.Calculator;

@Service
public class CalculatorPowImpl implements Calculator {

	@Override
	public int calculate(int op1, int op2, String operation) {
		try {
			return (int) Math.pow(op1, op2);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public String[] getSupportedOperations() {
		return new String[] { POW };
	}

}
