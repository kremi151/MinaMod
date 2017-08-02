package lu.kremi151.minamod.interfaces;

@FunctionalInterface
@Deprecated
/**
 * Consider using {@link java.util.Supplier} instead
 * Will be deleted soon
 * @author michm
 *
 * @param <T>
 */
public interface ICallback<T> {

	T callback();
}
