package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerLetterbox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLetterbox extends GuiContainer {

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/letterbox/default.png");

	private ContainerLetterbox ct;

	public GuiLetterbox(ContainerLetterbox container) {
		super(container);
		this.ySize = 179;
		this.ct = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color
		fontRenderer.drawString(
				I18n.translateToLocal("gui.letterbox.in"), 8, 10,
				4210752);
		fontRenderer.drawString(
				I18n.translateToLocal("gui.letterbox.storage"), 98,
				10, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (!ct.isOwner()) {
			this.drawTexturedModalRect(x+97, y+28, 0, 179, 54, 36);
		}
	}

}
