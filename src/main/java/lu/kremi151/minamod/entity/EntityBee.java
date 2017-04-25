package lu.kremi151.minamod.entity;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBee extends EntityCreature{
	
	public static final String entityId = "bee";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);

    private BlockPos spawnPosition;
    
    private static final DataParameter<Boolean> QUEEN = EntityDataManager.<Boolean>createKey(EntityBee.class, DataSerializers.BOOLEAN);
	
	public EntityBee(World worldIn) {
		super(worldIn);
        this.setSize(0.0625f,0.0625f);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(QUEEN, Boolean.FALSE);
    }
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3.1D);
        
        if(this.rand.nextInt(20) == 0){
        	this.getDataManager().set(QUEEN, Boolean.TRUE);
        }
    }

	@Override
    protected boolean canDespawn()
    {
        return true;
    }

	@Override
    public boolean canBePushed()
    {
        return false;
    }
	
	@Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

	@Override
    protected boolean canTriggerWalking()
    {
        return false;
    }
	
	public boolean isQueen(){
		return this.getDataManager().get(QUEEN);
	}

	@Override
    public void fall(float distance, float damageMultiplier) {}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos){}

	@Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }
	
	@Override
	public boolean isEntityInvulnerable(DamageSource ds){
		return ds == DamageSource.IN_WALL;
	}

	@Override
	public void onUpdate()
    {
        super.onUpdate();

        this.motionY *= 0.6000000238418579D;
    }

	@Override
    protected void updateAITasks()
    {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.up();

        if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
        {
            this.spawnPosition = null;
        }

        if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double)((int)this.posX), (double)((int)this.posY), (double)((int)this.posZ)) < 4.0D)
        {
            this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
        }

        double d0 = (double)this.spawnPosition.getX() + 0.5D - this.posX;
        double d1 = (double)this.spawnPosition.getY() + 0.1D - this.posY;
        double d2 = (double)this.spawnPosition.getZ() + 0.5D - this.posZ;
        this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
        this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
        this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
        float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
        float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
        this.moveForward = 0.5F;
        this.rotationYaw += f1;
    }
}
