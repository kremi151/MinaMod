package lu.kremi151.minamod.client;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.container.ContainerSelectItem;
import lu.kremi151.minamod.network.MessageItemSelected;
import lu.kremi151.minamod.util.ReflectionLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSelectItem extends GuiContainer {

	private static ResourceLocation guiTextures = new ResourceLocation(
			MinaMod.MODID, "textures/gui/select_item.png");

	private final ContainerSelectItem ct;
	private final GuiButton btnOk, btnCancel;
	private int selectedSlot = -1;

	public GuiSelectItem(ContainerSelectItem container) {
		super(container);
		this.ySize = 124;
		this.ct = container;

		this.btnOk = new GuiButton(0, 0, 0, 60, 20, I18n.translateToLocal("gui.select_item.ok"));
		this.btnCancel = new GuiButton(1, 0, 0, 60, 20, I18n.translateToLocal("gui.select_item.cancel"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				I18n.translateToLocal(ct.getTitle()), 8, 10,
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
		if(ct.getBlockedSlot() > 0) {
			Slot selected = ct.getSlot(ct.getBlockedSlot());
			this.drawTexturedModalRect(x + selected.xPos - 1, y + selected.yPos - 1, 18, 124, 18, 18);
		}
		if(selectedSlot > 0) {
			Slot selected = ct.getSlot(selectedSlot);
			this.drawTexturedModalRect(x + selected.xPos - 1, y + selected.yPos - 1, 0, 124, 18, 18);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		Slot slot = ReflectionLoader.GuiContainer_getSlotAtPosition(this, mouseX, mouseY);
		if(slot == null) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}else {
			if(slot.slotNumber != ct.getBlockedSlot() && slot.getHasStack()) {
				this.selectedSlot = slot.slotNumber;
			}
		}
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		super.drawScreen(mouseX, mouseY, partialTicks);
    }
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 1) {
			ct.getPlayer().closeScreen();
		}else if(selectedSlot > 0){
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageItemSelected(ct.getSlot(selectedSlot).getSlotIndex()));
			ct.getPlayer().closeScreen();
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(btnOk);
		this.buttonList.add(btnCancel);
		
		setupPositions();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
	
	@Override
	public void updateScreen()
    {
		super.updateScreen();
		this.btnOk.enabled = selectedSlot > 0;
    }
	
	private void setupPositions(){
		btnOk.x = guiLeft + 26;
		btnOk.y = guiTop + 99;
		
		btnCancel.x = btnOk.x + 66;
		btnCancel.y = btnOk.y;
	}

}
