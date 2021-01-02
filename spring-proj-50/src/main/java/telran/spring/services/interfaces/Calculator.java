package telran.spring.services.interfaces;

public interface Calculator {
	int calculate(int op1, int op2, String operation);

	String[] getSupportedOperations();
}
