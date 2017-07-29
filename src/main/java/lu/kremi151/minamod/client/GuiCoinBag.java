package lu.kremi151.minamod.client;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.element.GuiButtonHoldable;
import lu.kremi151.minamod.inventory.container.ContainerCoinBag;
import lu.kremi151.minamod.packet.message.MessageCoinBag;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static lu.kremi151.minamod.client.GuiUtils.isHovering;

@SideOnly(Side.CLIENT)
public class GuiCoinBag extends GuiCustomContainer implements GuiButtonHoldable.Listener{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/coin_bag.png");

	private final static int BTN_W_PLUS = 0;
	private final static int BTN_W_MINUS = 1;
	private final static int BTN_D_PLUS = 2;
	private final static int BTN_D_MINUS = 3;

	private static final ItemStack COIN_MODEL = new ItemStack(MinaItems.GOLDEN_COIN, 1);
	private static final ItemStack COIN_BAG_MODEL = new ItemStack(MinaItems.COIN_BAG, 1);

	private ContainerCoinBag container;

	private GuiButtonHoldable btnWPlus, btnWMinus, btnDPlus, btnDMinus;

	private final int relWithdrawX = 14, relWithdrawY = 43, relDepositX = 14, relDepositY = 63;

	private int withdrawAmount = 0, depositAmount = 0;
	private long currentTicks = 0, startedWPBtn = -1l, startedWMBtn = -1l, startedDPBtn = -1l, startedDMBtn = -1l;

	public GuiCoinBag(ContainerCoinBag inventorySlotsIn) {
		super(inventorySlotsIn);
		this.container = inventorySlotsIn;
		this.ySize = 177;

		btnWPlus = new GuiButtonHoldable(BTN_W_PLUS, 0, 0, "+").setListener(this);
		btnWMinus = new GuiButtonHoldable(BTN_W_MINUS, 0, 0, "-").setListener(this);
		btnDPlus = new GuiButtonHoldable(BTN_D_PLUS, 0, 0, "+").setListener(this);
		btnDMinus = new GuiButtonHoldable(BTN_D_MINUS, 0, 0, "-").setListener(this);
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(btnWPlus);
		addButton(btnWMinus);
		addButton(btnDPlus);
		addButton(btnDMinus);

		btnWPlus.x = guiLeft + 101;
		btnWPlus.y = guiTop + 41;
		btnWPlus.width = 30;
		btnWPlus.height = 18;

		btnWMinus.x = guiLeft + 133;
		btnWMinus.y = guiTop + 41;
		btnWMinus.width = 30;
		btnWMinus.height = 18;

		btnDPlus.x = guiLeft + 101;
		btnDPlus.y = guiTop + 61;
		btnDPlus.width = 30;
		btnDPlus.height = 18;

		btnDMinus.x = guiLeft + 133;
		btnDMinus.y = guiTop + 61;
		btnDMinus.width = 30;
		btnDMinus.height = 18;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		currentTicks++;
		boolean check = false;
		long dwp, dwm, ddp, ddm;
		if (startedWPBtn > 0 && (dwp = currentTicks - startedWPBtn) > 10 && dwp % 5 == 0) {
			playClickSound();
			withdrawAmount += dwp>=200?20:(dwp>=100?10:5);
			check = true;
		}
		if (startedWMBtn > 0 && (dwm = currentTicks - startedWMBtn) > 10 && dwm % 5 == 0) {
			playClickSound();
			withdrawAmount -= dwm>=200?20:(dwm>=100?10:5);
			check = true;
		}
		if (startedDPBtn > 0 && (ddp = currentTicks - startedDPBtn) > 10 && ddp % 5 == 0) {
			playClickSound();
			depositAmount += ddp>=200?20:(ddp>=100?10:5);
			check = true;
		}
		if (startedDMBtn > 0 && (ddm = currentTicks - startedDMBtn) > 10 && ddm % 5 == 0) {
			playClickSound();
			depositAmount -= ddm>=200?20:(ddm>=100?10:5);
			check = true;
		}
		if (check) {
			updateBoundaries();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < this.buttonList.size(); ++i) {
			GuiButton b = this.buttonList.get(i);
			b.enabled = true;
			b.drawButton(this.mc, mouseX, mouseY);
		}
		
        RenderHelper.enableGUIStandardItemLighting();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(matchesWithdrawButton(mouseX, mouseY)) {
			this.drawHoveringText(I18n.translateToLocal("gui.coin_bag.withdraw"), mouseX, mouseY);
		}else if(matchesDepositButton(mouseX, mouseY)) {
			this.drawHoveringText(I18n.translateToLocal("gui.coin_bag.deposit"), mouseX, mouseY);
		}
	}

	private boolean matchesWithdrawButton(int mouseX, int mouseY) {
		mouseX -= guiLeft;
		mouseY -= guiTop;
		return mouseX >= relWithdrawX && mouseX <= relWithdrawX + 16 && mouseY >= relWithdrawY
				&& mouseY <= relWithdrawY + 16;
	}

