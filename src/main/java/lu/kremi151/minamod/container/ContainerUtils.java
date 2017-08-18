package lu.kremi151.minamod.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public class ContainerUtils {

	static void transmitInteger(Container container, IContainerListener icrafting, int startKey, int val) {
		icrafting.sendWindowProperty(container, startKey, (val >> 16) & 0xFFFF);
		icrafting.sendWindowProperty(container, startKey+1, val & 0xFFFF);
	}
}
