package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.NonNullList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Fail-fast reflection helper class to load field or methods accessible over reflection at the start up of the mod.
 * If one field or method is not accessible, the mod crashes. This eases the development of the mod.
 * @author kremi151
 *
 */
public class ReflectionLoader {

	private static final Field ENTITY_LIVING_BASE_IS_JUMPING;
	private static final Field DIMENSION_TYPE_CLAZZ;
	private static final Field BLOCK_LEAVES_LEAVES_FANCY;
	private static final Field INVENTORYPLAYER_ALLINVENTORIES;
	
	private static final Method CONTAINER_MERGE_ITEMSTACK;
	private static final Method CRITERIATRIGGERS_REGISTER;
	private static final Method ITEM_WRITTENBOOK_RESOLVE_CONTENTS;
	
	@SideOnly(Side.CLIENT)
	private static Method GUI_CONTAINER_DRAW_ITEMSTACK;
	@SideOnly(Side.CLIENT)
	private static Method GUI_CONTAINER_GET_SLOT_AT_POSITION;
	
	static{
		try {
			ENTITY_LIVING_BASE_IS_JUMPING = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");
			DIMENSION_TYPE_CLAZZ = ReflectionHelper.findField(DimensionType.class, "field_186077_g", "clazz");
			BLOCK_LEAVES_LEAVES_FANCY = ReflectionHelper.findField(BlockLeaves.class, "leavesFancy", "field_185686_c");
			INVENTORYPLAYER_ALLINVENTORIES = ReflectionHelper.findField(InventoryPlayer.class, "field_184440_g", "allInventories");
			
			CONTAINER_MERGE_ITEMSTACK = ReflectionHelper.findMethod(Container.class, "mergeItemStack", "func_75135_a", ItemStack.class, int.class, int.class, boolean.class);
			CRITERIATRIGGERS_REGISTER = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
			ITEM_WRITTENBOOK_RESOLVE_CONTENTS = ReflectionHelper.findMethod(ItemWrittenBook.class, "resolveContents", "func_179229_a", ItemStack.class, EntityPlayer.class);
			
			try {
				GUI_CONTAINER_DRAW_ITEMSTACK = findClientMethod(net.minecraft.client.gui.inventory.GuiContainer.class, "drawItemStack", "func_146982_a", ItemStack.class, int.class, int.class, String.class);
				GUI_CONTAINER_GET_SLOT_AT_POSITION = findClientMethod(net.minecraft.client.gui.inventory.GuiContainer.class, "getSlotAtPosition", "func_146975_c", int.class, int.class);
			}catch(NoSuchFieldError | NoSuchMethodError | NoClassDefFoundError e) {}//TODO: Find a better way to handle this}
		}catch (UnableToFindFieldException e) {
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
	
	public static void CriteriaTriggers_register(ICriterionTrigger trigger) {
		try {
			CRITERIATRIGGERS_REGISTER.setAccessible(true);
			CRITERIATRIGGERS_REGISTER.invoke(null, trigger);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void ItemWrittenBook_resolveContents(ItemWrittenBook instance, ItemStack stack, EntityPlayer player) {
		try {
			ITEM_WRITTENBOOK_RESOLVE_CONTENTS.setAccessible(true);
			ITEM_WRITTENBOOK_RESOLVE_CONTENTS.invoke(instance, stack, player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static List<NonNullList<ItemStack>> InventoryPlayer_getAllInventories(InventoryPlayer instance){
		try {
			INVENTORYPLAYER_ALLINVENTORIES.setAccessible(true);
			return (List<NonNullList<ItemStack>>) INVENTORYPLAYER_ALLINVENTORIES.get(instance);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
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
}
