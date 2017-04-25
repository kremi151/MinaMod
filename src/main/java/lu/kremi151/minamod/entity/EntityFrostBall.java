package lu.kremi151.minamod.entity;

import java.util.Iterator;

import lu.kremi151.minamod.MinaEnchantments;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPotions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityFrostBall extends EntityFireball{
	
	public static final String entityId = "frost_ball";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);
	
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.<Boolean>createKey(EntityFrostBall.class, DataSerializers.BOOLEAN);

    public EntityFrostBall(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntityFrostBall(World worldIn, EntityLivingBase launcher, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_)
    {
        super(worldIn, launcher, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        this.setSize(0.3125F, 0.3125F);
    }

    @Override
    protected float getMotionFactor()
    {
        return this.isEntityInvulnerable(null) ? 0.73F : super.getMotionFactor();
    }

//    @SideOnly(Side.CLIENT)
//    public EntityFrostBall(World worldIn, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_)
//    {
//        super(worldIn, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_);
//        this.setSize(0.3125F, 0.3125F);
//    }

    @Override
    public boolean isBurning()
    {
        return false;
    }

    @Override
    public float getExplosionResistance(Explosion p_180428_1_, World worldIn, BlockPos p_180428_3_, IBlockState p_180428_4_)
    {
        float f = super.getExplosionResistance(p_180428_1_, worldIn, p_180428_3_, p_180428_4_);

        if (this.isEntityInvulnerable(null) && p_180428_4_.getBlock() != Blocks.BEDROCK && p_180428_4_.getBlock() != Blocks.END_PORTAL && p_180428_4_.getBlock() != Blocks.END_PORTAL_FRAME && p_180428_4_.getBlock() != Blocks.COMMAND_BLOCK)
        {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    @Override
    protected void onImpact(RayTraceResult res)
    {
        if (!this.world.isRemote)
        {
            if (res.entityHit != null)
            {
                if (this.shootingEntity != null)
                {
                    if (res.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
                    {
                        if (!res.entityHit.isEntityAlive())
                        {
                            this.shootingEntity.heal(15.0F);
                        }
                        else
                        {
                            this.applyEnchantments(this.shootingEntity, res.entityHit);
                        }
                    }
                }
                else
                {
                	res.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0F);
                }

                if (res.entityHit instanceof EntityLivingBase)
                {
                	EntityLivingBase lb = (EntityLivingBase)res.entityHit;
                	Iterator<ItemStack> it = lb.getArmorInventoryList().iterator();
                	int fp = 1;
                	while(it.hasNext()){
                		ItemStack is = it.next();
                		if(!is.isEmpty())fp += EnchantmentHelper.getEnchantmentLevel(MinaEnchantments.FREEZE_PROTECTION, is);
                	}
                	if(this.rand.nextInt(fp) == 0)lb.addPotionEffect(new PotionEffect(MinaPotions.FREEZE, 300));
                }
            }

            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false, false);
//            newSubstitution(worldObj, this, this.posX, this.posY, this.posZ, 2.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.getDataManager().register(INVULNERABLE, Boolean.FALSE);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
        return this.getDataManager().get(INVULNERABLE);
    }

    @Override
    public void setEntityInvulnerable(boolean isInvulnerable)
    {
        this.getDataManager().set(INVULNERABLE, isInvulnerable);
    }

}
