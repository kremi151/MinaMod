package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerAmuletInventory;
import lu.kremi151.minamod.proxy.ClientProxy;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAmuletInventory extends GuiContainer {

	private static ResourceLocation guiTextures = new ResourceLocation(
			MinaMod.MODID, "textures/gui/amulets.png");

	private ContainerAmuletInventory ct;

	public GuiAmuletInventory(ContainerAmuletInventory container) {
		super(container);
		this.ct = container;
		this.ySize = 176;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		fontRenderer.drawString(
				I18n.translateToLocal("container.amulets.inventory"), 8, 10,
				4210752);
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
		fontRenderer.drawString(ClientProxy.KEY_AMULET_1.getDisplayName(), 72, 26, 4210752);
		fontRenderer.drawString(ClientProxy.KEY_AMULET_2.getDisplayName(), 90, 44, 4210752);
		fontRenderer.drawString(ClientProxy.KEY_AMULET_3.getDisplayName(), 108, 62, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

	}

}
