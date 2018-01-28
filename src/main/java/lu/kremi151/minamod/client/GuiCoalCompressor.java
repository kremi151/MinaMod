package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerCoalCompressor;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCoalCompressor extends GuiCustomContainer.Inventory{

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/coal_compressor/default.png");

	private final ContainerCoalCompressor container;
	
	public GuiCoalCompressor(ContainerCoalCompressor container) {
		super(container);
		this.container = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal("tile.coal_compressor.name"), 8, 6,
				4210752);
		fontRenderer.drawString(
				I18n.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
		
		String progressCaption = I18n.translateToLocalFormatted("gui.progress.caption", Math.round(container.getCompressor().getProgress() * 10000.0f) / 100.0f);
		int progressCaptionWidth = fontRenderer.getStringWidth(progressCaption);
		fontRenderer.drawStringWithShadow(
				progressCaption, (this.xSize - progressCaptionWidth) / 2, 60,
				MinaUtils.COLOR_WHITE);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		int width = MathHelper.floor(48 * container.getCompressor().getProgress());
		this.drawTexturedModalRect(x + 64, y + 38, 176, 0, width, 9);
	}

}
