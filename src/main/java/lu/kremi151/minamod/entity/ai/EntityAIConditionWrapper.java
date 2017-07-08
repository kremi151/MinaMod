package lu.kremi151.minamod.entity.ai;

import java.util.function.Predicate;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIConditionWrapper<T extends EntityAIBase> extends EntityAIBase{
	
	private final T task;
	private final Predicate<T> condition;
	
	public EntityAIConditionWrapper(T task, Predicate<T> condition){
		this.task = task;
		this.condition = condition;
	}

	@Override
	public boolean shouldExecute() {
		return condition.test(task) && task.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting()
    {
        return condition.test(task) && task.shouldExecute();
    }

	@Override
    public boolean isInterruptible()
    {
        return task.isInterruptible();
    }

	@Override
    public void startExecuting()
    {
    	task.startExecuting();
    }

	@Override
    public void resetTask()
    {
    	task.resetTask();
    }

	@Override
    public void updateTask()
    {
    	task.updateTask();
    }

	@Override
    public void setMutexBits(int mutexBitsIn)
    {
    	task.setMutexBits(mutexBitsIn);
    }

	@Override
    public int getMutexBits()
    {
        return task.getMutexBits();
    }

}
