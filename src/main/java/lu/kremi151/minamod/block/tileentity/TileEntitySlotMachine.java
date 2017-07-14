package lu.kremi151.minamod.block.tileentity;

import java.util.ArrayList;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntitySlotMachine extends TileEntity{
	
	private EntityPlayer currentPlaying = null;
	//field_191525_da => IRON_NUGGET -.-
	private Item icons[] = new Item[] {Items.field_191525_da, Items.GOLD_NUGGET, MinaItems.GOLDEN_COIN, Items.IRON_INGOT, Items.GOLD_INGOT, MinaItems.CHERRY, Items.DIAMOND};
	private int reward[] = new int[] {1, 2, 3, 4, 5, 10, 15};
	private final byte wheelOffsets[] = new byte[5];

	public TileEntitySlotMachine() {
		
	}
	
	private void checkPlaying() {
		if(currentPlaying != null && currentPlaying.isDead) {
			currentPlaying = null;
		}
	}
	
	public boolean isInUse() {
		checkPlaying();
		return currentPlaying != null;
	}
	
	public boolean setCurrentPlayer(EntityPlayer player) {
		if(!isInUse()) {
			this.currentPlaying = player;
			return true;
		}else {
			return false;
		}
	}
	
	public int getWheelSize() {
		return this.icons.length;
	}
	
	public Item getItemIcon(int i) {
		return this.icons[i];
	}
	
	public int getRewardForIcon(int i) {
		return this.reward[i];
	}
	
	public int getWheelOffset(int wheelIndex) {
		return this.wheelOffsets[wheelIndex];
	}
	
	public void setWheelOffset(int wheelIndex, int offset) {
		this.wheelOffsets[wheelIndex] = (byte)MinaUtils.positiveModulo(offset, wheelOffsets.length);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Icons", 9)) {
			NBTTagList icons = nbt.getTagList("Icons", 10);
			ArrayList<Object> parsed = new ArrayList<Object>();
			for(int i = 0 ; i < icons.tagCount() ; i++) {
				NBTTagCompound inbt = icons.getCompoundTagAt(i);
				if(inbt.hasKey("Item", 8) && inbt.hasKey("Reward", 99)) {
					ResourceLocation iconRes = new ResourceLocation(inbt.getString("Item"));
					Item item = Item.REGISTRY.getObject(iconRes);
					if(item != null) {
						parsed.add(item);
						parsed.add(inbt.getInteger("Reward"));
					}
				}
			}
			if(parsed.size() % 2 != 0)throw new IllegalStateException("This should not happen");
			final int bound = parsed.size() / 2;
			this.icons = new Item[bound];
			this.reward = new int[bound];
			for(int i = 0 ; i < bound ; i++) {
				this.icons[i] = (Item) parsed.get(i * 2);
				this.reward[i] = ((Integer) parsed.get((i * 2) + 1)).intValue();
			}
		}
		if(nbt.hasKey("WheelOffsets", 9)) {
			NBTTagList wheelOffsets = nbt.getTagList("WheelOffsets", 1);
			for(int i = 0 ; i < this.wheelOffsets.length ; i++)this.wheelOffsets[i] = 0;
			for(int i = 0 ; i < Math.min(wheelOffsets.tagCount(), this.wheelOffsets.length) ; i++) {
				this.wheelOffsets[i] = ((NBTTagByte)wheelOffsets.get(i)).getByte();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return writeSharedToNBT(super.writeToNBT(nbt));
	}
	
	private NBTTagCompound writeSharedToNBT(NBTTagCompound nbt) {
		if(this.icons.length != this.reward.length) {
			throw new IllegalStateException("This should not happen");
		}else {
			NBTTagList icons = new NBTTagList();
			for(int i = 0 ; i < this.icons.length ; i++) {
				NBTTagCompound inbt = new NBTTagCompound();
				inbt.setString("Item", this.icons[i].getRegistryName().toString());
				inbt.setInteger("Reward", this.reward[i]);
				icons.appendTag(inbt);
			}
			nbt.setTag("Icons", icons);
		}
		NBTTagList wheelOffsets = new NBTTagList();
		for(int i = 0 ; i < this.wheelOffsets.length ; i++) {
			wheelOffsets.appendTag(new NBTTagByte(this.wheelOffsets[i]));
		}
		nbt.setTag("WheelOffsets", wheelOffsets);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		return writeSharedToNBT(super.getUpdateTag());
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}
}
