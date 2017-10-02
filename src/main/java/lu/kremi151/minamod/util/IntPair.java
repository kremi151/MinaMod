package lu.kremi151.minamod.util;

public class IntPair {

	public final int a, b;
	
	public IntPair(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}else if(obj == this) {
			return true;
		}else if(obj.getClass() == IntPair.class) {
			return ((IntPair)obj).a == this.a && ((IntPair)obj).b == this.b;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return a ^ b;
	}
}
