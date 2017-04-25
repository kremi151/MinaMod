package lu.kremi151.minamod.util;

import net.minecraft.entity.Entity;


public final class TaskFreezeEntity extends TaskRepeat{ // NO_UCD (unused code)
	
	Entity entity;
	double x, y, z;
	float yaw, pitch;
	int durationTicks;

	TaskFreezeEntity(Entity entity, int durationTicks) {
		super(0l, 0l, null);
		this.entity = entity;
		this.x = entity.posX;
		this.y = entity.posY;
		this.z = entity.posZ;
		this.yaw = entity.rotationYaw;
		this.pitch = entity.rotationPitch;
		this.durationTicks = durationTicks;
	}
	
	@Override
	public void run(Task task){
		entity.setPositionAndRotation(x, y, z, yaw, pitch);
		durationTicks--;
		if(durationTicks <= 0)this.setCanExecuteAgain(false);
	}

}
