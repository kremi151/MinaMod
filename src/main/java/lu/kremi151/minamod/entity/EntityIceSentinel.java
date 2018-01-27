package lu.kremi151.minamod.entity;

import java.util.HashMap;

import com.google.common.base.Predicate;

import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPotions;
import lu.kremi151.minamod.entity.ai.EntityAIBossDivide;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityIceSentinel extends EntityMob implements IRangedAttackMob{
	
    private static final DataParameter<Integer> INVULNERABILITY_TICKS = EntityDataManager.<Integer>createKey(EntityIceSentinel.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> DIVISION_POWER = EntityDataManager.<Byte>createKey(EntityIceSentinel.class, DataSerializers.BYTE);
    
	public static final String entityId = "ice_sentinel";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);
	
	private final static int MAX_INV_TICKS = 400;
	private final static double MAX_HEALTH = 400d;
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true).setCreateFog(true).setPlayEndBossMusic(true);

    private static final Predicate attackEntitySelector = new Predicate()
    {
        public boolean check(Entity entity)
        {
        	if(entity instanceof EntityLivingBase){
        		EntityLivingBase lb = (EntityLivingBase)entity;
                return !(entity instanceof EntityIceSentinel) &! (entity instanceof EntityIceGolhem) &! lb.isPotionActive(MinaPotions.FREEZE);
        	}
        	return true;
        }
    	@Override
        public boolean apply(Object obj)
        {
            return this.check((Entity)obj);
        }
    };

	public EntityIceSentinel(World worldIn) {
		super(worldIn);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(INVULNERABILITY_TICKS, Integer.valueOf(MAX_INV_TICKS));
        this.getDataManager().register(DIVISION_POWER, Byte.valueOf((byte)3));
    }
	
	@Override
	protected void initEntityAI(){
    	this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBossDivide(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
    	this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
    	this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    	this.tasks.addTask(7, new EntityAILookIdle(this));
    	this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    	this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, attackEntitySelector));
	}
	
	public int getInvulnerabilityTicksLeft(){
		return this.getDataManager().get(INVULNERABILITY_TICKS);
	}
	
	public void setInvulnerabilityTicks(int t){
		this.getDataManager().set(INVULNERABILITY_TICKS, t);
	}
	
	public byte getDivisionPower(){
		return this.getDataManager().get(DIVISION_POWER);
	}
	
	public void setDivisionPower(byte n){
		this.getDataManager().set(DIVISION_POWER, n);
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource ds){
		if(ds == DamageSource.FALL)return true;
		return getInvulnerabilityTicksLeft() > 0;
	}

	@Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
		if(getDivisionPower() == 0){//TODO
			ItemStack is = new ItemStack(MinaItems.HARMONY_PEARL, 1);
			HashMap<Enchantment, Integer> emap = new HashMap<Enchantment, Integer>();
			emap.put(MinaEnchantments.FUSION, 1);
			EnchantmentHelper.setEnchantments(emap, is);
	        EntityItem entityitem = this.entityDropItem(is, 0f);

	        if (entityitem != null)
	        {
	            entityitem.setNoDespawn();
	        }
		}

        //TODO
//        if (!this.worldObj.isRemote)
//        {
//            Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();
//
//            while (iterator.hasNext())
//            {
//                EntityPlayer entityplayer = (EntityPlayer)iterator.next();
//                entityplayer.triggerAchievement(AchievementList.killWither);
//            }
//        }
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(getInvulnerabilityTicksLeft() > 0){
			if(getInvulnerabilityTicksLeft() > MAX_INV_TICKS){
				setInvulnerabilityTicks(MAX_INV_TICKS);
			}
			this.rotationPitch = 90f;
			world.spawnParticle(
					EnumParticleTypes.FLAME, 
			          posX + rand.nextFloat() * width * 2.0F - width, 
			          posY + 0.5D + rand.nextFloat() * height, 
			          posZ + rand.nextFloat() * width * 2.0F - width, 
			          motionX, 
			          motionY, 
			          motionZ);
			float hts = 1f - ((float)getInvulnerabilityTicksLeft() / (float)MAX_INV_TICKS);
			this.bossInfo.setPercent(hts);
			this.bossInfo.setColor(Color.WHITE);
			this.setNoAI(true);
//			if(getInvTicksLeft() == 1){
//	            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 10.0F, false, false);
//			}
		}else{
			this.setNoAI(false);
			this.bossInfo.setColor(Color.BLUE);
		}
        if(getInvulnerabilityTicksLeft() > 0){
			setInvulnerabilityTicks(getInvulnerabilityTicksLeft() - 1);
		}
	}
	
	@Override
	protected void updateAITasks(){
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

    /**
     * Makes this boss Entity visible to the given player. Has no effect if this Entity is not a boss.
     */
	@Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Makes this boss Entity non-visible to the given player. Has no effect if this Entity is not a boss.
     */
	@Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    private void launchFrostBallToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
    {
        this.launchFrostBallToEntity(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F);
    }

    private void launchFrostBallToEntity(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_)
    {
    	this.world.playEvent((EntityPlayer)null, 1024, new BlockPos(this), 0);
        double d3 = this.getHeadX(p_82209_1_);
        double d4 = this.getHeadY(p_82209_1_);
        double d5 = this.getHeadZ(p_82209_1_);
        double d6 = p_82209_2_ - d3;
        double d7 = p_82209_4_ - d4;
        double d8 = p_82209_6_ - d5;
        EntityFrostBall fb = new EntityFrostBall(this.world, this, d6, d7, d8);

        if (p_82209_8_)
        {
            fb.setEntityInvulnerable(true);
        }

        fb.posY = d4;
        fb.posX = d3;
        fb.posZ = d5;
        this.world.spawnEntity(fb);
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
        this.launchFrostBallToEntity(0, p_82196_1_);
	}

    private double getHeadX(int p_82214_1_)
    {
        if (p_82214_1_ <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + (double)f1 * 1.3D;
        }
    }

    private double getHeadY(int p_82208_1_)
    {
        return p_82208_1_ <= 0 ? this.posY + 1.8D : this.posY + 1.6D;
    }

    private double getHeadZ(int p_82213_1_)
    {
        if (p_82213_1_ <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) / 180.0F * (float)Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double)f1 * 1.3D;
        }
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
    	super.writeEntityToNBT(tagCompound);
    	tagCompound.setInteger("invTicks", getInvulnerabilityTicksLeft());
    	tagCompound.setByte("divisionPower", getDivisionPower());
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
    	super.readEntityFromNBT(tagCompound);
    	if(tagCompound.hasKey("invTicks", 3)){
        	setInvulnerabilityTicks(tagCompound.getInteger("invTicks"));
    	}
    	if(tagCompound.hasKey("divisionPower", 3)){
    		setDivisionPower(tagCompound.getByte("divisionPower"));
    	}
    }
    
    @Override
    public boolean isNonBoss()
    {
        return false;
    }

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub
	}

}
