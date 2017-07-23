package lu.kremi151.minamod.util.slotmachine;

public class WheelManager{
	private final int wheelData[][];
	
	private boolean dirty = false;
	
	public WheelManager(int nWheels, int wheelSize) {
		if(nWheels <= 0)throw new IllegalArgumentException();
		wheelData = new int[nWheels][wheelSize];
	}
	
	public void setWheelContent(int wheelIdx, int wheelPos, int value) {
		int old = wheelData[wheelIdx][wheelPos];
		wheelData[wheelIdx][wheelPos] = value;
		if(old != value)dirty = true;
	}
	
	public void unmarkDirty() {
		dirty = false;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public int getWheelCount() {
		return this.wheelData.length;
	}
	
	public int getDisplayWheelSize() {
		return wheelData[0].length;
	}
	
	public int getWheelValue(int wheelIdx, int wheelPos) {
		return wheelData[wheelIdx][wheelPos];
	}
	
	public int[][] createRawWheelSnapshot(){
		int copy[][] = new int[wheelData.length][wheelData[0].length];
		for(int x = 0 ; x < wheelData.length ; x++) {
			for(int y = 0 ; y < wheelData[0].length ; y++) {
				copy[x][y] = wheelData[x][y];
			}
		}
		return copy;
	}
	
}