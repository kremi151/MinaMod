package lu.kremi151.minamod.entity;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaSounds;
import lu.kremi151.minamod.entity.ai.EntityAISwimInWater;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPenguin extends EntityAnimal{
	
	public static final String entityId = "penguin";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);
	
	//Rendering fields, only for client
	private RenderData renderData = null;
	//
	
	public EntityPenguin(World worldIn) {
		super(worldIn);
        this.setSize(0.5f, 17f / 16f);

        this.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
	
	@Override
	protected void initEntityAI(){
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
		this.tasks.addTask(0, new EntityAISwimInWater(this, 1.6D));
		this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMate(this, 1.1D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.FISH, false));
		this.tasks.addTask(3, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));//Movement Speed
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityLiving.class, 6F));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.2D));
	}
	
	@Override
    public void onLivingUpdate(){
    	super.onLivingUpdate();
    	MinaMod.getProxy().checkInitialPenguinState(this);
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
    {
		super.writeEntityToNBT(nbt);
		//TODO
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
    {
		super.readEntityFromNBT(nbt);
		//TODO
    }
	
	@Override
	protected void dropFewItems(boolean causedByPlayer, int looting)
    {
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        setAir(3000);
    }
	
	@Override
    protected SoundEvent getAmbientSound()
    {
        return MinaSounds.soundPenguinSay;
    }
	
	@Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityPenguin(this.world);
	}
	
	@Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.FISH;
    }
	
	@SideOnly(Side.CLIENT)
	public RenderData getRenderData(){
		if(renderData == null){
			renderData = new RenderData(this);
		}
		return renderData;
	}
	
	public static class RenderData{
		private RenderData(EntityPenguin p){
		}
		public static final int max_cooldown = 40;
		public int cooldown = 0;
		public boolean was_in_water = false;
		public boolean checked_water_state = false;
	}

}
