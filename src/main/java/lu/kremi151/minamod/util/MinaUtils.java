package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.block.BlockStool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MinaUtils {

	public static final double CONST_M = Math.PI / 180;
	public static final int COLOR_WHITE = convertRGBToDecimal(255, 255, 255);
	public static final int COLOR_GRAY = convertRGBToDecimal(128, 128, 128);
	public static final int COLOR_BLACK = convertRGBToDecimal(0, 0, 0);
	public static final AxisAlignedBB emptyBlockBounds = new AxisAlignedBB(0d, 0d, 0d, 0d, 0d, 0d);
	public static final AxisAlignedBB fullBlockBounds = new AxisAlignedBB(0d, 0d, 0d, 1d, 1d, 1d);

	public static int convertRGBToDecimal(int r, int g, int b) {
		return b + (g << 8) + (r << 16);
	}

	public static int[] convertDecimalToRGB(int dec) {
		int r = extractRedFromColor(dec);
		int g = extractGreenFromColor(dec);
		int b = extractBlueFromColor(dec);
		return new int[] { r, g, b };
	}
	
	public static float[] convertDecimalToRGBFloat(int color){
		return new float[]{
				(float)((color >> 16) & 255) / 255f,
				(float)((color >> 8) & 255) / 255f,
				(float)(color & 255) / 255f
		};
	}
	
	public static int invertColor(int dec){
		int r = 255 - extractRedFromColor(dec);
		int g = 255 - extractGreenFromColor(dec);
		int b = 255 - extractBlueFromColor(dec);
		return convertRGBToDecimal(r,g,b);
	}
	
	public static int extractRedFromColor(int color){
		return (color >> 16) & 255;
	}
	
	public static int extractGreenFromColor(int color){
		return (color >> 8) & 255;
	}
	
	public static int extractBlueFromColor(int color){
		return color & 255;
	}
	
	public static int positiveModulo(int n, int mod){
		if(n < 0){
			return mod + (n % mod);
		}else{
			return n % mod;
		}
	}

	public static boolean isRemote() { // NO_UCD (unused code)
		return FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER;
	}

	public static int getHeightValue(World world, int x, int z) {
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		int xInChunk = x - (chunk.xPosition * 16);
		int zInChunk = z - (chunk.zPosition * 16);
		return chunk.getHeightValue(xInChunk, zInChunk);
	}
	
	public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2){
		return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2) + Math.pow(Math.abs(z1 - z2), 2));
	}

	public static String getStackCustomName(ItemStack is) {
		if (is.getTagCompound() == null)
			return null;
		if (is.getTagCompound().hasKey("display")) {
			NBTTagCompound nbt = is.getTagCompound().getCompoundTag("display");

			if (nbt.hasKey("Name", 8)) {
				return nbt.getString("Name");
			}
		}
		return null;
	}

	@Deprecated
	/**
	 * Use {@link addItemStackToInventory} instead
	 */
	public static ItemStack mergeItemStack(ItemStack input, ItemStack target,
			int quantity) {
		if(input == null || target == null)throw new NullPointerException();
		ItemStack res = ItemStack.EMPTY;
		if (target.isEmpty() && !input.isEmpty()) {
			if(input.getMaxStackSize() == 1){
				res = input.copy();
				input.setCount(0);
				return res;
			}else{
				res = input.copy();
				res.setCount(0);
			}
		} else if (!target.isEmpty() && !input.isEmpty()) {
			res = target.copy();
		} else if (!target.isEmpty() && input.isEmpty()) {
			return target;
		} else {
			return ItemStack.EMPTY;
		}
		int t;
		if (!areItemsStackable(input, res, true))
			return ItemStack.EMPTY;
		if (quantity == 0)
			return res;
		if (input.getCount() < quantity) {
			quantity = input.getCount();
		}
		if (res.getMaxStackSize() - res.getCount() == 0) {
			return res;
		} else if (res.getMaxStackSize() - res.getCount() < quantity) {
			t = quantity - (res.getMaxStackSize() - res.getCount());
		} else {
			t = quantity;
		}
		input.shrink(t);
		res.grow(t);
		return res;
	}

	/**
	 * Checks if two item stacks can be stacked
	 * @param a The first stack
	 * @param b The second stack
	 * @param strict Set to true if the two item stacks should not be empty, false if an empty and a non empty stack can be stacked
	 * @return
	 */
	public static boolean areItemsStackable(ItemStack a, ItemStack b, boolean strict) {
		boolean b1a = a.isEmpty() && !b.isEmpty();
		boolean b1b = !a.isEmpty() && b.isEmpty();
		boolean b1 =  (b1a || b1b) || a.getItem() == b.getItem();//EVIL
		boolean b2 = a.getMetadata() == b.getMetadata();
		boolean b3 = a.getItemDamage() == b.getItemDamage();
		boolean b4 = (a.getTagCompound() == null && b.getTagCompound() == null) || ((a.getTagCompound() != null && b.getTagCompound() != null) && a.getTagCompound().equals(b.getTagCompound()));
		if(strict && (a.isEmpty() || b.isEmpty())){
			return false;
		}else{
			return (!strict && (b1a || b1b)) || (b1 && b2 && b3 && b4);
		}
		/*return a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata()
				&& a.getItemDamage() == b.getItemDamage()
				&& a.getTagCompound() == b.getTagCompound();*/
	}
	
	/**
	 * Attempts to add an {@link ItemStack} to an {@link IInventory}
	 * @param stack The item stack
	 * @param inventory The target inventory
	 * @return Returns the left over stack
	 */
	public static ItemStack addItemStackToInventory(ItemStack stack, IInventory inventory){
		if(stack.isEmpty())return ItemStack.EMPTY;
		final int length = inventory.getSizeInventory();
		ItemStack itemLeft = stack.copy();
		for(int i = 0 ; (i < length && !itemLeft.isEmpty()) ; i++){
			ItemStack slot = inventory.getStackInSlot(i);
			if(slot.isEmpty()){
				if(itemLeft.getCount() > inventory.getInventoryStackLimit()){
					ItemStack bye = itemLeft.copy();
					bye.setCount(inventory.getInventoryStackLimit());
					inventory.setInventorySlotContents(i, bye);
					itemLeft.shrink(inventory.getInventoryStackLimit());
				}else{
					inventory.setInventorySlotContents(i, itemLeft);
					return ItemStack.EMPTY;
				}
			}else if(MinaUtils.areItemsStackable(itemLeft, slot, true)){
				int left = Math.min(inventory.getInventoryStackLimit(), slot.getMaxStackSize()) - slot.getCount();
				if(left > 0){
					if(itemLeft.getCount() > left){
						slot.setCount(inventory.getInventoryStackLimit());
						itemLeft.shrink(left);
					}else{
						slot.grow(itemLeft.getCount());
						return ItemStack.EMPTY;
					}
				}
			}
		}
		return itemLeft;
	}

	public static int countItemsInInventory(IInventory inventory, Item item) { // NO_UCD (unused code)
		int cnt = 0;
		for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
			ItemStack itemstack = inventory.getStackInSlot(slot);
			if (!itemstack.isEmpty() && itemstack.getItem() == item) {
				cnt += itemstack.getCount();
			}
		}
		return cnt;
	}

	public static boolean consumeInventoryItems(IInventory inventory, // NO_UCD (unused code)
			Item item, int count) {
		boolean flag = false;
		for (int slot = 0, remain = count; slot < inventory.getSizeInventory(); ++slot) {
			ItemStack itemstack = inventory.getStackInSlot(slot);
			if (itemstack.getItem() == item) {
				if ((remain -= itemstack.getCount()) <= 0) {
					flag = true;
					break;
				}
			}
		}
		if (flag) {
			for (int slot = 0; count > 0 && slot < inventory.getSizeInventory(); ++slot) {
				ItemStack itemstack = inventory.getStackInSlot(slot);
				if (itemstack.getItem() == item) {
					if(itemstack.getCount() >= count){
						itemstack.shrink(count);
						count = 0;
					}else{
						count -= itemstack.getCount();
						inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
					}
				}
			}
		}
		return flag;
	}
	
	public static int bitwiseOr(Integer... values){
		int v = 0;
		for(int i = 0 ; i < values.length ; i++){
			v |= values[i];
		}
		return v;
	}
	
	public static int bitwiseInvert(int v){
		return ~v;
	}
	
	public static void makeLookAt(double px, double py, double pz , Entity me)
	{
		px += 0.5;
		py += 0.5;
		pz += 0.5;
		
	    double dirx = me.posX - px;
	    double diry = me.posY - py;
	    double dirz = me.posZ - pz;

	    double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

	    dirx /= len;
	    diry /= len;
	    dirz /= len;

	    double pitch = Math.asin(diry);
	    double yaw = Math.atan2(dirz, dirx);

	    //to degree
	    pitch = pitch * 180.0 / Math.PI;
	    yaw = yaw * 180.0 / Math.PI;

	    yaw += 90f;
	    me.rotationPitch = (float)pitch;
	    me.rotationYaw = (float)yaw;
	    
	    me.setPositionAndUpdate(me.posX, me.posY, me.posZ);
	}
	
	public static void applyLoresToItemStack(ItemStack is, String[] lores){
		NBTTagCompound root = is.getTagCompound();
		if(root == null){
			root = new NBTTagCompound();
			is.setTagCompound(root);
		}
		NBTTagCompound display = is.getOrCreateSubCompound("display");
		NBTTagList lore = new NBTTagList();
		for(int i = 0 ; i < lores.length ; i++){
			lore.appendTag(new NBTTagString(lores[i]));
		}
		display.setTag("Lore", lore);
	}
	
	public static <E> List<E> mergeLists(List<E> dest, List<E>... lists){
		for(int i = 0 ; i < lists.length ; i++){
			if(lists[i] != null)dest.addAll(lists[i]);
		}
		return dest;
	}
	
	public static boolean checkHasTag(Entity e, String tag){
		for(String s : e.getTags()){
			if(s.equals(tag))return true;
		}
		return false;
	}
	
	public static EntityItem dropItem(ItemStack stack, World world, double x, double y, double z){
		EntityItem ei = new EntityItem(world, x, y, z, stack);
    	world.spawnEntity(ei);
    	return ei;
	}
	
	public static int safeRandom(Random rand, int bound){
		if(bound > 0){
			return rand.nextInt(bound);
		}else{
			return -1;
		}
	}
	
	@Deprecated
	public static Item findItem(String modid, String name){
		return findItem(new ResourceLocation(modid, name));
	}
	
	public static Item findItem(ResourceLocation res){
		return Item.REGISTRY.getObject(res);
	}
	
	public static ResourceLocation[] deserializeResourceLocations(String modId, String... resources){
		ResourceLocation res[] = new ResourceLocation[resources.length];
		for(int i = 0 ; i < resources.length ; i++){
			String resource = resources[i];
			int j = resource.indexOf(':');
			if(j > 0){
				res[i] = new ResourceLocation(resource);
			}else{
				res[i] = new ResourceLocation(modId, resource);
			}
		}
		return res;
	}
	
	public static NonNullList<ItemStack> getNonEmptyStacks(IInventory inv){
		NonNullList<ItemStack> stacks = NonNullList.create();
		final int l = inv.getSizeInventory();
		for(int i = 0 ; i < l ; i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				stacks.add(stack);
			}
		}
		return stacks;
	}
	
	public static boolean makePlayerSitDown(EntityPlayer player, World world, double x, double y, double z){
		if(player.getRidingEntity() == null && world.isAirBlock(new BlockPos(x, y +1, z)) && world.isAirBlock(new BlockPos(x, y +2, z))){
			EntityArmorStand ee = new EntityArmorStand(world);
			ee.setInvisible(true);
			ee.setSilent(true);
			ee.setNoGravity(true);
			ee.addTag(BlockStool.DUMMY_TAG);
			ee.setPosition(x, y - 2.0, z);
			world.spawnEntity(ee);
			player.startRiding(ee, true);
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean makePlayerSitOnBlock(EntityPlayer player, World world, BlockPos blockPos){
		IBlockState block = world.getBlockState(blockPos);
		return makePlayerSitDown(player, world, blockPos.getX() + 0.5, blockPos.getY() + block.getBoundingBox(world, blockPos).maxY + 0.3, blockPos.getZ() + 0.5);
	}

}
