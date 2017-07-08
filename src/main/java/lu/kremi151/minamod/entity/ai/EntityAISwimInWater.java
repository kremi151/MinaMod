package lu.kremi151.minamod.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAISwimInWater extends EntityAIBase
{
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public EntityAISwimInWater(EntityCreature creatureIn, double speedIn)
    {
        this(creatureIn, speedIn, 120);
    }

    public EntityAISwimInWater(EntityCreature creatureIn, double speedIn, int chance)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
    	if(!(this.entity.isInWater() || this.entity.isInLava()))return false;
        if (!this.mustUpdate)
        {
            if (this.entity.getIdleTime() >= 100)
            {
                return false;
            }

            if (this.entity.getRNG().nextInt(this.executionChance) != 0)
            {
                return false;
            }
        }

        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec3d.x;
            this.yPosition = vec3d.y;
            this.zPosition = vec3d.z;
            this.mustUpdate = false;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

//    /**
//     * Makes task to bypass chance
//     */
//    public void makeUpdate()
//    {
//        this.mustUpdate = true;
//    }

    /**
     * Changes task random possibility for execution
     */
    public void setExecutionChance(int newchance)
    {
        this.executionChance = newchance;
    }

}
