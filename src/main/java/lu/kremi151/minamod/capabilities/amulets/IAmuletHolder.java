package lu.kremi151.minamod.capabilities.amulets;

import lu.kremi151.minamod.item.amulet.AmuletStack;

public interface IAmuletHolder {

	AmuletStack getAmuletAt(int slot);
	void setAmuletAt(int slot, AmuletStack amulet);
	int amuletAmount();
}
