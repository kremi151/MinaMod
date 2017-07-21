package lu.kremi151.minamod.util.nbtmath;

import java.util.function.BinaryOperator;

public abstract class SerializableOperator implements BinaryOperator<Number>{
	
	private final char operation;
	
	SerializableOperator(char operation) {
		this.operation = operation;
	}
	
	public char getOperation() {
		return operation;
	}
	
}