package lu.kremi151.minamod.block.tileentity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.inventory.container.ContainerSlotMachine;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import lu.kremi151.minamod.util.TaskRepeat;
import lu.kremi151.minamod.util.weightedlist.MutableWeightedList;
import lu.kremi151.minamod.util.weightedlist.WeightedList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntitySlotMachine extends TileEntity{
	
	private WeakReference<EntityPlayer> currentPlaying = null;
	//field_191525_da => IRON_NUGGET -.-
	private Item icons[] = new Item[] {Items.field_191525_da, Items.GOLD_NUGGET, Items.IRON_INGOT, Items.GOLD_INGOT, MinaItems.CHERRY, Items.DIAMOND};
	private int occurences[] = new int[] {4, 4, 3, 3, 2, 1};
	private WeightedList<Integer> weightedIconIds;
	private boolean isTurning;
	private final LinkedList<WeakReference<ContainerSlotMachine>> listeningContainers = new LinkedList<>();
	private final WheelManager wheels = new WheelManager(5, 3);
	private boolean needs_sync = false;

	public TileEntitySlotMachine() {
		fillWeigtedIcons();
	}
	
	public void registerListeningContainer(ContainerSlotMachine container) {
		listeningContainers.add(new WeakReference<>(container));
	}
		
	private void checkPlaying() {
		if(currentPlaying != null && currentPlaying.get() != null && currentPlaying.get().isDead) {
			currentPlaying = null;
		}
	}
	
	public EntityPlayer getPlaying() {
		return currentPlaying != null ? currentPlaying.get() : null;
	}
	
	public boolean isTurning() {
		return isTurning;
	}
	
	public boolean isInUse() {
		checkPlaying();
		return currentPlaying != null;
	}
	
	public boolean setCurrentPlayer(EntityPlayer player) {
		if(player == null || !isInUse()) {
			this.currentPlaying = new WeakReference<>(player);
			return true;
		}else {
			return false;
		}
	}
	
	public int getWheelCount() {
		return this.wheels.getWheelCount();
	}
	
	public int getDisplayWheelSize() {
		return this.wheels.getDisplayWheelSize();
	}
	
	public int getIconCount() {
		return this.icons.length;
	}
	
	public Item getItemIcon(int i) {
		return this.icons[i];
	}
	
	public int getWheelValue(int wheelIdx, int wheelPos) {
		return this.wheels.getWheelValue(wheelIdx, wheelPos);
	}
	
	public boolean needsSync() {
		return needs_sync || this.wheels.isDirty();
	}
	
	public void notifySynced() {
		this.wheels.unmarkDirty();
		needs_sync = false;
	}
	
	/*public int getWheelOffset(int wheelIndex) {
		return this.wheelOffsets[wheelIndex];
	}
	
	public void setWheelOffset(int wheelIndex, int offset) {
		this.wheelOffsets[wheelIndex] = (byte)MinaUtils.positiveModulo(offset, wheelLayout.length);
	}*/
	
	private void fillWeigtedIcons() {
		MutableWeightedList<Integer> weightedIcons = WeightedList.create();
		for(int i = 0 ; i < this.icons.length ; i++) {
			weightedIcons.add(i, (double)this.occurences[i]);
		}
		this.weightedIconIds = weightedIcons.immutable();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Icons", 9)) {
			NBTTagList icons = nbt.getTagList("Icons", 10);
			ArrayList<Object> parsed = new ArrayList<Object>();
			for(int i = 0 ; i < icons.tagCount() ; i++) {
				NBTTagCompound inbt = icons.getCompoundTagAt(i);
				if(inbt.hasKey("Item", 8) && inbt.hasKey("Occurence", 99)) {
					ResourceLocation iconRes = new ResourceLocation(inbt.getString("Item"));
					Item item = Item.REGISTRY.getObject(iconRes);
					if(item != null) {
						parsed.add(item);
						parsed.add(inbt.getInteger("Occurence"));
					}
				}
			}
			if(parsed.size() % 2 != 0)throw new IllegalStateException("This should not happen");
			if(parsed.size() > 32) {
				MinaMod.println("A slot machines maximum icon count is limited at 16. Additional icons will be dropped.");
			}
			final int bound = Math.min(parsed.size() / 2, 16);
			this.icons = new Item[bound];
			this.occurences = new int[bound];
			for(int i = 0 ; i < bound ; i++) {
				this.icons[i] = (Item) parsed.get(i * 2);
				this.occurences[i] = ((Integer) parsed.get((i * 2) + 1)).intValue();
			}
			fillWeigtedIcons();
		}
		/*if(nbt.hasKey("WheelOffsets", 9)) {
			NBTTagList wheelOffsets = nbt.getTagList("WheelOffsets", 1);
			for(int i = 0 ; i < this.wheelOffsets.length ; i++)this.wheelOffsets[i] = 0;
			for(int i = 0 ; i < Math.min(wheelOffsets.tagCount(), this.wheelOffsets.length) ; i++) {
				this.wheelOffsets[i] = ((NBTTagByte)wheelOffsets.get(i)).getByte();
			}
		}*/
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return writeSharedToNBT(super.writeToNBT(nbt));
	}
	
	private NBTTagCompound writeSharedToNBT(NBTTagCompound nbt) {
		if(this.icons.length != this.occurences.length) {
			throw new IllegalStateException("This should not happen");
		}else {
			NBTTagList icons = new NBTTagList();
			for(int i = 0 ; i < this.icons.length ; i++) {
				NBTTagCompound inbt = new NBTTagCompound();
				inbt.setString("Item", this.icons[i].getRegistryName().toString());
				inbt.setInteger("Occurence", this.occurences[i]);
				icons.appendTag(inbt);
			}
			nbt.setTag("Icons", icons);
		}
		/*NBTTagList wheelOffsets = new NBTTagList();
		for(int i = 0 ; i < this.wheelOffsets.length ; i++) {
			wheelOffsets.appendTag(new NBTTagByte(this.wheelOffsets[i]));
		}
		nbt.setTag("WheelOffsets", wheelOffsets);*/
		
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
	
	public void turnSlots(Random rand) {
		if(isTurning) {
			throw new IllegalStateException("Slot machine is already turning");
		}else {
			isTurning = true;
			new TaskRepeat(System.currentTimeMillis(), 100, new TaskTurnSlots(rand)).enqueueServerTask();
		}
	}
	
	public class TaskTurnSlots implements ITaskRunnable{
		
		private final int spacings[] = new int[wheels.getWheelCount()];
		private int turns;
		private final Random rand;

		private TaskTurnSlots(Random rand) {
			this.rand = rand;
			for(int i = 0 ; i < spacings.length ; i++) {
				spacings[i] = 5 + rand.nextInt(10);
			}
			turns = 30 + rand.nextInt(30);
		}

		@Override
		public void run(Task task, ProgressDispatcher progressDispatcher) {
			boolean cont = false;
			for(int i = 0 ; i < spacings.length ; i++) {
				if(turns > 0 || spacings[i]-- > 0) {
					cont = true;
					turnWheel(i);
				}
			}
			turns--;
			if(!cont) {
				task.setCanExecuteAgain(false);
				isTurning = false;
				needs_sync = true;
			}
		}
		
		private void turnWheel(int wheelIdx) {
			for(int j = wheels.getDisplayWheelSize() - 1 ; j > 0 ; j--) {
				setWheelIcon(wheelIdx, j, wheels.getWheelValue(wheelIdx, j - 1));
			}
			setWheelIcon(wheelIdx, 0, weightedIconIds.randomElement(rand));
		}
		
		private void setWheelIcon(int wheelIdx, int wheelPos, int icon) {
			wheels.setWheelContent(wheelIdx, wheelPos, icon);
		}
		
	}
	
	public static class WheelManager{
		@Deprecated
		private final int wheelData[][];//= new int[5][3]
		
		private boolean dirty = false;
		
		public WheelManager(int nWheels, int wheelSize) {
			wheelData = new int[nWheels][wheelSize];
		}
		
		public void setWheelContent(int wheelIdx, int wheelPos, int value) {
			int old = wheelData[wheelIdx][wheelPos];
			wheelData[wheelIdx][wheelPos] = value;
			if(old != value)dirty = true;
		}
		
		private void unmarkDirty() {
			dirty = false;
		}
		
		public boolean isDirty() {
			return dirty;
		}
		
		public int getWheelCount() {
			return this.wheelData.length;
		}
		
		public int getDisplayWheelSize() {
			return wheelData[0].length;
		}
		
		public int getWheelValue(int wheelIdx, int wheelPos) {
			return wheelData[wheelIdx][wheelPos];
		}
		
	}
}
