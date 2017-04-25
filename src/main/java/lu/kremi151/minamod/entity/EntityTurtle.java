package lu.kremi151.minamod.entity;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaSounds;
import lu.kremi151.minamod.entity.ai.EntityAIHerbivore;
import lu.kremi151.minamod.entity.ai.EntityAISwimInWater;
import lu.kremi151.minamod.interfaces.IEntityAIHerbivoreListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTurtle extends EntityAnimal implements IEntityAIHerbivoreListener{
	
	private static final int SATURATION_MAX = 15;
	
    private static final DataParameter<Integer> SATURATION = EntityDataManager.<Integer>createKey(EntityTurtle.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> MINE_TURTLE = EntityDataManager.<Boolean>createKey(EntityTurtle.class, DataSerializers.BOOLEAN);
	
	public static final String entityId = "turtle";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);

	public EntityTurtle(World worldIn) {
		super(worldIn);
        
		setSize(14f/16f, 5f/16f);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(SATURATION, Integer.valueOf(SATURATION_MAX));
        this.getDataManager().register(MINE_TURTLE, Boolean.FALSE);
    }

	@Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
	
	@Override
	protected void initEntityAI(){
		if(this.tasks.taskEntries != null)this.tasks.taskEntries.clear();
		if(this.targetTasks.taskEntries != null)this.targetTasks.taskEntries.clear();
		this.tasks.addTask(0, new EntityAISwimInWater(this, 0.25D));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMate(this, 0.3D));
		this.tasks.addTask(2, new EntityAIPanic(this, 0.3D));
        this.tasks.addTask(3, new EntityAIWander(this, 0.3D));//Movement Speed
        this.tasks.addTask(3, new EntityAIHerbivore(this).setListener(this));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 6F));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0.35D));
	}
	
	private int getSaturation(){
		return this.getDataManager().get(SATURATION);
	}
	
	private void setSaturation(int v){
		this.getDataManager().set(SATURATION, v);
	}
	
	public boolean isMineTurtle(){
		return this.getDataManager().get(MINE_TURTLE);
	}
	
	private EntityTurtle setMineTurtle(boolean v){
		this.getDataManager().set(MINE_TURTLE, v);
		return this;
	}
	
	@Override
    public void onLivingUpdate(){
    	super.onLivingUpdate();
    	if(getSaturation() > 0 && getRNG().nextInt(15) == 0){
    		setSaturation(getSaturation() - 1);
    	}
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
    {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("mineTurtle", isMineTurtle());
		nbt.setInteger("saturation", getSaturation());
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
    {
		super.readEntityFromNBT(nbt);
		if(nbt.hasKey("mineTurtle")){
			setMineTurtle(nbt.getBoolean("mineTurtle"));
		}
		if(nbt.hasKey("saturation")){
			setSaturation(nbt.getInteger("saturation"));
		}
    }
	
	@Override
	protected void dropFewItems(boolean causedByPlayer, int looting)
    {
        Item item = MinaItems.TURTLE_SHELL;

        int j = 3;

        if (looting > 0)
        {
            j += this.rand.nextInt(looting + 1);
        }

        if(this.rand.nextInt(j) > 1){
        	this.dropItem(item, 1);
        }
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }
	
	@Override
	protected void collideWithEntity(Entity entity){
		super.collideWithEntity(entity);
		if(isMineTurtle()){
			this.playLivingSound();
            this.setDead();
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 0.75F, false, this.world.getGameRules().getBoolean("mobGriefing"));
		}
	}
	
	@Override
    protected SoundEvent getAmbientSound()
    {
        return MinaSounds.soundTurtleHello;
    }
	
	@Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityTurtle(this.world);
	}
	
	@Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == MinaItems.NAMIE_SEEDS;
    }

	@Override
	public void onBlockEaten(World world, BlockPos pos) {
		setSaturation(SATURATION_MAX + getRNG().nextInt(6));
	}

	@Override
	public boolean canEat() {
		return getSaturation() <= 0;
	}

}
