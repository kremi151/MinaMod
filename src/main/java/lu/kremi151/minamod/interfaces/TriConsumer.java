package lu.kremi151.minamod.interfaces;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

	void accept(A a, B b, C c);
	
}
