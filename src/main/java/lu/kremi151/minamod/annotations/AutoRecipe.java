package lu.kremi151.minamod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to automatically add crafting recipes for tools and armor
 * @author kremi151
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoRecipe {

	String material();
	Type type() default Type.AUTO;
	
	public static enum Type{
		AUTO,
		AXE,
		PICKAXE,
		SHOVEL,
		HOE,
		SWORD,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS
	}
}
