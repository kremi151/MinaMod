package lu.kremi151.minamod.capabilities.bliss;

import net.minecraft.entity.EntityLivingBase;

public class EntityBlissImpl implements ICapabilityBliss{
	
	private final EntityLivingBase entity;
	private int bliss = 1;
	
	public EntityBlissImpl(EntityLivingBase entity){
		this(entity, 1);
	}
	
	public EntityBlissImpl(EntityLivingBase entity, int bliss){
		this.entity = entity;
		this.bliss = bliss;
	}

	@Override
	public boolean chanceOutOf(int n, int total) {
		if(bliss > 1)total /= bliss;
		if(total > 0){
			return entity.getRNG().nextInt(total) < n;
		}else{
			return true;
		}
	}

	@Override
	public void incrementBliss(int amount) {
		bliss++;
	}

	@Override
	public void decrementBliss(int amount) {
		if(bliss > 0)bliss--;
	}

	@Override
	public int getBliss() {
		return bliss;
	}

	@Override
	public void setBliss(int bliss) throws IllegalArgumentException {
		if(bliss <= 0){
			throw new IllegalArgumentException("The bliss value must be positive");
		}
		this.bliss = bliss;
	}

}
