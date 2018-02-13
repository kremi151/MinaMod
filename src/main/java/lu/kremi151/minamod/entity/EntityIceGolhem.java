package lu.kremi151.minamod.entity;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaSounds;
import lu.kremi151.minamod.entity.ai.EntityAILookToNearestFrostTemple;
import lu.kremi151.minamod.enums.EnumIceGolhemState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityIceGolhem extends EntityTameable{

    private static final DataParameter<Byte> STATE = EntityDataManager.<Byte>createKey(EntityIceGolhem.class, DataSerializers.BYTE);
	
	public static final String entityId = "ice_golhem";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);

	public EntityIceGolhem(World par1World) {
		super(par1World);
        this.setSize(8F / 16F, 14F / 16F);
		this.setCanPickUpLoot(true);
	}

	@Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(STATE, (byte)EnumIceGolhemState.PASSIVE.getStateId());
    }

	@Override
	protected void initEntityAI(){
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
		if(!isDisabled())this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		if(isPassive())this.tasks.addTask(2, new EntityAIPanic(this, 0.8D));
		
        if(!isDisabled() &! isZombie())this.tasks.addTask(0, new EntityAISwimming(this));
        if(!isDisabled())this.tasks.addTask(3, new EntityAIWander(this, 0.6D));//Movement Speed
        if(!isDisabled() &! isZombie())this.tasks.addTask(5, new EntityAILookToNearestFrostTemple(this));
        if(!this.isTamed() &! isZombie()){
            this.tasks.addTask(2, new EntityAITempt(this, 0.7D, MinaItems.NAMIE_FRUIT, false));
        }else{
            if(!isZombie())this.tasks.addTask(2, new EntityAIFollowOwner(this, 0.15d, 1f, 3f));
        }
        
        if(isZombie()){
        	this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        	this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIceGolhem.class, true));
        }
	}
	
	@Override
    protected SoundEvent getAmbientSound()
    {
        return MinaSounds.soundIceGolhemSay;
    }
	
	@Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return MinaSounds.soundIceGolhemHurt;
    }
	
	@Override
	protected SoundEvent getDeathSound()
    {
        return MinaSounds.soundIceGolhemDeath;
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
    {
		super.writeEntityToNBT(nbt);
		nbt.setByte("State", getState().getStateId());
		nbt.setDouble("MaxHealth", this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
    {
		super.readEntityFromNBT(nbt);
		if(nbt.hasKey("State", 99)){
			setState(EnumIceGolhemState.getFromId(nbt.getByte("State")));
		}
		if(nbt.hasKey("MaxHealth", 99)){
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(nbt.getDouble("MaxHealth"));
		}
		this.initEntityAI();
    }
	
	@Override
    public float getEyeHeight()
    {
        return this.height * 0.9F;
    }
	
	public boolean isDisabled(){
		return getState() == EnumIceGolhemState.DISABLED;
	}
	
	public boolean isZombie(){
		return getState() == EnumIceGolhemState.ZOMBIE;
	}
	
	public boolean isPassive(){
		return getState() == EnumIceGolhemState.PASSIVE;
	}
	
	public EnumIceGolhemState getState(){
		return EnumIceGolhemState.getFromId(this.getDataManager().get(STATE));
	}
	
	public void setState(EnumIceGolhemState state){
		this.getDataManager().set(STATE, state.getStateId());
		this.initEntityAI();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }
	
	@Override
	public void onUpdate()
    {
		super.onUpdate();
    }
	
	@Override
	public void onLivingUpdate()
    {
		super.onLivingUpdate();
//		if(this.isWet()){
//			this.worldObj.setBlockState(getPosition().down(), Blocks.ice.getDefaultState());
//		}
    }
	
	@Override
    protected boolean canEquipItem(ItemStack is)//Check fir itemer opzeraafen
    {
        return is.getItem() == MinaItems.NAMIE_FRUIT;
    }
	
	@Override
	public boolean isEntityInvulnerable(DamageSource ds){
		if(this.isDisabled() && !ds.isCreativePlayer()) return true;
		return super.isEntityInvulnerable(ds);
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
		ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == MinaItems.NAMIE_FRUIT && !player.capabilities.isCreativeMode &! isZombie())
        {
            if (itemstack.getCount() >= 1)
            {
                itemstack.shrink(1);
                boolean tame = false;
//                Player mp = Player.get(par1EntityPlayer);
//                if(mp.getSympathy() >= 75){
//                	tame = getRNG().nextInt(3) == 0;
//                }else if(mp.getSympathy() >= 50){
//                	tame = getRNG().nextInt(8) == 0;
//                }else if(mp.getSympathy() > 25){
                	tame = getRNG().nextInt(25) == 0;
//                }
                
                if(!this.isTamed() && tame){
                	this.setOwnerId(player.getUniqueID());
                    this.setTamed(true);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                    this.initEntityAI();
                }
                this.setHealth(this.getMaxHealth());
            }

            return true;
        }
        else if (itemstack.isEmpty() && this.isTamed() && isOwner(player) &! isZombie())
        {
        	boolean d = !isDisabled();
        	setState(d?EnumIceGolhemState.DISABLED:EnumIceGolhemState.PASSIVE);
        	this.initEntityAI();
        	return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

	@Override
	public EntityAgeable createChild(EntityAgeable partner) {
		return new EntityIceGolhem(partner.world);
	}
	
	private boolean isOwner(EntityPlayer p){
		return this.getOwner() == p;
	}
	

    @SideOnly(Side.CLIENT)
    public void effectHealing(){
    	showParticles(EnumParticleTypes.VILLAGER_HAPPY);
    }
	
    @SideOnly(Side.CLIENT)
    private void showParticles(EnumParticleTypes type)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(type, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2, new int[0]);
        }
    }

}
