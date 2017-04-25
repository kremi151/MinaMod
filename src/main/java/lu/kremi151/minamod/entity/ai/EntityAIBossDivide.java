package lu.kremi151.minamod.entity.ai;

import java.util.UUID;

import com.google.common.base.Optional;

import lu.kremi151.minamod.entity.EntityIceSentinel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;

public class EntityAIBossDivide extends EntityAIBase{
	
	private EntityIceSentinel boss;
	
	public EntityAIBossDivide(EntityIceSentinel boss){
		this.boss = boss;
	}

	@Override
	public boolean shouldExecute() {
		return boss.getDivisionPower() > 0 && ((boss.getHealth() / boss.getMaxHealth()) <= 0.5);
	}
	
	@Override
    public boolean continueExecuting()
    {
        return false;
    }
	
	@Override
    public void startExecuting()
    {
		boss.setDead();
		EntityIceSentinel b1, b2;
		b1 = new EntityIceSentinel(boss.world);
		b2 = new EntityIceSentinel(boss.world);
		b1.setPosition(boss.posX, boss.posY, boss.posZ);
		b2.setPosition(boss.posX, boss.posY, boss.posZ);
		boss.world.spawnEntity(b1);
		boss.world.spawnEntity(b2);
		b1.setPositionAndRotation(boss.posX, boss.posY, boss.posZ, boss.rotationYaw, boss.rotationPitch);
		b2.setPositionAndRotation(boss.posX, boss.posY, boss.posZ, boss.rotationYaw, boss.rotationPitch);
		double nmhealth = boss.getMaxHealth() / 2.0d;
		b1.setHealth((float)nmhealth);
		b2.setHealth((float)nmhealth);
		b1.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(nmhealth);
		b2.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(nmhealth);
		b1.setDivisionPower((byte) (boss.getDivisionPower() - 1));
		b2.setDivisionPower((byte) (boss.getDivisionPower() - 1));
		b1.setInvulnerabilityTicks(0);
		b2.setInvulnerabilityTicks(0);
		for(int i = 0 ; i < 15 ; i++){
			boss.world.spawnParticle(
					EnumParticleTypes.DRAGON_BREATH, 
					boss.posX + boss.getRNG().nextFloat() * boss.width * 2.0F - boss.width, 
					boss.posY + 0.5D + boss.getRNG().nextFloat() * boss.height, 
					boss.posZ + boss.getRNG().nextFloat() * boss.width * 2.0F - boss.width, 
					boss.motionX, 
					boss.motionY, 
					boss.motionZ);
		}
    }

}
