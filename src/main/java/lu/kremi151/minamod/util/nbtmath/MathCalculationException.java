package lu.kremi151.minamod.util.nbtmath;

public class MathCalculationException extends RuntimeException{
	
	MathCalculationException(String msg) {
		super(msg);
	}
	
	MathCalculationException(Exception e) {
		super(e);
	}
	
	MathCalculationException(String msg, Exception e) {
		super(msg, e);
	}
	
}
