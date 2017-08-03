package lu.kremi151.minamod.client;

import static lu.kremi151.minamod.client.GuiUtils.drawLines;
import static lu.kremi151.minamod.client.GuiUtils.isHovering;
import static lu.kremi151.minamod.client.GuiUtils.playClickSound;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.inventory.container.ContainerSlotMachineClient;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.Point;
import lu.kremi151.minamod.util.ReflectionLoader;
import lu.kremi151.minamod.util.slotmachine.Icon;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlotMachine extends GuiContainer{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/slot_machine.png");
	
	private final ContainerSlotMachineClient container;
	private final GuiButton spin1LButton, spin3LButton, spin5LButton, reportButton, nextPageButton, previousPageButton;

	private final static int infoBtnLeft = 14, infoBtnTop = 52, infoBtnRight = 28, infoBtnBottom = 66;
	private final static int instantBtnLeft = 14, instantBtnTop = 70, instantBtnRight = 28, instantBtnBottom = 84;
	private boolean displayInfoPage = false, instantMode = false, hoveringDisplayInfo = false, hoveringInstantMode = false;
	private int infoPage = 0, totalInfoPages = 0;

	public GuiSlotMachine(ContainerSlotMachineClient container) {
		super(container);
		this.container = container;
		
		this.xSize = 176;
		this.ySize = 186;

		spin1LButton = new GuiButton(0, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 1));
		spin3LButton = new GuiButton(1, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 3));
		spin5LButton = new GuiButton(2, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 5));
		reportButton = new GuiButton(99, 0, 0, 20, 20, "!");
		previousPageButton = new GuiButton(100, 0, 0, 20, 20, "<");
		nextPageButton = new GuiButton(101, 0, 0, 20, 20, ">");
	}
	
	private String getDisplayName() {
		return (container.getCustomName() != null) ? container.getCustomName() : I18n.translateToLocal("tile.slot_machine.name");
	}
	
	private void drawSlotBackground(int x, int y) {
		this.drawTexturedModalRect(x, y, 18, 200, 18, 18);
	}
	
	private void refreshButtonVisibilities() {
		for(GuiButton btn : this.buttonList)btn.visible = (btn.id < 100 && !displayInfoPage) || (btn.id >= 100 && displayInfoPage);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(
				getDisplayName() + TextFormatting.RESET + " (BETA)", 8, 10,
				4210752);
		
		fontRenderer.drawString(
				I18n.translateToLocalFormatted("gui.slot_machine.credit", container.getCredits()), 10,
				168, MinaUtils.COLOR_WHITE);

		this.mc.renderEngine.bindTexture(guiTextures);
		if(displayInfoPage) {
			this.drawTexturedModalRect(infoBtnLeft, infoBtnTop, 30, 186, 14, 14);
		}else {
			if(hoveringDisplayInfo) {
				this.drawTexturedModalRect(infoBtnLeft, infoBtnTop, 44, 186, 14, 14);
			}else {
				this.drawTexturedModalRect(infoBtnLeft, infoBtnTop, 16, 186, 14, 14);
			}
		}

		this.mc.renderEngine.bindTexture(guiTextures);
		if(!displayInfoPage) {
			if(instantMode) {
				this.drawTexturedModalRect(instantBtnLeft, instantBtnTop, 72, 186, 14, 14);
			}else {
				if(hoveringInstantMode) {
					this.drawTexturedModalRect(instantBtnLeft, instantBtnTop, 86, 186, 14, 14);
				}else {
					this.drawTexturedModalRect(instantBtnLeft, instantBtnTop, 58, 186, 14, 14);
				}
			}

			this.mc.renderEngine.bindTexture(guiTextures);
			for(int i = 0 ; i < container.getWheelCount() ; i++) {
				for(int j = 0 ; j < container.getDisplayWheelSize() ; j++) {
					if(container.isWinning(i, j)) {
						this.drawTexturedModalRect(40 + (i * 20), 34 + (j * 17), 0, 186, 16, 16);
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			for(int i = 0 ; i < 5 ; i++) {
				int x = 40 + (i * 20);
				for(int j = 0 ; j < 3 ; j++) {
					int y = 34 + (j * 17);
					Item icon = container.getIcon(i, j);
					try {
						ReflectionLoader.GuiContainer_drawItemStack(this, new ItemStack(icon), x, y, "");
				        GlStateManager.translate(0.0F, 0.0F, -32.0F);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			RenderHelper.disableStandardItemLighting();
			
			if(isHovering(mouseX, mouseY, spin1LButton)) {
				drawLines(0.2f, 0.8f, 0.8f, new Point(48, 59), new Point(127, 59));
			}
			
			if(isHovering(mouseX, mouseY, spin3LButton)) {
				drawLines(0.8f, 0.8f, 0.2f, new Point(48, 42), new Point(127, 42));
				drawLines(0.2f, 0.8f, 0.8f, new Point(48, 59), new Point(127, 59));
				drawLines(0.8f, 0.2f, 0.8f, new Point(48, 76), new Point(127, 76));
			}
			
			if(isHovering(mouseX, mouseY, spin5LButton)) {
				drawLines(0.8f, 0.8f, 0.2f, new Point(48, 42), new Point(127, 42));
				drawLines(0.2f, 0.8f, 0.8f, new Point(48, 59), new Point(127, 59));
				drawLines(0.8f, 0.2f, 0.8f, new Point(48, 76), new Point(127, 76));

				drawLines(0.5f, 0.8f, 0.5f, new Point(48, 42), new Point(88, 75), new Point(127, 42));
				drawLines(0.8f, 0.3f, 0.4f, new Point(48, 75), new Point(88, 42), new Point(127, 75));
			}

			drawCoinAmount(spin1LButton.x + spin1LButton.width + 6 - guiLeft, spin1LButton.y + ((spin1LButton.height - 16) / 2) - guiTop, container.getPriceFor1Spin());
			drawCoinAmount(spin3LButton.x + spin3LButton.width + 6 - guiLeft, spin3LButton.y + ((spin3LButton.height - 16) / 2) - guiTop, container.getPriceFor3Spins());
			drawCoinAmount(spin5LButton.x + spin5LButton.width + 6 - guiLeft, spin5LButton.y + ((spin5LButton.height - 16) / 2) - guiTop, container.getPriceFor5Spins());
		}else {
			final int minIcon = infoPage * 6;
			final int maxIcon = Math.min(minIcon + 6, container.getIconCount());
			for(int i = minIcon ; i < maxIcon ; i++) {
				int y = 23 + ((i - minIcon) * 18);
				Icon icon = container.getIcon(i);
				this.mc.renderEngine.bindTexture(guiTextures);
				drawSlotBackground(40, y);
				
				this.drawTexturedModalRect(60, y + 7, 18, 228, 18, 4);
				String valueCaption = "" + container.getIconRowValue(i);
				if(icon.cherry) {
					this.drawTexturedModalRect(95 + this.fontRenderer.getStringWidth(valueCaption), y + 4, 18, 218, 7, 10);
				}
				
				this.fontRenderer.drawStringWithShadow(valueCaption, 80, y + 5, MinaUtils.COLOR_WHITE);
				this.fontRenderer.drawStringWithShadow(I18n.translateToLocalFormatted("gui.generic.page", infoPage + 1, totalInfoPages), 95, 146, MinaUtils.COLOR_WHITE);
				
				RenderHelper.enableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				try {
					ReflectionLoader.GuiContainer_drawItemStack(this, new ItemStack(icon.icon), 41, y + 1, "");
			        GlStateManager.translate(0.0F, 0.0F, -32.0F);
			        
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				RenderHelper.disableStandardItemLighting();
			}
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		refreshButtonVisibilities();
		if(displayInfoPage) {
			totalInfoPages = (container.getIconCount() / 6) + (((container.getIconCount() % 6) > 0) ? 1 : 0);
			if(infoPage >= totalInfoPages) {
				infoPage = totalInfoPages - 1;
			}else if(infoPage < 0) {
				infoPage = 0;
			}
		}
		hoveringDisplayInfo = isHovering(mouseX, mouseY, guiLeft + infoBtnLeft, guiTop + infoBtnTop, guiLeft + infoBtnRight, guiTop + infoBtnBottom);
		hoveringInstantMode = isHovering(mouseX, mouseY, guiLeft + instantBtnLeft, guiTop + instantBtnTop, guiLeft + instantBtnRight, guiTop + instantBtnBottom);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(hoveringDisplayInfo) {
			this.drawHoveringText(I18n.translateToLocal("gui.slot_machine.info_page"), mouseX, mouseY);
		}
		if(hoveringInstantMode) {
			if(instantMode) {
				this.drawHoveringText(I18n.translateToLocal("gui.slot_machine.disable_instant_mode"), mouseX, mouseY);
			}else {
				this.drawHoveringText(I18n.translateToLocal("gui.slot_machine.enable_instant_mode"), mouseX, mouseY);
			}
		}

		if(!displayInfoPage && isHovering(mouseX, mouseY, reportButton)) {
			drawHoveringText(TextFormatting.RED + "Report in case the slot machine does not reward money when at least one row is a winning line", mouseX, mouseY);
		}
		
		if(isHovering(mouseX, mouseY, guiLeft + 8, guiTop + 166, guiLeft + 170, guiTop + 179)) {
			drawHoveringText(I18n.translateToLocalFormatted("gui.slot_machine.current_win", container.getSessionWin()>0?(TextFormatting.GREEN + "+" + container.getSessionWin()):(container.getSessionWin()==0?("" + container.getSessionWin()):(TextFormatting.RED + "" + container.getSessionWin()))), mouseX, mouseY);
		}
    }
	
	private void drawCoinAmount(int x, int y, int amount) {
		try {
			ReflectionLoader.GuiContainer_drawItemStack(this, new ItemStack(MinaItems.GOLDEN_COIN, amount), x, y, ""+amount);
	        GlStateManager.translate(0.0F, 0.0F, -32.0F);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		if(!displayInfoPage) {
			for(int i = 0 ; i < 5 ; i++) {
				this.drawTexturedModalRect(x + 39 + (i * 20), y + 33, 0, 202, 18, 52);
			}

			drawSlotBackground(x + 143, y + 91);
			drawSlotBackground(x + 143, y + 116);
			drawSlotBackground(x + 143, y + 141);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton == 0) {
			if(hoveringDisplayInfo) {
				displayInfoPage = !displayInfoPage;
				playClickSound();
			}else if(hoveringInstantMode) {
				instantMode = !instantMode;
				playClickSound();
			}
		}
    }
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id < 100 && !displayInfoPage) {
			switch(guibutton.id){
			case 0:
				container.spin(SpinMode.ONE, instantMode);
				break;
			case 1:
				container.spin(SpinMode.THREE, instantMode);
				break;
			case 2:
				container.spin(SpinMode.FIVE, instantMode);
				break;
			case 99:
				container.report();
				Minecraft.getMinecraft().player.closeScreen();
				Minecraft.getMinecraft().player.sendChatMessage("A report has been send. Please wait until the administrator contacts you.");
				break;
			}
		}else if(displayInfoPage) {
			switch(guibutton.id){
			case 100:
				if(infoPage > 0) {
					infoPage--;
				}
				break;
			case 101:
				if(infoPage < totalInfoPages - 1) {
					infoPage++;
				}
				break;
			}
		}
	}
	
	@Override
	public void updateScreen()
    {
		super.updateScreen();
		this.spin1LButton.enabled = !container.isTurning();
		this.spin3LButton.enabled = !container.isTurning();
		this.spin5LButton.enabled = !container.isTurning();
		this.reportButton.enabled = !container.isTurning();
		this.nextPageButton.enabled = infoPage < totalInfoPages - 1;
		this.previousPageButton.enabled = infoPage > 0;
    }
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(spin1LButton);
		this.buttonList.add(spin3LButton);
		this.buttonList.add(spin5LButton);
		this.buttonList.add(reportButton);
		this.buttonList.add(previousPageButton);
		this.buttonList.add(nextPageButton);
		
		setupPositions();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
	
	private void setupPositions(){
		spin1LButton.x = guiLeft + ((xSize - spin1LButton.width) / 2);
		spin1LButton.y = guiTop + 90;
		
		spin3LButton.x = guiLeft + ((xSize - spin3LButton.width) / 2);
		spin3LButton.y = spin1LButton.y + spin1LButton.height + 5;
		
		spin5LButton.x = guiLeft + ((xSize - spin5LButton.width) / 2);
		spin5LButton.y = spin3LButton.y + spin3LButton.height + 5;
		
		reportButton.x = guiLeft + 142;
		reportButton.y = guiTop + 50;
		
		previousPageButton.x = guiLeft + 35;
		previousPageButton.y = guiTop + 140;

		nextPageButton.x = previousPageButton.x + 25;
		nextPageButton.y = previousPageButton.y;
	}
}
