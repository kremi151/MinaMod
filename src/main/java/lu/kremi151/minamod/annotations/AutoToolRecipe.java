package lu.kremi151.minamod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoToolRecipe {

	String value();
	String sword() default "";
	String axe() default "";
	String pickaxe() default "";
	String shovel() default "";
	String hoe() default "";
}
