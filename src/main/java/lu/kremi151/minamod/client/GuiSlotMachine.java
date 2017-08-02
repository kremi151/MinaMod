package lu.kremi151.minamod.client;

import static lu.kremi151.minamod.client.GuiUtils.drawLines;
import static lu.kremi151.minamod.client.GuiUtils.isHovering;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.inventory.container.ContainerSlotMachineClient;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.Point;
import lu.kremi151.minamod.util.ReflectionLoader;
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
	private final GuiButton spin1LButton, spin3LButton, spin5LButton, reportButton;

	public GuiSlotMachine(ContainerSlotMachineClient container) {
		super(container);
		this.container = container;
		
		this.xSize = 176;
		this.ySize = 186;

		spin1LButton = new GuiButton(0, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 1));
		spin3LButton = new GuiButton(1, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 3));
		spin5LButton = new GuiButton(2, 0, 0, 100, 20, I18n.translateToLocalFormatted("gui.slot_machine.spin", 5));
		reportButton = new GuiButton(999, 0, 0, 20, 20, "!");
	}
	
	private String getDisplayName() {
		return (container.getCustomName() != null) ? container.getCustomName() : I18n.translateToLocal("tile.slot_machine.name");
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
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		super.drawScreen(mouseX, mouseY, partialTicks);

		if(isHovering(mouseX, mouseY, reportButton)) {
			drawHoveringText(TextFormatting.RED + "Report in case the slot machine does not reward money when at least one row is a winning line", mouseX, mouseY);
		}
		
		if(isHovering(mouseX, mouseY, guiLeft + 8, guiTop + 166, guiLeft + 170, guiTop + 179)) {
			drawHoveringText(I18n.translateToLocalFormatted("gui.slot_machine.current_win", container.getSessionWin()>=0?(TextFormatting.GREEN + "+" + container.getSessionWin()):(TextFormatting.RED + "" + container.getSessionWin())), mouseX, mouseY);
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
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id){
		case 0:
			container.spin(SpinMode.ONE);
			break;
		case 1:
			container.spin(SpinMode.THREE);
			break;
		case 2:
			container.spin(SpinMode.FIVE);
			break;
		case 999:
			container.report();
			Minecraft.getMinecraft().player.closeScreen();
			Minecraft.getMinecraft().player.sendChatMessage("A report has been send. Please wait until the administrator contacts you.");
			break;
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
    }
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(spin1LButton);
		this.buttonList.add(spin3LButton);
		this.buttonList.add(spin5LButton);
		this.buttonList.add(reportButton);
		
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
	}
}
