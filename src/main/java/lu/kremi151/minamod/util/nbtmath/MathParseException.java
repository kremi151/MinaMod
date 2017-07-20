package lu.kremi151.minamod.util.nbtmath;

public class MathParseException extends Exception{
	
	MathParseException(String msg) {
		super(msg);
	}
	
	MathParseException(Exception e) {
		super(e);
	}
	
	MathParseException(String msg, Exception e) {
		super(msg, e);
	}
}