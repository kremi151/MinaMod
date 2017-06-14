package lu.kremi151.minamod.capabilities.stats.types;

import java.util.UUID;

import lu.kremi151.minamod.attribute.AttributeModifierStatSpeed;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.Stat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;

public class StatTypeSpeed<T extends EntityLivingBase> extends StatType<T> {

	public StatTypeSpeed(String id) {
		super(id);
	}

	@Override
	public Stat buildStat(ICapabilityStats<T> stats, T entity) {
		return this.createDefaultStat(entity, stats::pointsLeft, 255, (a, t) -> new StatSpeed(entity, a, t));
	}
	
	private static class StatSpeed extends Stat{
		
		public static final UUID SPEED_MODIFIER_ID = UUID.fromString("3ef9b0ec-f250-4939-883e-3bed5f0b3732");
		private final AttributeModifierStatSpeed speedMod;
		
		private final EntityLivingBase entity;

		public StatSpeed(EntityLivingBase entity, Value actual, Value training) {
			super(actual, training);
			this.entity = entity;
			this.speedMod = new AttributeModifierStatSpeed(SPEED_MODIFIER_ID, actual::get);
		}
		
		@Override
		public void initialize(){
			this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_ID);
			this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedMod);
		}
		
	}

}
