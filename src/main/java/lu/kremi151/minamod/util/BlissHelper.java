package lu.kremi151.minamod.util;

import java.util.Random;

import lu.kremi151.minamod.capabilities.bliss.ICapabilityBliss;
import net.minecraft.entity.EntityLivingBase;

public class BlissHelper {

	public static ICapabilityBliss getBliss(EntityLivingBase entity){
		if(entity.hasCapability(ICapabilityBliss.CAPABILITY_BLISS, null)){
			return entity.getCapability(ICapabilityBliss.CAPABILITY_BLISS, null);
		}else{
			return new FallbackBliss(entity.getRNG());
		}
	}
	
	private static class FallbackBliss implements ICapabilityBliss{
		
		private final Random rand;
		
		private FallbackBliss(Random rand){
			this.rand = rand;
		}

		@Override
		public boolean chanceOutOf(int n, int total) {
			return rand.nextInt(total) < n;
		}

		@Override
		public void incrementBliss(int amount) {
			throw new RuntimeException("Cannot increment bliss on a fallback bliss handler");
		}

		@Override
		public void decrementBliss(int amount) {
			throw new RuntimeException("Cannot decrement bliss on a fallback bliss handler");
		}

		@Override
		public int getBliss() {
			return 1;
		}

		@Override
		public void setBliss(int bliss) throws IllegalArgumentException {
			throw new RuntimeException("Cannot set bliss on a fallback bliss handler");
		}
		
	};
}
