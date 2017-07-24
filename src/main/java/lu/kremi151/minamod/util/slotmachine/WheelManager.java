package lu.kremi151.minamod.util.slotmachine;

public class WheelManager{
	private final int wheelData[][];
	private final boolean winning[][];
	
	private boolean dirty = false;
	
	public WheelManager(int nWheels, int wheelSize) {
		if(nWheels <= 0)throw new IllegalArgumentException();
		wheelData = new int[nWheels][wheelSize];
		winning = new boolean[nWheels][wheelSize];
	}
	
	public void setWheelContent(int wheelIdx, int wheelPos, int value) {
		int old = wheelData[wheelIdx][wheelPos];
		wheelData[wheelIdx][wheelPos] = value;
		if(old != value)dirty = true;
	}
	
	public void setWheelWinning(int wheelIdx, int wheelPos, boolean winning) {
		boolean old = this.winning[wheelIdx][wheelPos];
		this.winning[wheelIdx][wheelPos] = winning;
		if(old != winning)dirty = true;
	}
	
	public void clearWinnings() {
		for(int i = 0 ; i < winning.length ; i++) {
			for(int j = 0 ; j < winning[0].length ; j++) {
				winning[i][j] = false;
			}
		}
		dirty = true;
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
	
	public boolean isWinning(int wheelIdx, int wheelPos) {
		return winning[wheelIdx][wheelPos];
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