package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerSketchView;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSketchView extends GuiContainer {

	private static ResourceLocation guiTextures = new ResourceLocation(
			MinaMod.MODID, "textures/gui/sketch.png");

	private final ContainerSketchView ct;

	public GuiSketchView(ContainerSketchView container) {
		super(container);
		this.ct = container;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal("item.sketch.name"), 8, 10,
				4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX,
			int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
