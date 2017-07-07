package lu.kremi151.minamod.capabilities.stats.datasync;

import net.minecraft.util.math.MathHelper;

public class StatData {

	private final int actual, training;
	
	public StatData(int actual, int training){
		this.actual = MathHelper.clamp(actual, 0, 255);
		this.training = MathHelper.clamp(training, -255, 255);
	}
	
	public StatData(){
		this(0, 0);
	}
	
	public int getActual(){
		return actual;
	}
	
	public int getTraining(){
		return training;
	}
	
	public StatData withActual(int actual) {
		return new StatData(actual, this.training);
	}
	
	public StatData withTraining(int training) {
		return new StatData(this.actual, training);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof StatData) {
			return ((StatData)obj).actual == this.actual && ((StatData)obj).training == this.training;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return 31 * actual + training;
	}
}
