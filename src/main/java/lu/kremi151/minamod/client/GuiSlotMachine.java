package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.inventory.container.ContainerSlotMachineClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlotMachine extends GuiContainer{
	
	private final ContainerSlotMachineClient container;
	private final GuiButton spinButton;

	public GuiSlotMachine(ContainerSlotMachineClient container) {
		super(container);
		this.container = container;
		
		this.xSize = 270;
		this.ySize = 180;

		spinButton = new GuiButton(0, 0, 0, 100, 20, "Spin");
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
		/*this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (!ct.isOwner()) {
			this.drawTexturedModalRect(x+97, y+28, 0, 179, 54, 36);
		}*/
		
		for(int i = 0 ; i < 5 ; i++) {
			int x = ((width - 96) / 2) + (i * 20);
			for(int j = 0 ; j < 3 ; j++) {
				int y = ((height - 56) / 2) + (j * 20);
				Item icon = container.getIcon(i, j);
				this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), x, y);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id){
		case 0:
			container.spin();
			break;
		}
	}
	
	@Override
	public void updateScreen()
    {
		super.updateScreen();
		this.spinButton.enabled = !container.isTurning();
    }
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(spinButton);
		
		setupPositions();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
	
	private void setupPositions(){
		spinButton.x = guiLeft + ((xSize - spinButton.width) / 2);
		spinButton.y = guiTop + ySize - spinButton.height;
	}
}
