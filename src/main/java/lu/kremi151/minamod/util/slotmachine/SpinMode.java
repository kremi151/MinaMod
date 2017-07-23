package lu.kremi151.minamod.util.slotmachine;

public enum SpinMode{
	ONE(1),
	THREE(3),
	FIVE(5);
	
	private final int defaultPrice;
	
	private SpinMode(int defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	
	public int getDefaultPrice() {
		return defaultPrice;
	}
}