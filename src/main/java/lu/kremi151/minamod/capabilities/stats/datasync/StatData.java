package lu.kremi151.minamod.capabilities.stats.datasync;

import net.minecraft.util.math.MathHelper;

public class StatData {

	private int actual, training;
	
	public StatData(int actual, int training){
		this.actual = actual;
		this.training = training;
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
	
	public void setActual(int val){
		this.actual = MathHelper.clamp(val, 0, 255);
	}
	
	public void setTraining(int val){
		this.training = MathHelper.clamp(val, -255, 255);
	}
}
