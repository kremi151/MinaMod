package lu.kremi151.minamod.util.nbtmath;

public class MathFunctionException extends RuntimeException{
	
	MathFunctionException(String msg) {
		super(msg);
	}
	
	MathFunctionException(Exception e) {
		super(e);
	}
	
	MathFunctionException(String msg, Exception e) {
		super(msg, e);
	}
}
