package lu.kremi151.minamod.exceptions;

public class InvalidFormatException extends RuntimeException{

	public InvalidFormatException() {}
	
	public InvalidFormatException(String msg) {
		super(msg);
	}
	
	public InvalidFormatException(String msg, Throwable e) {
		super(msg, e);
	}

	public InvalidFormatException(Throwable e) {
		super(e);
	}
}
