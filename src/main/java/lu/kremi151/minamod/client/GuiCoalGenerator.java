package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerCoalGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;

public class GuiCoalGenerator extends GuiContainer {

	private static ResourceLocation guiTextures = new ResourceLocation(
			MinaMod.MODID, "textures/gui/coal_generator.png");

	private final ContainerCoalGenerator ct;

	public GuiCoalGenerator(ContainerCoalGenerator container) {
		super(container);
		this.ySize = 133;
		this.ct = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal(ct.getGenerator().getName()), 8, 10,
				4210752);
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		this.mc.renderEngine.bindTexture(guiTextures);
		int width = MathHelper.floor(34 * ct.getGenerator().getHeating());
		drawTexturedModalRect(x + 129, y + 14, 176, 0, width, 12);
		width = MathHelper.floor(34 * ct.getGenerator().getProgress());
		drawTexturedModalRect(x + 129, y + 30, 176, 12, width, 12);
	}

}
