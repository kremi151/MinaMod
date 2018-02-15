package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.item.ItemColoredWrittenBook;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBook extends BaseTileEntity{
	
	private static final int DEFAULT_COLOR = MinaUtils.convertRGBToDecimal(117, 50, 30);
	
	private ItemStack theBook = ItemStack.EMPTY;
	private int color = DEFAULT_COLOR;
	
	private boolean isValidBook(ItemStack stack) {
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemWrittenBook;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBookColor() {
		return color;
	}
	
	public ItemStack getBookItem() {
		if(isValidBook(theBook)) {
			return theBook;
		}else {
			return ItemStack.EMPTY;
		}
	}
	
	public boolean setBookItem(ItemStack stack) {
		if(isValidBook(stack)) {
			theBook = stack;
			if(stack.getItem() instanceof ItemColoredWrittenBook) {
				color = ItemColoredWrittenBook.getBookColor(stack);
			}else {
				color = DEFAULT_COLOR;
			}
			markDirty();
			if(world != null)sync();
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		readBookData(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return writeBookData(super.writeToNBT(nbt));
	}
	
	private NBTTagCompound writeBookData(NBTTagCompound nbt) {
		if(isValidBook(theBook))nbt.setTag("Book", theBook.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("Color", color);
		return nbt;
	}
	
	private void readBookData(NBTTagCompound nbt) {
		if(nbt.hasKey("Book", 10)) {
			theBook = new ItemStack(nbt.getCompoundTag("Book"));
		}
		if(nbt.hasKey("Color", 99)) {
			color = nbt.getInteger("Color");
		}else {
			color = DEFAULT_COLOR;
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, writeBookData(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readBookData(packet.getNbtCompound());
        sync();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeBookData(super.getUpdateTag());
	}
}
