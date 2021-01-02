package telran.spring.services.impl;

import static telran.spring.api.ApiConstant.ADD;
import static telran.spring.api.ApiConstant.DIV;
import static telran.spring.api.ApiConstant.MUL;
import static telran.spring.api.ApiConstant.SUB;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import telran.spring.services.interfaces.Calculator;

@Service
public class CalculatorBasicArithmeticsImpl implements Calculator {

	@Override
	public int calculate(int op1, int op2, String operation) {
		try {
			switch (operation) {
			case ADD:
				return op1 + op2;
			case MUL:
				return op1 * op2;
			case DIV:
				return op1 / op2;
			case SUB:
				return op1 - op2;
			default:
				throw new IllegalArgumentException("Operation not supported");
			}
		} catch (Exception e) {
			throw new RuntimeErrorException(null, e.getMessage());
		}

	}

	@Override
	public String[] getSupportedOperations() {
		return new String[] { ADD, MUL, DIV, SUB };
	}

}
