package lu.kremi151.minamod.entity;

import java.util.UUID;

import com.google.common.base.Predicate;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.item.ItemSoulPearl;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySoulPearl extends EntityThrowable{
	
	public static final String entityId = "soul_pearl";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);
	
	public static final String DENY_TAG = "deny_soulpearl";
	private static final boolean allow_exceptions = false;
	
	NBTTagCompound entity_nbt;
	String custom_name = null;
	int damage = 0;
	boolean can_drop = true;
	private Predicate<Entity> predicate = DEFAULT_PREDICATE;

    public EntitySoulPearl(World worldIn)
    {
        super(worldIn);
    }

    public EntitySoulPearl(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }
    
    public EntitySoulPearl setCatchedEntityNBT(NBTTagCompound nbt){
    	this.entity_nbt = nbt;
    	return this;
    }
    
    public EntitySoulPearl setDamage(int damage){
    	this.damage = damage;
    	return this;
    }
    
    public EntitySoulPearl setCustomName(String custom_name){
    	this.custom_name = custom_name;
    	return this;
    }
    
    public EntitySoulPearl setCanDrop(boolean v){
    	this.can_drop = v;
    	return this;
    }
    
    @SuppressWarnings("unused")
	private void processImpact(RayTraceResult mop){
    	if (entity_nbt != null){
			Entity entity = EntityList.createEntityFromNBT(entity_nbt, world);
            if(entity == null || isBuggyEntity(entity_nbt)){
        		dropEmptySoulPearl(this.posX, this.posY, this.posZ, damage);
            	//this.setDead();
            }else{
            	entity.setUniqueId(UUID.randomUUID());//if(!can_drop)
                if(custom_name != null)entity.setCustomNameTag(custom_name);
                entity.setPosition(this.posX, this.posY, this.posZ);
            	this.world.spawnEntity(entity);
            	this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, true);
            	this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(MinaItems.SOUL_PEARL)});
        		dropEmptySoulPearl(this.posX, this.posY, this.posZ, damage + 1);
            }
        }else if (mop.entityHit != null && entity_nbt == null && predicate.apply(mop.entityHit)){
			if(allow_exceptions && MinaUtils.checkHasTag(mop.entityHit, DENY_TAG)){
                dropEmptySoulPearl(this.posX, this.posY, this.posZ, damage);
                if(this.getThrower() != null){
                	TextHelper.sendTranslateableErrorMessage(getThrower(), "msg.soulpearl.catch_protection");
                }
                //this.setDead();
                return;
        	}else{
            	ItemStack is = new ItemStack(MinaItems.SOUL_PEARL, 1);
            	is.setItemDamage(damage);
            	if(mop.entityHit.hasCustomName()){
            		is.setStackDisplayName(mop.entityHit.getCustomNameTag());
            	}
            	if(ItemSoulPearl.catchEntity(is, (EntityLivingBase)mop.entityHit)){
            		setTargetDead(mop.entityHit);
    				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, true);
                	this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(MinaItems.SOUL_PEARL)});
                	EntityItem ei = new EntityItem(this.world, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, is);
                	this.world.spawnEntity(ei);
                	//this.setDead();
            	}else{
            		dropEmptySoulPearl(this.posX, this.posY, this.posZ, damage);
            	}
            	
        	}
		}else{
    		dropEmptySoulPearl(this.posX, this.posY, this.posZ, damage);
		}
        //this.setDead();
    }

    @Override
    protected void onImpact(RayTraceResult mop)
    {
    	if(!this.world.isRemote){
    		processImpact(mop);
        	this.setDead();
    	}
    }
    
    private void dropEmptySoulPearl(double x, double y, double z, int damage){
    	if(can_drop && damage < MinaItems.SOUL_PEARL.getMaxDamage()){
    		ItemStack is = new ItemStack(MinaItems.SOUL_PEARL, 1);
        	is.setItemDamage(damage);
    		MinaUtils.dropItem(is, world, x, y, z);
    	}
    }
    
    private boolean isBuggyEntity(NBTTagCompound nbt){
    	return nbt.getString("id").equalsIgnoreCase("minecraft:");
    }
    
    public void setTargetDead(Entity e){
    	if(!e.world.isRemote){
    		e.setDead();
    	}
    }
    
    private static final Predicate<Entity> DEFAULT_PREDICATE = new Predicate<Entity>(){

		@Override
		public boolean apply(Entity input) {
			return input instanceof EntityLivingBase && !(input instanceof EntityPlayer);
		}
    	
    };

}
