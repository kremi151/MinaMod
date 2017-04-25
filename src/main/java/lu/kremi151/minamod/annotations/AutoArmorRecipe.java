package lu.kremi151.minamod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoArmorRecipe {

	String value();
	String helmet() default "";
	String chestplate() default "";
	String leggings() default "";
	String boots() default "";
}
