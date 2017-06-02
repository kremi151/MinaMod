package lu.kremi151.minamod.capabilities;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.attribute.AttributeModifierStatSpeed;
import lu.kremi151.minamod.enums.EnumPlayerStat;
import lu.kremi151.minamod.packet.message.MessageShowOverlay;
import lu.kremi151.minamod.util.BlissHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityPlayerStats implements IPlayerStats{
	
	public static final UUID SPEED_MODIFIER_ID = UUID.fromString("3ef9b0ec-f250-4939-883e-3bed5f0b3732");

	public final static int NORMAL_STAT_MINIMUM = 64;
	public final static int NORMAL_STAT_MAXIMUM = 255;
	public final static int DISTRIBUTABLE_POINTS = 2 * NORMAL_STAT_MAXIMUM;
	public final static int MAX_EXP_BAR = 60;
	
	@CapabilityInject(IPlayerStats.class)
	public static final Capability<IPlayerStats> CAPABILITY = null;
	
	private final EntityPlayer player;

	private static final DataParameter<Integer> statToDM[];
	private static final DataParameter<Integer> effortBar = EntityDataManager.<Integer>createKey(EntityPlayer.class, DataSerializers.VARINT);
	private final AttributeModifierStatSpeed speedMod;
	
	static{
		statToDM = new DataParameter[EnumPlayerStat.values().length * 2];
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			statToDM[stat.ordinal() * 2] = EntityDataManager.<Integer>createKey(EntityPlayer.class, DataSerializers.VARINT);
			statToDM[(stat.ordinal() * 2) + 1] = EntityDataManager.<Integer>createKey(EntityPlayer.class, DataSerializers.VARINT);
		}
	}
	
	private static DataParameter<Integer> getActualData(EnumPlayerStat stat){
		return statToDM[stat.ordinal() * 2];
	}
	
	private static DataParameter<Integer> getTrainingData(EnumPlayerStat stat){
		return statToDM[(stat.ordinal() * 2) + 1];
	}
	
	public CapabilityPlayerStats(EntityPlayer player){
		this.player = player;
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			registerData(statToDM[stat.ordinal() * 2], 128);
			registerData(statToDM[(stat.ordinal() * 2) + 1], 0);
		}
		registerData(effortBar, 0);
		this.speedMod = new AttributeModifierStatSpeed(SPEED_MODIFIER_ID, this);
	}
	
	private <T> void registerData(DataParameter<T> key, T value){
		try{
			player.getDataManager().register(key, value);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}

	@Override
	public int getStats(EnumPlayerStat stat) {
		return MathHelper.clamp(player.getDataManager().get(getActualData(stat)), NORMAL_STAT_MINIMUM, NORMAL_STAT_MAXIMUM);
	}

	@Override
	public int getTrainingStats(EnumPlayerStat stat) {
		return clampTrainingStats(stat, player.getDataManager().get(getTrainingData(stat)));
	}

	@Override
	public int getMaxTrainingStats(EnumPlayerStat stat) {
		return NORMAL_STAT_MAXIMUM - getStats(stat);
	}

	@Override
	public int getMinTrainingStats(EnumPlayerStat stat) {
		return -getStats(stat);
	}

	@Override
	public int getPointsLeft() {
		return Math.max(DISTRIBUTABLE_POINTS - getTotalPoints(), 0);
	}

	@Override
	public int getMaxDistributablePoints() {
		return DISTRIBUTABLE_POINTS;
	}

	@Override
	public int getEffortBar() {
		return player.getDataManager().get(effortBar);
	}

	@Override
	public int getMaxEffortBar() {
		return MAX_EXP_BAR;
	}

	@Override
	public void addEffort(int amount) {
		int exp = Math.max(0, getEffortBar() + amount);
		while(exp > MAX_EXP_BAR){
			exp -= MAX_EXP_BAR;
			onLevelUp();
		}
		player.getDataManager().set(effortBar, exp);
	}
	
	private void onLevelUp(){
		final int points = BlissHelper.getBliss(player).chanceOneIn(10) ? 5 : 3;
		if(distribute(player.getRNG(), points) > 0 && player instanceof EntityPlayerMP){
			MinaMod.getMinaMod().getPacketDispatcher().sendTo(new MessageShowOverlay(0, 1000), (EntityPlayerMP) player);
		}
	}
	
	@Override
	public void setEffort(int amount){
		player.getDataManager().set(effortBar, MathHelper.clamp(amount, 0, MAX_EXP_BAR));
	}

	@Override
	public void copyFrom(IPlayerStats source) {
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			this.setStats(stat, source.getStats(stat), source.getTrainingStats(stat));
			this.setEffort(source.getEffortBar());
		}
	}
	
	private int getTotalPoints(){
		int sum = 0;
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			sum += getStats(stat) + getTrainingStats(stat);
		}
		return sum;
	}

	@Override
	public void applyTraining(EnumPlayerStat stat, int amount) {
		player.getDataManager().set(getTrainingData(stat), clampTrainingStats(stat, player.getDataManager().get(getTrainingData(stat)) + amount));
	}
	
	@Override
	public void resetStats(){
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			player.getDataManager().set(getActualData(stat), 128);
			player.getDataManager().set(getTrainingData(stat), 0);
		}
		player.getDataManager().set(effortBar, 0);
	}

	@Override
	public int distribute(Random rand, int amount) {
		ArrayList<EnumPlayerStat> avd = new ArrayList<EnumPlayerStat>();
		int res = 0;
		for(int i = 0 ; i < amount ; i++){
			avd.clear();
			
			for(EnumPlayerStat stat : EnumPlayerStat.values()){
				if(getTrainingStats(stat) != 0){
					avd.add(stat);
				}
			}
			
			final int size = avd.size();
			if(size == 0){
				return res;
			}else{
				EnumPlayerStat stat = avd.get(rand.nextInt(size));
				int val = getTrainingStats(stat);
				if(val > 0){
					setStats(stat, getStats(stat) + 1, val - 1);
					res++;
				}else if(val < 0){
					setStats(stat, getStats(stat) - 1, val + 1);
					res++;
				}
			}
		}
		return res;
	}
	
	private void setStats(EnumPlayerStat stat, int actual, int training){
		player.getDataManager().set(getActualData(stat), MathHelper.clamp(actual, NORMAL_STAT_MINIMUM, NORMAL_STAT_MAXIMUM));
		player.getDataManager().set(getTrainingData(stat), clampTrainingStats(stat, training));
	}
	
	private int clampTrainingStats(EnumPlayerStat stat, int value){
		return MathHelper.clamp(value, getMinTrainingStats(stat), getMaxTrainingStats(stat));
	}
	
	static class Storage implements Capability.IStorage<IPlayerStats>{

		@Override
		public NBTBase writeNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			if(instance instanceof CapabilityPlayerStats){
				CapabilityPlayerStats ps = (CapabilityPlayerStats) instance;
				
				for(EnumPlayerStat stat : EnumPlayerStat.values()){
					nbt.setInteger(stat.getId(), ps.getStats(stat));
					nbt.setInteger("t_" + stat.getId(), ps.getTrainingStats(stat));
				}
			}
			return nbt;
		}

		@Override
		public void readNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side, NBTBase nbt_) {
			if(instance instanceof CapabilityPlayerStats && nbt_ instanceof NBTTagCompound){
				CapabilityPlayerStats ps = (CapabilityPlayerStats) instance;
				NBTTagCompound nbt = (NBTTagCompound) nbt_;
				
				for(EnumPlayerStat stat : EnumPlayerStat.values()){
					ps.setStats(stat, readInt(nbt, stat.getId(), 128), readInt(nbt, "t_" + stat.getId(), 0));
				}
			}
		}
		
		private int readInt(NBTTagCompound nbt, String key, int def){
			if(nbt.hasKey(key, 99)){
				return nbt.getInteger(key);
			}else{
				return def;
			}
		}
		
	}

	@Override
	public void initAttributes() {
		this.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_ID);
		this.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedMod);
	}

}
