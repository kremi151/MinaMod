package lu.kremi151.minamod.entity;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityWookie extends EntityAnimal{
	
	public static final String entityId = "wookie";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);

	public EntityWookie(World worldIn) {
		super(worldIn);
        this.setSize(10f / 16f, 1f);
        
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
    }

	@Override
	protected void initEntityAI(){
		if(this.tasks.taskEntries != null)this.tasks.taskEntries.clear();
		if(this.targetTasks.taskEntries != null)this.targetTasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMate(this, 0.5D));
        this.tasks.addTask(2, new EntityAITempt(this, 0.5D, MinaItems.BAMBUS, false));
		this.tasks.addTask(2, new EntityAIPanic(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 0.5D));//Movement Speed
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 6F));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0.5D));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
	}
	
	@Override
    public void onLivingUpdate(){
    	super.onLivingUpdate();
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }
	
	//TODO: Wookie sounds
	@Override
    protected SoundEvent getAmbientSound()
    {
        return null;
    }
	
	@Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityWookie(this.world);
	}
	
	@Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == MinaItems.BAMBUS;
    }

}
