package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerKeyChain;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiKeyChain extends GuiCustomContainer{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/keychain.png");

	private ContainerKeyChain container;
	
	public GuiKeyChain(ContainerKeyChain inventorySlotsIn) {
		super(inventorySlotsIn);
		this.container = inventorySlotsIn;

		this.ySize = 207;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color

		//fontRendererObj.drawString(I18n.translateToLocal("gui.coin_bag.name"), 8, 10, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);


	}

}
