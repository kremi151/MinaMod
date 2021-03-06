package lu.kremi151.minamod.capabilities;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.EnergyStorage;

public class AccessableEnergyStorage extends EnergyStorage{

	public AccessableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	public AccessableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public AccessableEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public AccessableEnergyStorage(int capacity) {
		super(capacity);
	}
	
	public void setEnergy(int value) {
		this.energy = MathHelper.clamp(value, 0, capacity);
	}
	
	public void setCapacity(int value) {
		if(value < 0)throw new IllegalArgumentException("Energy capacity cannot be lower than 0");
		this.capacity = value;
		if(this.energy > value) {
			this.energy = value;
		}
	}

}
