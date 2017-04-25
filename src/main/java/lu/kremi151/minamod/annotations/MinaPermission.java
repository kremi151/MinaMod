package lu.kremi151.minamod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.server.permission.DefaultPermissionLevel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinaPermission {

	public String desc();
	public DefaultPermissionLevel lvl() default DefaultPermissionLevel.ALL;
	
}
