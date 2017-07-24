package lu.kremi151.minamod.block.tileentity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockSlotMachine;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import lu.kremi151.minamod.util.TaskRepeat;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.util.nbtmath.MathParseException;
import lu.kremi151.minamod.util.nbtmath.NBTMathHelper;
import lu.kremi151.minamod.util.nbtmath.SerializableBinaryOperation;
import lu.kremi151.minamod.util.nbtmath.SerializableConstant;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedMapper;
import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.slotmachine.Icon;
import lu.kremi151.minamod.util.slotmachine.SlotMachineEconomyHandler;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import lu.kremi151.minamod.util.slotmachine.WheelManager;
import lu.kremi151.minamod.util.weightedlist.MutableWeightedList;
import lu.kremi151.minamod.util.weightedlist.WeightedList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class TileEntitySlotMachine extends TileEntity{
	
	private String customName = null;
	private Session currentSession = Session.empty();
	//field_191525_da => IRON_NUGGET -.-
	private Icon icons[] = new Icon[] {
			new Icon(Items.field_191525_da, 4, false),
			new Icon(Items.GOLD_NUGGET, 4, false),
			new Icon(Items.IRON_INGOT, 3, false),
			new Icon(Items.GOLD_INGOT, 3, false),
			new Icon(MinaItems.CHERRY, 1, true),
			new Icon(Items.DIAMOND, 1, false)
	};
	private final int prices[] = new int[SpinMode.values().length];
	private WeightedList<Integer> weightedIconIds;
	private boolean isTurning;
	private final WheelManager wheels = new WheelManager(5, 3);
	private boolean needs_sync = false, expandCherryItems = true;
	private int coinTray = 0;

	private SerializableFunction<? extends NBTBase> customRowPriceFunction = null;
	private SlotMachineEconomyHandler economyHandler = new DefaultEconomyHandler();
	
	//Log data for reports:
	private SpinMode lastSpinMode = null;
	private int awardedForLastSpin = 0;
	
	private final SerializableFunction<? extends NBTBase> defaultRowPriceFunction = generateDefaultRowPriceFunction(200.0);

	public TileEntitySlotMachine() {
		fillWeigtedIcons();
		
		for(SpinMode mode : SpinMode.values()) {
			prices[mode.ordinal()] = mode.getDefaultPrice();
		}
	}
	
	public void setEconomyHandler(@Nonnull SlotMachineEconomyHandler handler) {
		if(handler == null) {
			throw new NullPointerException();
		}
		this.economyHandler = handler;
	}
	
	public SlotMachineEconomyHandler getEconomyHandler() {
		return this.economyHandler;
	}
	
	public void setRowPriceFunction(SerializableFunction<? extends NBTBase> func) {
		this.customRowPriceFunction = func;
		this.markDirty();
	}
	
	public SerializableFunction<? extends NBTBase> parseFunction(NBTBase nbt) throws MathParseException{
		return NBTMathHelper.parseFunction(nbt, context);
	}
	
	public EntityPlayer getPlaying() {
		return currentSession.getPlayerIfValid();
	}
	
	public boolean isTurning() {
		return isTurning;
	}
	
	public boolean isInUse() {
		return currentSession.isValid();
	}
	
	public Optional<Session> setCurrentPlayer(EntityPlayer player) {
		if(player == null || !isInUse()) {
			this.currentSession = new Session(player);
			if(coinTray > 0 && player != null) {
				int reward = coinTray;
				coinTray = 0;
				rewardPlayer(reward, false);
			}
			return Optional.of(this.currentSession);
		}else {
			return Optional.empty();
		}
	}
	
	public int getSessionWin() {
		return currentSession.currentWin;
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
		return this.icons[i].icon;
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
	
	private void fillWeigtedIcons() {
		MutableWeightedList<Integer> weightedIcons = WeightedList.create();
		for(int i = 0 ; i < this.icons.length ; i++) {
			weightedIcons.add(i, (double)this.icons[i].weight);
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
		return prices[SpinMode.ONE.ordinal()];
	}
	
	public int getPriceFor3Spins() {
		return prices[SpinMode.THREE.ordinal()];
	}
	
	public int getPriceFor5Spins() {
		return prices[SpinMode.FIVE.ordinal()];
	}
	
	public int getPriceForSpinMode(SpinMode mode) {
		return prices[mode.ordinal()];
	}
	
	private void rewardPlayer(int coins, boolean won) {
		EntityPlayer playing = getPlaying();
		if(economyHandler.rewardPlayer(playing, coins)) {
			TextHelper.sendTranslateableChatMessage(playing, TextFormatting.GREEN, won?"gui.slot_machine.won_coins":"gui.slot_machine.found_coins", coins);
			currentSession.currentWin += coins;
			needs_sync = true;
		}else {
			coinTray += coins;
		}
		
		awardedForLastSpin = coins;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.isTurning = nbt.getBoolean("IsTurning");
		if(nbt.hasKey("Icons", 9)) {
			NBTTagList icons = nbt.getTagList("Icons", 10);
			ArrayList<Icon> parsed = new ArrayList<>();
			for(int i = 0 ; i < icons.tagCount() ; i++) {
				NBTTagCompound inbt = icons.getCompoundTagAt(i);
				try {
					parsed.add(new Icon(inbt));
				} catch (NBTException e) {
					e.printStackTrace();
				}
			}
			if(parsed.size() > 16) {
				MinaMod.println("A slot machines maximum icon count is limited at 16. Additional icons will be dropped.");
			}
			final int bound = Math.min(parsed.size(), 16);
			this.icons = new Icon[bound];
			for(int i = 0 ; i < bound ; i++) {
				this.icons[i] = parsed.get(i);
			}
			fillWeigtedIcons();
		}
		for(SpinMode mode : SpinMode.values()) {
			prices[mode.ordinal()] = mode.getDefaultPrice();
		}
		if(nbt.hasKey("Prices", 10)) {
			NBTTagCompound pnbt = nbt.getCompoundTag("Prices");
			for(SpinMode mode : SpinMode.values()) {
				if(pnbt.hasKey(mode.name().toLowerCase(), 99)) {
					prices[mode.ordinal()] = MathHelper.clamp(pnbt.getInteger(mode.name().toLowerCase()), 1, 255);
				}
			}
		}
		if(nbt.hasKey("CoinTray", 99)) {
			coinTray = Math.max(nbt.getInteger("CoinTray"), 0);
		}
		if(nbt.hasKey("RowPriceFunction")) {
			try {
				customRowPriceFunction = NBTMathHelper.parseFunction(nbt.getTag("RowPriceFunction"), context);
			} catch (MathParseException e) {
				MinaMod.println("Could not parse row price function for slot machine at %s", pos.toString());
				e.printStackTrace();
			}
		}else if(nbt.hasKey("MaxWin", 99)) {
			customRowPriceFunction = generateDefaultRowPriceFunction(nbt.getDouble("MaxWin"));
		}
		if(nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}
		if(nbt.hasKey("ExpandCherryIcon", 99)) {
			this.expandCherryItems = nbt.getBoolean("ExpandCherryIcon");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt = writeSharedToNBT(super.writeToNBT(nbt));

		NBTTagCompound pnbt = new NBTTagCompound();
		for(SpinMode mode : SpinMode.values()) {
			pnbt.setInteger(mode.name().toLowerCase(), prices[mode.ordinal()]);
		}
		nbt.setTag("Prices", pnbt);
		nbt.setInteger("CoinTray", coinTray);
		if(customRowPriceFunction != null)nbt.setTag("RowPriceFunction", customRowPriceFunction.serialize());
		nbt.setBoolean("ExpandCherryIcon", expandCherryItems);
		
		return nbt;
	}
	
	private NBTTagCompound writeSharedToNBT(NBTTagCompound nbt) {
		if(customName != null) {
			nbt.setString("CustomName", customName);
		}
		
		NBTTagList icons = new NBTTagList();
		for(int i = 0 ; i < this.icons.length ; i++) {
			icons.appendTag(this.icons[i].serialize());
		}
		nbt.setTag("Icons", icons);
		nbt.setBoolean("IsTurning", isTurning);
		
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
        notifyClientTurnState(isTurning);
	}
	
	public String getCustomName() {
		return customName;
	}
	
	private int getCredits(EntityPlayer player) {
		return economyHandler.getCredits(player);
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
		return this.icons[id].cherry;
	}
	
	private boolean withdrawCoins(EntityPlayer player, int amount) {
		if(economyHandler.withdrawCoins(player, amount)) {
			needs_sync = true;
			return true;
		}
		return false;
	}
	
	private void notifyClientTurnState(boolean state) {
		IBlockState oldState = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), oldState, oldState/*.withProperty(BlockSlotMachine.IS_TURNING, state)*/, 3);
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
						notifyClientTurnState(true);
						awardedForLastSpin = 0;
						currentSession.currentWin -= price;
						needs_sync = true;
						new TaskRepeat(System.currentTimeMillis(), 100, new TaskTurnSlots(mode, rand)).enqueueServerTask();
					}else {
						TextHelper.sendTranslateableErrorMessage(playing, "gui.slot_machine.not_enough_coins");
					}
				}
			}
		}
	}
	
	private SerializableFunction<? extends NBTBase> generateDefaultRowPriceFunction(double maxWin) {
		return new SerializableNamedFunction.Max(
				new SerializableBinaryOperation(
						new SerializableBinaryOperation(
								new SerializableConstant(1.0),
								new SerializableBinaryOperation(//TODO: Find a way to not have to manually program these lambdas here
										new SerializableBinaryOperation(
												new SerializableNamedMapper(id -> (double)icons[id.intValue()].weight, "iconWeight"),
												new SerializableConstant(1.0),
												NBTMathHelper.DIFFERENCE
												),
										new SerializableNamedMapper(id -> weightedIconIds.reduceWeight(0, (a, b) -> Math.max(a, b)), "maxWeight"),
										NBTMathHelper.DIVISION
										),
								NBTMathHelper.DIFFERENCE
								),
						new SerializableConstant(maxWin),
						NBTMathHelper.MULTIPLICATION
						),
				new SerializableConstant(1.0)
				);
	}
	
	public class TaskTurnSlots implements ITaskRunnable{
		
		private final int spacings[] = new int[wheels.getWheelCount()];
		private int turns;
		private final Random rand;
		private final SpinMode mode;

		private TaskTurnSlots(SpinMode mode, Random rand) {
			this.mode = mode;
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
					if(expandCherryItems) {
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
			}
			turns--;
			if(!cont) {
				task.setCanExecuteAgain(false);
				int win = evaluateResult();
				if(win > 0) {
					rewardPlayer(win, true);
				}
				isTurning = false;
				notifyClientTurnState(false);
				needs_sync = true;
			}
		}
		
		private int checkHLine(int pos) {
			int prev = -1;
			int cherryId = -1;
			for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
				int a = wheels.getWheelValue(i, pos);
				if(prev == -1 && !isCherryIcon(a)){
					prev = a;
				}else if(prev != a && !isCherryIcon(a)) {
					return -1;
				}else if(isCherryIcon(a)) {
					if(cherryId == -1) {
						cherryId = a;
					}else if(cherryId != a){
						return -1;
					}
				}
			}
			return (prev == -1 && cherryId != -1) ? -2 : prev;
		}
		
		private int checkVLine(boolean invert) {
			int prev = -1;
			int cherryId = -1;
			for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
				int pos = invert ? (2 - Math.abs(i - 2)) : Math.abs(i - 2);
				int a = wheels.getWheelValue(i, pos);
				if(prev == -1 && !isCherryIcon(a)){
					prev = a;
				}else if(prev != a && !isCherryIcon(a)) {
					return -1;
				}else if(isCherryIcon(a)) {
					if(cherryId == -1) {
						cherryId = a;
					}else if(cherryId != a){
						return -1;
					}
				}
			}
			return (prev == -1 && cherryId != -1) ? -2 : prev;
		}
		
		private int rowPrice(int iconId) {
			return (customRowPriceFunction != null ? customRowPriceFunction : defaultRowPriceFunction).apply(iconId).intValue();
		}
		
		private int evaluateResult() {
			int win = 0;
			
			int eval = checkHLine(1);
			if(eval == -2) {//Cherry
				return 1000;//TODO:Adjust
			}else if(eval >= 0) {
				win += rowPrice(eval);
			}
			
			if(mode == SpinMode.THREE || mode == SpinMode.FIVE) {
				for(int i = 0 ; i < 3 ; i += 2) {
					eval = checkHLine(i);
					if(eval == -2) {//Cherry
						return 1000;//TODO:Adjust
					}else if(eval >= 0) {
						win += rowPrice(eval);
					}
				}
			}
			
			if(mode == SpinMode.FIVE) {
				for(int i = 0 ; i < 2 ; i ++) {
					eval = checkVLine(i == 1);
					if(eval == -2) {//Cherry
						return 1000;//TODO:Adjust
					}else if(eval >= 0) {
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
	
	public static class Session{
		private final WeakReference<EntityPlayer> playerRef;
		private int currentWin = 0;
		
		private Session(EntityPlayer player) {
			this.playerRef = new WeakReference<>(player);
		}
		
		public boolean isValid() {
			return getPlayerIfValid() != null;
		}
		
		private EntityPlayer getPlayerIfValid() {
			EntityPlayer pl = playerRef.get();
			if(pl != null && !pl.isDead) {
				return pl;
			}
			return null;
		}
		
		private static Session empty() {
			return new Session(null);
		}
	}
	
	private final Context context = new SlotMachineContext();
	
	private class SlotMachineContext implements Context{

		@Override
		public Number getConstant(String name) {
			return null;
		}

		@Override
		public UnaryOperator<Number> getVariable(String name) {
			if(name.equalsIgnoreCase("iconWeight")) {
				return id -> icons[id.intValue()].weight;
			}else if(name.equalsIgnoreCase("iconCount")) {
				return id -> icons.length;
			}else if(name.equalsIgnoreCase("totalWeight")) {
				return id -> ((Double)weightedIconIds.totalWeight()).intValue();
			}else if(name.equalsIgnoreCase("maxWeight")) {
				return id -> weightedIconIds.reduceWeight(0, (a, b) -> Math.max(a, b));
			}else {
				return null;
			}
		}

		@Override
		public void applyMappings(BiConsumer<String, Object> consumer) {
			consumer.accept("iconWeight", (UnaryOperator<Number>)id -> icons[id.intValue()].weight);
			consumer.accept("iconCount", icons.length);
			consumer.accept("totalWeight", (UnaryOperator<Number>)id -> ((Double)weightedIconIds.totalWeight()).intValue());
			consumer.accept("maxWeight", (UnaryOperator<Number>)id -> weightedIconIds.reduceWeight(0, (a, b) -> Math.max(a, b)));
		}
		
	};
	
	private class DefaultEconomyHandler implements SlotMachineEconomyHandler{

		@Override
		public boolean rewardPlayer(EntityPlayer player, int amount) {
			if(player != null && player.hasCapability(ICoinHandler.CAPABILITY, null)) {
				return ((ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null)).depositCoins(amount);
			}else {
				return false;
			}
		}

		@Override
		public boolean withdrawCoins(EntityPlayer player, int amount) {
			if(player.hasCapability(ICoinHandler.CAPABILITY, null)) {
				return ((ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null)).withdrawCoins(amount);
			}
			return false;
		}

		@Override
		public int getCredits(EntityPlayer player) {
			if(player.hasCapability(ICoinHandler.CAPABILITY, null)) {
				return ((ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null)).getAmountCoins();
			}
			return 0;
		}
		
	}
	
	
	
	//TODO: Remove when out of beta
	public StateSnapshot createStateSnapshot() {
		return new StateSnapshot(lastSpinMode, wheels.createRawWheelSnapshot(), awardedForLastSpin);
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
