package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReflectionLoader {

	private static final Field FML_MISSING_MAPPING_ACTION;
	private static final Field ENTITY_LIVING_BASE_IS_JUMPING;
	private static final Field DIMENSION_TYPE_CLAZZ;
	private static final Field BLOCK_LEAVES_LEAVES_FANCY;
	
	private static final Method CONTAINER_MERGE_ITEMSTACK;
	
	@SideOnly(Side.CLIENT)
	private static Field GUI_ACHIEVEMENT_ACHIEVEMENT_TITLE;
	@SideOnly(Side.CLIENT)
	private static Field GUI_ACHIEVEMENT_ACHIEVEMENT_DESCRIPTION;
	@SideOnly(Side.CLIENT)
	private static Field GUI_ACHIEVEMENT_NOTIFICATION_TIME;
	@SideOnly(Side.CLIENT)
	private static Field GUI_ACHIEVEMENT_ACHIEVEMENT;
	@SideOnly(Side.CLIENT)
	private static Field GUI_ACHIEVEMENT_PERMANENT_NOTIFICATION;
	
	@SideOnly(Side.CLIENT)
	private static Method GUI_CONTAINER_DRAW_ITEMSTACK;
	@SideOnly(Side.CLIENT)
	private static Method GUI_CONTAINER_GET_SLOT_AT_POSITION;
	
	static{
		try {
			FML_MISSING_MAPPING_ACTION = FMLMissingMappingsEvent.MissingMapping.class.getDeclaredField("action");
			ENTITY_LIVING_BASE_IS_JUMPING = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");
			DIMENSION_TYPE_CLAZZ = ReflectionHelper.findField(DimensionType.class, "field_186077_g", "clazz");
			BLOCK_LEAVES_LEAVES_FANCY = ReflectionHelper.findField(BlockLeaves.class, "leavesFancy", "field_185686_c");
			
			CONTAINER_MERGE_ITEMSTACK = ReflectionHelper.findMethod(Container.class, "mergeItemStack", "func_75135_a", ItemStack.class, int.class, int.class, boolean.class);
			
			try {
				GUI_CONTAINER_DRAW_ITEMSTACK = findClientMethod(net.minecraft.client.gui.inventory.GuiContainer.class, "drawItemStack", "func_146982_a", ItemStack.class, int.class, int.class, String.class);
				GUI_CONTAINER_GET_SLOT_AT_POSITION = findClientMethod(net.minecraft.client.gui.inventory.GuiContainer.class, "getSlotAtPosition", "func_146975_c", int.class, int.class);

				GUI_ACHIEVEMENT_ACHIEVEMENT_TITLE = findClientField(net.minecraft.client.gui.achievement.GuiAchievement.class, "achievementTitle", "field_146268_i");
				GUI_ACHIEVEMENT_ACHIEVEMENT_DESCRIPTION = findClientField(net.minecraft.client.gui.achievement.GuiAchievement.class, "achievementDescription", "field_146265_j");
				GUI_ACHIEVEMENT_NOTIFICATION_TIME = findClientField(net.minecraft.client.gui.achievement.GuiAchievement.class, "notificationTime", "field_146263_l");
				GUI_ACHIEVEMENT_ACHIEVEMENT = findClientField(net.minecraft.client.gui.achievement.GuiAchievement.class, "achievement", "field_146266_k");
				GUI_ACHIEVEMENT_PERMANENT_NOTIFICATION = findClientField(net.minecraft.client.gui.achievement.GuiAchievement.class, "permanentNotification", "field_146262_n");
			}catch(NoSuchFieldError | NoSuchMethodError | NoClassDefFoundError e) {}//TODO: Find a better way to handle this}
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("At least one needed reflection field does not exists", e);
		} catch (UnableToFindMethodException e){
			throw new IllegalStateException("At least one needed reflection method does not exists", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("At least one needed reflection field or method has thrown a security exception", e);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static Method findClientMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nullable String methodObfName, Class<?>... parameterTypes) {
		return ReflectionHelper.findMethod(clazz, methodName, methodObfName, parameterTypes);
	}
	
	@SideOnly(Side.CLIENT)
	private static Field findClientField(@Nonnull Class<?> clazz, @Nonnull String... fieldNames) {
		return ReflectionHelper.findField(clazz, fieldNames);
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
	
	public static boolean Container_mergeItemStack(Container container, ItemStack stack, int destMinIncl, int destMaxExcl, boolean inverse) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		CONTAINER_MERGE_ITEMSTACK.setAccessible(true);
		return (Boolean)CONTAINER_MERGE_ITEMSTACK.invoke(container, stack, destMinIncl, destMaxExcl, inverse);
	}
	
	@SideOnly(Side.CLIENT)
	public static void GuiContainer_drawItemStack(net.minecraft.client.gui.inventory.GuiContainer container, ItemStack stack, int x, int y, String altText) {
		try {
			GUI_CONTAINER_DRAW_ITEMSTACK.setAccessible(true);
			GUI_CONTAINER_DRAW_ITEMSTACK.invoke(container, stack, x, y, altText);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static Slot GuiContainer_getSlotAtPosition(net.minecraft.client.gui.inventory.GuiContainer container, int x, int y) {
		try {
			return (Slot) GUI_CONTAINER_GET_SLOT_AT_POSITION.invoke(container, x, y);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("This should not happen", e);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void GuiAchievement_postCustomAchievement(net.minecraft.client.gui.achievement.GuiAchievement gui, String title, String description, long notificationTime, Achievement ach, boolean permanentNotification) throws IllegalArgumentException, IllegalAccessException {
		GUI_ACHIEVEMENT_ACHIEVEMENT_TITLE.setAccessible(true);
		GUI_ACHIEVEMENT_ACHIEVEMENT_TITLE.set(gui, title);
		GUI_ACHIEVEMENT_ACHIEVEMENT_DESCRIPTION.setAccessible(true);
		GUI_ACHIEVEMENT_ACHIEVEMENT_DESCRIPTION.set(gui, description);
		GUI_ACHIEVEMENT_ACHIEVEMENT.setAccessible(true);
		GUI_ACHIEVEMENT_ACHIEVEMENT.set(gui, ach);
		GUI_ACHIEVEMENT_PERMANENT_NOTIFICATION.setAccessible(true);
		GUI_ACHIEVEMENT_PERMANENT_NOTIFICATION.setBoolean(gui, permanentNotification);
		GUI_ACHIEVEMENT_NOTIFICATION_TIME.setAccessible(true);
		//System.out.println("Duration: " + notificationTime);
		GUI_ACHIEVEMENT_NOTIFICATION_TIME.setLong(gui, notificationTime);
		//System.out.println("New val: " + GUI_ACHIEVEMENT_NOTIFICATION_TIME.getLong(gui));
	}
}
