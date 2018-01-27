package lu.kremi151.minamod.util;

import java.util.Set;

import lu.kremi151.minamod.client.GuiMinaModConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class MinaGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public boolean hasConfigGui() {
		return false;//TODO: Enable gui button (rework of the actual gui...)
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiMinaModConfiguration(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}
