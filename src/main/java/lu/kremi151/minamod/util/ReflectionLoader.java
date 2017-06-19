package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

public class ReflectionLoader {

	private static final Field FML_MISSING_MAPPING_ACTION;
	private static final Field ENTITY_LIVING_BASE_IS_JUMPING;
	private static final Field DIMENSION_TYPE_CLAZZ;
	private static final Field BLOCK_LEAVES_LEAVES_FANCY;
	
	static{
		try {
			FML_MISSING_MAPPING_ACTION = FMLMissingMappingsEvent.MissingMapping.class.getDeclaredField("action");
			ENTITY_LIVING_BASE_IS_JUMPING = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");
			DIMENSION_TYPE_CLAZZ = ReflectionHelper.findField(DimensionType.class, "field_186077_g", "clazz");
			BLOCK_LEAVES_LEAVES_FANCY = ReflectionHelper.findField(BlockLeaves.class, "leavesFancy", "field_185686_c");
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("At least one needed reflection field does not exists", e);
		} catch (UnableToFindMethodException e){
			throw new IllegalStateException("At least one needed reflection method does not exists", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("At least one needed reflection field or method has thrown a security exception", e);
		}
	}
	
	public static void MissingMapping_setAction(MissingMapping mapping, FMLMissingMappingsEvent.Action action) throws IllegalAccessException{
		FML_MISSING_MAPPING_ACTION.setAccessible(true);
		FML_MISSING_MAPPING_ACTION.set(mapping, action);
	}
	
	public static boolean EntityLivingBase_isJumping(EntityLivingBase entity) throws IllegalAccessException{
		ENTITY_LIVING_BASE_IS_JUMPING.setAccessible(true);
		return ENTITY_LIVING_BASE_IS_JUMPING.getBoolean(entity);
	}
	
	public static void DimensionType_setClazz(DimensionType instance, Class<? extends WorldProvider> clazz) throws NoSuchFieldException, SecurityException, IllegalAccessException{
		DIMENSION_TYPE_CLAZZ.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(DIMENSION_TYPE_CLAZZ, DIMENSION_TYPE_CLAZZ.getModifiers() & ~Modifier.FINAL);
	    DIMENSION_TYPE_CLAZZ.set(instance, clazz);
	    modifiersField.setInt(DIMENSION_TYPE_CLAZZ, DIMENSION_TYPE_CLAZZ.getModifiers() | Modifier.FINAL);
	    DIMENSION_TYPE_CLAZZ.setAccessible(false);
	}
	
	public static boolean BlockLeaves_isFancyLeaves(BlockLeaves block) throws IllegalAccessException{
		BLOCK_LEAVES_LEAVES_FANCY.setAccessible(true);
		return BLOCK_LEAVES_LEAVES_FANCY.getBoolean(block);
	}
	
	public static void BlockLeaves_setFancyLeaves(BlockLeaves block, boolean v) throws IllegalAccessException{
		BLOCK_LEAVES_LEAVES_FANCY.setAccessible(true);
		BLOCK_LEAVES_LEAVES_FANCY.set(block, v);
	}
}
