package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectionLoader {

	private static final Field FML_MISSING_MAPPING_ACTION;
	private static final Field ENTITY_LIVING_BASE_IS_JUMPING;
	private static final Field DIMENSION_TYPE_CLAZZ;
	
	static{
		try {
			FML_MISSING_MAPPING_ACTION = FMLMissingMappingsEvent.MissingMapping.class.getDeclaredField("action");
			ENTITY_LIVING_BASE_IS_JUMPING = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");
			DIMENSION_TYPE_CLAZZ = ReflectionHelper.findField(DimensionType.class, "field_186077_g", "clazz");
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("At least one reflection field does not exists", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("At least one reflection field or method has thrown a security exception", e);
		}
	}
	
	public static void MissingMapping_setAction(MissingMapping mapping, FMLMissingMappingsEvent.Action action) throws IllegalArgumentException, IllegalAccessException{
		FML_MISSING_MAPPING_ACTION.setAccessible(true);
		FML_MISSING_MAPPING_ACTION.set(mapping, action);
	}
	
	public static boolean EntityLivingBase_isJumping(EntityLivingBase entity) throws IllegalArgumentException, IllegalAccessException{
		ENTITY_LIVING_BASE_IS_JUMPING.setAccessible(true);
		return ENTITY_LIVING_BASE_IS_JUMPING.getBoolean(entity);
	}
	
	public static void DimensionType_setClazz(DimensionType instance, Class<? extends WorldProvider> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		DIMENSION_TYPE_CLAZZ.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(DIMENSION_TYPE_CLAZZ, DIMENSION_TYPE_CLAZZ.getModifiers() & ~Modifier.FINAL);
	    DIMENSION_TYPE_CLAZZ.set(instance, clazz);
	    modifiersField.setInt(DIMENSION_TYPE_CLAZZ, DIMENSION_TYPE_CLAZZ.getModifiers() | Modifier.FINAL);
	    DIMENSION_TYPE_CLAZZ.setAccessible(false);
	}
}
