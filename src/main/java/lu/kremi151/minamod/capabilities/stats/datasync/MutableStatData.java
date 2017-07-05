package lu.kremi151.minamod.capabilities.stats.datasync;

import net.minecraft.util.math.MathHelper;

public class MutableStatData {

	private int actual, training;
	
	public MutableStatData(int actual, int training){
		this.actual = MathHelper.clamp(actual, 0, 255);
		this.training = MathHelper.clamp(training, -255, 255);
	}
	
	public MutableStatData(){
		this(0, 0);
	}
	
	public int getActual(){
		return actual;
	}
	
	public int getTraining(){
		return training;
	}
	
	public MutableStatData setActual(int val){
		this.actual = MathHelper.clamp(val, 0, 255);
		return this;
	}
	
	public MutableStatData setTraining(int val){
		this.training = MathHelper.clamp(val, -255, 255);
		return this;
	}
	
	public StatData toImmutable() {
		return new StatData(actual, training);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof StatData) {
			return ((MutableStatData)obj).actual == this.actual && ((MutableStatData)obj).training == this.training;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return 31 * actual + training;
	}
}
