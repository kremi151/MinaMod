package lu.kremi151.minamod.block.tileentity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.events.SlotMachineEvent;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import lu.kremi151.minamod.util.TaskRepeat;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.util.nbtmath.MathParseException;
import lu.kremi151.minamod.util.nbtmath.NBTMathHelper;
import lu.kremi151.minamod.util.nbtmath.SerializableBinaryOperation;
import lu.kremi151.minamod.util.nbtmath.SerializableConditional;
import lu.kremi151.minamod.util.nbtmath.SerializableConstant;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableFunctionBase;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedLogical;
import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.nbtmath.util.ToBooleanFunction;
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
import net.minecraftforge.common.MinecraftForge;

public class TileEntitySlotMachine extends TileEntity{
	
	private static final NBTMathHelper nbtmath = NBTMathHelper.getDefaultInstance();
	
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
	private boolean isTurning = false;
	private TaskRepeat currentTask = null;
	private final WheelManager wheels = new WheelManager(5, 3);
	private boolean needs_sync = false, expandCherryItems = true;
	private int coinTray = 0;

	private SerializableFunctionBase<? extends NBTBase> customRowPriceFunction = null;
	private final SlotMachineEconomyHandler economyHandler;
	private final SlotMachineCoinHandler coinHandler;
	
	private final SerializableFunctionBase<? extends NBTBase> defaultRowPriceFunction = generateDefaultRowPriceFunction(200.0, 200.0);

	public TileEntitySlotMachine() {
		fillWeigtedIcons();
		
		for(SpinMode mode : SpinMode.values()) {
			prices[mode.ordinal()] = mode.getDefaultPrice();
		}
		
		SlotMachineEvent.SetEconomyHandler event = new SlotMachineEvent.SetEconomyHandler(new DefaultEconomyHandler());
		MinecraftForge.EVENT_BUS.post(event);
		this.economyHandler = event.getNewHandler();
		this.coinHandler = new SlotMachineCoinHandler();
	}
	
	public void setRowPriceFunction(SerializableFunctionBase<? extends NBTBase> func) {
		this.customRowPriceFunction = func;
		this.markDirty();
	}
	
	public SerializableFunctionBase<? extends NBTBase> parseFunction(NBTBase nbt) throws MathParseException{
		return nbtmath.parseFunction(nbt, context);
	}
	
	public EntityPlayer getPlaying() {
		return currentSession.getPlayerIfValid();
	}
	
