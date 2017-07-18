package lu.kremi151.minamod.block.tileentity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.inventory.container.ContainerSlotMachine;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import lu.kremi151.minamod.util.TaskRepeat;
import lu.kremi151.minamod.util.TextHelper;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class TileEntitySlotMachine extends TileEntity{
	
	private WeakReference<EntityPlayer> currentPlaying = null;
	//field_191525_da => IRON_NUGGET -.-
	private Item icons[] = new Item[] {Items.field_191525_da, Items.GOLD_NUGGET, Items.IRON_INGOT, Items.GOLD_INGOT, MinaItems.CHERRY, Items.DIAMOND};
	private int occurences[] = new int[] {4, 4, 3, 3, 1, 1};
	private final int prices[] = new int[] {1, 3, 5};
	private WeightedList<Integer> weightedIconIds;
	private boolean isTurning;
	private final WheelManager wheels = new WheelManager(5, 3);
	private boolean needs_sync = false;
	private int coinTray = 0;
	
	//Log data for reports:
	private SpinMode lastSpinMode = null;
	private int awardedForLastSpin = 0;

	public TileEntitySlotMachine() {
		fillWeigtedIcons();
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
		return currentPlaying != null && currentPlaying.get() != null;
	}
	
	public boolean setCurrentPlayer(EntityPlayer player) {
		if(player == null || !isInUse()) {
			this.currentPlaying = new WeakReference<>(player);
			if(coinTray > 0 && player != null) {
				int reward = coinTray;
				coinTray = 0;
				rewardPlayer(reward, false);
			}
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
		Random rand = new Random(System.currentTimeMillis());
		for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
			for(int j = 0 ; j < wheels.getDisplayWheelSize() ; j++) {
				wheels.setWheelContent(i, j, weightedIcons.randomElement(rand));
			}
		}
	}
	
	public int getPriceFor1Spin() {
		return prices[0];
	}
	
	public int getPriceFor3Spins() {
		return prices[1];
	}
	
	public int getPriceFor5Spins() {
		return prices[2];
	}
	
	public int getPriceForSpinMode(SpinMode mode) {
		return mode == SpinMode.FIVE ? prices[2] : (mode == SpinMode.THREE ? prices[1] : prices[0]);
	}
	
	private void rewardPlayer(int coins, boolean won) {
		EntityPlayer playing = getPlaying();
		if(playing != null && playing.hasCapability(ICoinHandler.CAPABILITY, null)) {
			if(((ICoinHandler)playing.getCapability(ICoinHandler.CAPABILITY, null)).depositCoins(coins)) {
				TextHelper.sendTranslateableChatMessage(playing, TextFormatting.GREEN, won?"gui.slot_machine.won_coins":"gui.slot_machine.found_coins", coins);
				needs_sync = true;
			}else {
				coinTray += coins;
			}
		}else {
			coinTray += coins;
		}
		awardedForLastSpin = coins;
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
		if(nbt.hasKey("Price1", 99)) {
			prices[0] = MathHelper.clamp(nbt.getInteger("Price1"), 1, 255);
		}else {
			prices[0] = 1;
		}
		if(nbt.hasKey("Price3", 99)) {
			prices[1] = MathHelper.clamp(nbt.getInteger("Price3"), 1, 255);
		}else {
			prices[1] = 3;
		}
		if(nbt.hasKey("Price5", 99)) {
			prices[2] = MathHelper.clamp(nbt.getInteger("Price5"), 1, 255);
		}else {
			prices[2] = 5;
		}
		if(nbt.hasKey("CoinTray", 99)) {
			coinTray = Math.max(nbt.getInteger("CoinTray"), 0);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt = writeSharedToNBT(super.writeToNBT(nbt));

		nbt.setInteger("Price1", prices[0]);
		nbt.setInteger("Price3", prices[1]);
		nbt.setInteger("Price5", prices[2]);
		nbt.setInteger("CoinTray", coinTray);
		
		return nbt;
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
	
	private int getCredits(EntityPlayer player) {
		if(player.hasCapability(ICoinHandler.CAPABILITY, null)) {
			return ((ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null)).getAmountCoins();
		}
		return 0;
	}
	
	public int getPlayingCredits() {
		EntityPlayer playing = getPlaying();
		if(playing != null) {
			return getCredits(playing);
		}else {
			return 0;
		}
	}
	
	private boolean isCherryIcon(int id) {
		return getItemIcon(id) == MinaItems.CHERRY;//TODO
	}
	
	private boolean withdrawCoins(EntityPlayer player, int amount) {
		if(player.hasCapability(ICoinHandler.CAPABILITY, null)) {
			if(((ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null)).withdrawCoins(amount)) {
				needs_sync = true;
				return true;
			}
		}
		return false;
	}
	
	public void turnSlots(SpinMode mode, Random rand) {
		if(!world.isRemote) {
			if(isTurning) {
				throw new IllegalStateException("Slot machine is already turning");
			}else {
				int price = getPriceForSpinMode(mode);
				EntityPlayer playing = getPlaying();
				if(playing != null) {
					if(withdrawCoins(playing, price)){
						isTurning = true;
						awardedForLastSpin = 0;
						new TaskRepeat(System.currentTimeMillis(), 100, new TaskTurnSlots(mode, rand)).enqueueServerTask();
					}else {
						TextHelper.sendTranslateableErrorMessage(playing, "gui.slot_machine.not_enough_coins");
					}
				}
			}
		}
	}
	
	public class TaskTurnSlots implements ITaskRunnable{
		
		private final int spacings[] = new int[wheels.getWheelCount()];
		private int turns;
		private final Random rand;
		private final SpinMode mode;

		private TaskTurnSlots(SpinMode mode, Random rand) {
			this.mode = mode;//TODO
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
				}else if(spacings[i] == -1) {
					int found_cherry = -1;
					for(int j = 0 ; j < wheels.getDisplayWheelSize() ; j++) {
						if(isCherryIcon(wheels.getWheelValue(i, j))) {
							found_cherry = wheels.getWheelValue(i, j);
							break;
						}
					}
					if(found_cherry > 0) {
						for(int j = 0 ; j < wheels.getDisplayWheelSize() ; j++) {
							wheels.setWheelContent(i, j, found_cherry);
						}
					}
				}
			}
			turns--;
			if(!cont) {
				task.setCanExecuteAgain(false);
				int win = evaluateResult();
				if(win > 0) {
					rewardPlayer(win, true);
				}
				isTurning = false;
				needs_sync = true;
			}
		}
		
		private int checkHLine(int pos) {
			int prev = -1;
			boolean cherry = false;
			for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
				int a = wheels.getWheelValue(i, pos);
				if(prev == -1){
					if(!isCherryIcon(a)) {
						prev = a;
					}
				}else if(prev != a && !isCherryIcon(a)) {
					return -1;
				}else if(isCherryIcon(a)) {
					cherry = true;
				}
			}
			return (prev == -1 && cherry) ? -2 : prev;
		}
		
		/*private int checkHLine(int pos) {
			int wheels[][] = new int[][] {
				new int[] {0, 0, 0},
				new int[] {2, 2, 1},
				new int[] {0, 0, 0},
				new int[] {0, 0, 0},
				new int[] {1, 1, 1}
			};
			int prev = -1;
			boolean cherry = false;
			for(int i = 0 ; i < wheels.length ; i++) {
				int a = wheels[i][pos];
				if(prev == -1){
					if(a != 0) {
						prev = a;
					}
				}else if(prev != a && a != 0) {
					return -1;
				}else if(a == 0) {
					cherry = true;
				}
			}
			return (prev == -1 && cherry) ? -2 : prev;
		}*/
		
		private int checkVLine(boolean invert) {
			int prev = -1;
			boolean cherry = false;
			for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
				int pos = invert ? (2 - Math.abs(i - 2)) : Math.abs(i - 2);
				int a = wheels.getWheelValue(i, pos);
				if(prev == -1){
					if(!isCherryIcon(a)) {
						prev = a;
					}
				}else if(prev != a && !isCherryIcon(a)) {
					return -1;
				}else if(isCherryIcon(a)) {
					cherry = true;
				}
			}
			return (prev == -1 && cherry) ? -2 : prev;
		}
		
		private int rowPrice(int iconId) {//TODO: Adjust
			float r = (float)occurences.length / (float)occurences[iconId];
			return (int) Math.max(25f * r, 1f);
		}
		
		private int evaluateResult() {
			System.out.println("Evaluate result for matrix:");
			for(int j = 0 ; j < wheels.getDisplayWheelSize() ; j++) {
				for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
					System.out.print(wheels.getWheelValue(i, j) + "\t");
				}
				System.out.println();
			}
			
			int win = 0;
			
			int eval = checkHLine(1);
			if(eval == -2) {//Cherry
				return 1000;//TODO:Adjust
			}else if(eval > 0) {
				win += rowPrice(eval);
			}
			
			if(mode == SpinMode.THREE || mode == SpinMode.FIVE) {
				for(int i = 0 ; i < 3 ; i += 2) {
					eval = checkHLine(i);
					if(eval == -2) {//Cherry
						return 1000;//TODO:Adjust
					}else if(eval > 0) {
						win += rowPrice(eval);
					}
				}
			}
			
			if(mode == SpinMode.FIVE) {
				for(int i = 0 ; i < 2 ; i ++) {
					eval = checkVLine(i == 1);
					if(eval == -2) {//Cherry
						return 1000;//TODO:Adjust
					}else if(eval > 0) {
						win += rowPrice(eval);
					}
				}
			}
			
			return win;
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
	
	public static enum SpinMode{
		ONE,
		THREE,
		FIVE
	}
	
	public StateSnapshot createStateSnapshot() {
		return new StateSnapshot(lastSpinMode, wheels.wheelData, awardedForLastSpin);
	}
	
	public static class StateSnapshot{
		public final SpinMode lastSpinMode;
		public final int wheelData[][];
		public final int awardedForLastSpin;
		
		private StateSnapshot(SpinMode lastSpinMode, int wheelData[][], int awardedForLastSpin) {
			this.lastSpinMode = lastSpinMode;
			this.wheelData = new int[wheelData.length][wheelData[0].length];
			this.awardedForLastSpin = awardedForLastSpin;
			for(int i = 0 ; i < wheelData.length ; i++) {
				for(int j = 0 ; j < wheelData[0].length ; j++) {
					this.wheelData[i][j] = wheelData[i][j];
				}
			}
		}
	}
}