	private boolean matchesDepositButton(int mouseX, int mouseY) {
		mouseX -= guiLeft;
		mouseY -= guiTop;
		return mouseX >= relDepositX && mouseX <= relDepositX + 16 && mouseY >= relDepositY
				&& mouseY <= relDepositY + 16;
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		int rmouseX = mouseX - guiLeft;
		int rmouseY = mouseY - guiTop;
		if (matchesWithdrawButton(mouseX, mouseY)) {
			playClickSound();
			MinaMod.getMinaMod().getPacketDispatcher()
					.sendToServer(new MessageCoinBag(MessageCoinBag.Type.WITHDRAW, withdrawAmount));
		} else if (matchesDepositButton(mouseX, mouseY)) {
			playClickSound();
			MinaMod.getMinaMod().getPacketDispatcher()
					.sendToServer(new MessageCoinBag(MessageCoinBag.Type.DEPOSIT, depositAmount));
		}
	}
	
	private void playClickSound(){
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case BTN_W_PLUS:
			if (withdrawAmount < Short.MAX_VALUE) {
				withdrawAmount++;
			}
			updateBoundaries();
			break;
		case BTN_W_MINUS:
			if (withdrawAmount > 0) {
				withdrawAmount--;
			}
			updateBoundaries();
			break;
		case BTN_D_PLUS:
			if (depositAmount < Short.MAX_VALUE) {
				depositAmount++;
			}
			updateBoundaries();
			break;
		case BTN_D_MINUS:
			if (depositAmount > 0) {
				depositAmount--;
			}
			updateBoundaries();
			break;
		}
	}

	private void updateBoundaries() {
		int wmax = container.getStack().getMetadata();
		int dmax = MinaUtils.countItemsInInventory(Minecraft.getMinecraft().player.inventory, MinaItems.GOLDEN_COIN);
		if (withdrawAmount > wmax) {
			withdrawAmount = wmax;
		}
		if (depositAmount > dmax) {
			depositAmount = dmax;
		}
		if (depositAmount < 0) {
			depositAmount = 0;
		}
		if (depositAmount < 0) {
			depositAmount = 0;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color

		fontRenderer.drawString(I18n.translateToLocal("gui.coin_bag.name"), 8, 10, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

		fontRenderer.drawString(
				I18n.translateToLocalFormatted("gui.coin_bag.amount", container.getStack().getMetadata()), 37, 26,
				MinaUtils.COLOR_WHITE);

		fontRenderer.drawString("" + withdrawAmount, 37, 46, MinaUtils.COLOR_WHITE);
		fontRenderer.drawString("" + depositAmount, 37, 66, MinaUtils.COLOR_WHITE);

		GlStateManager.translate(0.0F, 0.0F, 1.0F);

		drawItemStack(COIN_MODEL, 13, 22, "");
		drawItemStack(COIN_BAG_MODEL, 13, 42, "");
		drawItemStack(COIN_BAG_MODEL, 13, 62, "");
		
		GlStateManager.translate(0.0F, 0.0F, 400.0F);
		
		this.mc.renderEngine.bindTexture(guiTextures);
		RenderHelper.disableStandardItemLighting();

		this.drawTexturedModalRect(23, 46, 0, 177, 5, 5);// Green arrow
		this.drawTexturedModalRect(23, 66, 5, 177, 5, 5);// Red arrow
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (matchesWithdrawButton(mouseX, mouseY)) {
			this.drawTexturedModalRect(x + 12, y + 41, 0, 182, 18, 18);
		} else if (matchesDepositButton(mouseX, mouseY)) {
			this.drawTexturedModalRect(x + 12, y + 61, 0, 182, 18, 18);
		}

		/*GlStateManager.translate(0.0F, 0.0F, 1.0F);

		drawItemStack(COIN_MODEL, x + 13, y + 22, "");
		drawItemStack(COIN_BAG_MODEL, x + 13, y + 42, "");
		drawItemStack(COIN_BAG_MODEL, x + 13, y + 62, "");*/

	}

	public void update(int totalQuantity) {
		container.getStack().setItemDamage(totalQuantity);
		updateBoundaries();
	}

	@Override
	public void onHold(int id) {
		switch(id){
		case BTN_W_PLUS:
			if (startedWPBtn == -1l) {
				startedWPBtn = currentTicks;
			}
			break;
		case BTN_W_MINUS:
			if (startedWMBtn == -1l) {
				startedWMBtn = currentTicks;
			}
			break;
		case BTN_D_PLUS:
			if (startedDPBtn == -1l) {
				startedDPBtn = currentTicks;
			}
			break;
		case BTN_D_MINUS:
			if (startedDMBtn == -1l) {
				startedDMBtn = currentTicks;
			}
			break;
		}
	}

	@Override
	public void onRelease(int id) {
		switch(id){
		case BTN_W_PLUS:
			startedWPBtn = -1l;
			break;
		case BTN_W_MINUS:
			startedWMBtn = -1l;
			break;
		case BTN_D_PLUS:
			startedDPBtn = -1l;
			break;
		case BTN_D_MINUS:
			startedDMBtn = -1l;
			break;
		}
	}

}