	public boolean isTurning() {
		return isTurning || currentTask != null;
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
	
	public Icon getIcon(int i) {
		return this.icons[i];
	}
	
	public int getWheelValue(int wheelIdx, int wheelPos) {
		return this.wheels.getWheelValue(wheelIdx, wheelPos);
	}
	
	public boolean isWinningSlot(int wheelIdx, int wheelPos) {
		return this.wheels.isWinning(wheelIdx, wheelPos);
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
					prices[mode.ordinal()] = MathHelper.clamp(pnbt.getInteger(mode.name().toLowerCase()), 0, 255);
				}
			}
		}
		if(nbt.hasKey("CoinTray", 99)) {
			coinTray = Math.max(nbt.getInteger("CoinTray"), 0);
		}
		if(nbt.hasKey("RowPriceFunction")) {
			try {
				customRowPriceFunction = nbtmath.parseFunction(nbt.getTag("RowPriceFunction"), context);
			} catch (MathParseException e) {
				MinaMod.println("Could not parse row price function for slot machine at %s", pos.toString());
				e.printStackTrace();
			}
		}else if(nbt.hasKey("MaxWin", 99) || nbt.hasKey("CherryWin", 99)) {
			double maxWin = nbt.hasKey("MaxWin", 99) ? nbt.getDouble("MaxWin") : 200.0;
			double cherryWin = nbt.hasKey("CherryWin", 99) ? nbt.getDouble("CherryWin") : 200.0;
			customRowPriceFunction = generateDefaultRowPriceFunction(maxWin, cherryWin);
		}
		if(nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}
		if(nbt.hasKey("ExpandCherryIcon", 99)) {
			this.expandCherryItems = nbt.getBoolean("ExpandCherryIcon");
		}
	}
	
	public NBTTagCompound writeSlotMachineToNBT(NBTTagCompound nbt) {
		nbt = writeSharedToNBT(nbt);

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

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return writeSlotMachineToNBT(super.writeToNBT(nbt));
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
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return capability == ICoinHandler.CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return capability == ICoinHandler.CAPABILITY ? (T) coinHandler : super.getCapability(capability, facing);
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
	
	public void resetState() {
		this.isTurning = false;
		if(this.currentTask != null) {
			this.currentTask.setCanExecuteAgain(false);
			this.currentTask = null;
		}
		this.needs_sync = true;
		IBlockState state = this.world.getBlockState(pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	public int evaluateRowPrice(int iconId) {
		return (customRowPriceFunction != null ? customRowPriceFunction : defaultRowPriceFunction).apply(iconId, context).intValue();
	}
	
	public void turnSlots(SpinMode mode, Random rand, boolean instant) {
		if(!world.isRemote) {
			if(isTurning()) {
				throw new IllegalStateException("Slot machine is already turning");
			}else {
				int price = getPriceForSpinMode(mode);
				EntityPlayer playing = getPlaying();
				if(playing != null) {
					if(withdrawCoins(playing, price)){
						coinHandler.depositCoins(price);
						currentSession.currentWin -= price;
						wheels.clearWinnings();
						needs_sync = true;
						this.currentTask = new TaskRepeat(System.currentTimeMillis(), 100, new TaskTurnSlots(mode, rand));
						if(instant) {
							while(this.currentTask != null && this.currentTask.canExecuteAgain()) {
								this.currentTask.run(currentTask);//Simulate spinning
							}
						}else {
							isTurning = true;
							notifyClientTurnState(true);
							this.currentTask.enqueueServerTask();
						}
						
					}else {
						TextHelper.sendTranslateableErrorMessage(playing, "gui.slot_machine.not_enough_coins");
					}
				}
			}
		}
	}
	
	private SerializableFunctionBase<? extends NBTBase> generateDefaultRowPriceFunction(double maxWin, double cherryWin) {
		return new SerializableConditional(
				SerializableNamedLogical.createAndProvide("isCherry", id -> icons[id.intValue()].cherry),
				new SerializableConstant(cherryWin),//Individual line. End result is multiplied by 5.
				new SerializableFunction.Max(
						new SerializableBinaryOperation(
								new SerializableBinaryOperation(
										new SerializableConstant(1.0),
										new SerializableBinaryOperation(//TODO: Find a way to not have to manually program these lambdas here
												new SerializableBinaryOperation(
														SerializableNamedFunction.createAndProvide("iconWeight", id -> (double)icons[id.intValue()].weight),
														new SerializableConstant(1.0),
														NBTMathHelper.DIFFERENCE
														),
												SerializableNamedFunction.createAndProvide("maxWeight", id -> weightedIconIds.reduceWeight(0, (a, b) -> Math.max(a, b))),
												NBTMathHelper.DIVISION
												),
										NBTMathHelper.DIFFERENCE
										),
								new SerializableConstant(maxWin),
								NBTMathHelper.MULTIPLICATION
								),
						new SerializableConstant(1.0)
						)
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
				handleWinForPlayer(mode);
				isTurning = false;
				currentTask = null;
				notifyClientTurnState(false);
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
	
	private void handleWinForPlayer(SpinMode mode) {
		int win = evaluateResult(mode);
		if(win > 0) {
			if(coinHandler.canPayCoins(win) && coinHandler.withdrawCoins(win)) {
				rewardPlayer(win, true);
			}else {
				//TODO: error message
			}
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
	
	private void markEverythingWinning() {
		for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
			for(int j = 0 ; j < wheels.getDisplayWheelSize() ; j++) {
				wheels.setWheelWinning(i, j, true);
			}
		}
	}
	
	private void markHLineWinning(int line) {
		for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
			wheels.setWheelWinning(i, line, true);
		}
	}
	
	private void markVLineWinning(boolean invert) {
		for(int i = 0 ; i < wheels.getWheelCount() ; i++) {
			int pos = invert ? (2 - Math.abs(i - 2)) : Math.abs(i - 2);
			wheels.setWheelWinning(i, pos, true);
		}
	}
	
	private int evaluateResult(SpinMode mode) {
		int win = 0;
		
		int eval = checkHLine(1);
		if(eval == -2) {//Cherry
			win += evaluateRowPrice(wheels.getWheelValue(0, 1));
			markHLineWinning(1);
		}else if(eval >= 0) {
			win += evaluateRowPrice(eval);
			markHLineWinning(1);
		}
		
		if(mode == SpinMode.THREE || mode == SpinMode.FIVE) {
			for(int i = 0 ; i < 3 ; i += 2) {
				eval = checkHLine(i);
				if(eval == -2) {//Cherry
					win += evaluateRowPrice(wheels.getWheelValue(0, i));
					markHLineWinning(i);
				}else if(eval >= 0) {
					win += evaluateRowPrice(eval);
					markHLineWinning(i);
				}
			}
		}
		
		if(mode == SpinMode.FIVE) {
			for(int i = 0 ; i < 2 ; i ++) {
				eval = checkVLine(i == 1);
				if(eval == -2) {//Cherry
					win += evaluateRowPrice(wheels.getWheelValue(1, 1));
					markVLineWinning(i == 1);
				}else if(eval >= 0) {
					win += evaluateRowPrice(eval);
					markVLineWinning(i == 1);
				}
			}
		}
		
		return win;
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

		private final HashMap<String, Number> constants = new HashMap<>();
		private final HashMap<String, UnaryOperator<Number>> variables = new HashMap<>();
		private final HashMap<String, ToBooleanFunction<Number>> logicals = new HashMap<>();
		
		private SlotMachineContext() {
			variables.put("iconWeight", id -> icons[id.intValue()].weight);
			variables.put("iconCount", id -> icons.length);
			variables.put("totalWeight", id -> ((Double)weightedIconIds.totalWeight()).intValue());
			variables.put("maxWeight", id -> weightedIconIds.reduceWeight(0, (a, b) -> Math.max(a, b)));
			
			logicals.put("isCherry", id -> icons[id.intValue()].cherry);
		}

		@Override
		public Number resolveConstant(String name) {
			return constants.get(name);
		}

		@Override
		public UnaryOperator<Number> resolveVariable(String name) {
			return variables.get(name);
		}

		@Override
		public ToBooleanFunction<Number> resolveLogic(String name) {
			return logicals.get(name);
		}

		@Override
		public Set<String> constantNameSet() {
			return constants.keySet();
		}

		@Override
		public Set<String> variableNameSet() {
			return variables.keySet();
		}

		@Override
		public Set<String> logicNameSet() {
			return logicals.keySet();
		}
		
	};
	
	private class DefaultEconomyHandler implements SlotMachineEconomyHandler{

		@Override
		public boolean rewardPlayer(EntityPlayer player, int amount) {
			if(player != null && player.hasCapability(ICoinHandler.CAPABILITY, null)) {
				ICoinHandler coinHandler = (ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null);
				return coinHandler.depositCoins(amount);
			}
			return false;
		}

		@Override
		public boolean withdrawCoins(EntityPlayer player, int amount) {
			if(player.hasCapability(ICoinHandler.CAPABILITY, null)) {
				ICoinHandler coinHandler = (ICoinHandler)player.getCapability(ICoinHandler.CAPABILITY, null);
				return coinHandler.withdrawCoins(amount);
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
	
	private class SlotMachineCoinHandler implements ICoinHandler{

		@Override
		public int getAmountCoins() {
			return 0;//TODO: Implement
		}

		@Override
		public boolean canPayCoins(int amount) {
			return true;//TODO: Implement
		}

		@Override
		public boolean withdrawCoins(int amount, boolean simulate) {
			return true;//TODO: Implement
		}

		@Override
		public boolean depositCoins(int amount, boolean simulate) {
			return true;//TODO: Implement
		}
		
	}
}
