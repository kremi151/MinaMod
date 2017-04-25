package lu.kremi151.minamod.util;

import net.minecraft.util.math.MathHelper;

public class Point {
	private int x, y;
	
	public Point(){}
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y){
		this.x = MathHelper.floor(x);
		this.y = MathHelper.floor(y);
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
}
