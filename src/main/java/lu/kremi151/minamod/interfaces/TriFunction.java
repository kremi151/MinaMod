package lu.kremi151.minamod.interfaces;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {

	R apply(A a, B b, C c);
	
}
