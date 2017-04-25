package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumPlayerStat;
import lu.kremi151.minamod.packet.message.MessageUseElevator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElevatorNew extends GuiNoInventory{

	private static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/elevator.png");
	
	private final static int BTN_PREVIOUS = 0;
	private final static int BTN_NEXT = 1;
	private final static int BTN_FLOORS_START = 20;

	private final static int BTN_WIDTH = 48;
	private final static int BTN_HEIGHT = 20;
	
	private final GuiButton btnPreviousFloors, btnNextFloors;
	private final GuiButton floorButtons[] = new GuiButton[15];
	
	private int floors;
	private int currentFloor;
	private int floorButtonsSection = 0;

	public GuiElevatorNew(int floors, int currentFloor){
		this.floors = floors;
		this.currentFloor = currentFloor;
		
		btnPreviousFloors = new GuiButton(BTN_PREVIOUS, 0, 0, BTN_WIDTH, BTN_HEIGHT, I18n.translateToLocal("gui.elevator.previous"));
		btnNextFloors = new GuiButton(BTN_NEXT, 0, 0, BTN_WIDTH, BTN_HEIGHT, I18n.translateToLocal("gui.elevator.next"));
		
		floorButtonsSection = currentFloor / floorButtons.length;
		
		for(int i = 0 ; i < floorButtons.length ; i++){
			floorButtons[i] = new GuiButton(BTN_FLOORS_START + i, 0, 0, BTN_WIDTH, BTN_HEIGHT, "");
		}
	}
	
	private void setupPositions(){
		final int btn_x = guiLeft + 14;
		final int btn_y = guiTop + 69;
		
		btnPreviousFloors.xPosition = guiLeft + 62;
		btnPreviousFloors.yPosition = guiTop + 45;
		
		for(int i = 0 ; i < floorButtons.length ; i++){
			GuiButton btn = floorButtons[i];
			final int j = i % 3;
			final int k = i / 3;
			
			btn.xPosition = btn_x + (j * BTN_WIDTH);
			btn.yPosition = btn_y + (k * BTN_HEIGHT);
		}
		
		btnNextFloors.xPosition = guiLeft + 62;
		btnNextFloors.yPosition = guiTop + 173;
		
		refreshFloorButtons();
	}
	
	private void refreshFloorButtons(){
		while(floors - (floorButtonsSection * floorButtons.length) < 0)floorButtonsSection--;
		int base = floorButtonsSection * floorButtons.length;
		for(int i = 0 ; i < floorButtons.length ; i++){
			int btnfloor = base + i;
			if(base + i >= floors){
				floorButtons[i].displayString = "";
				floorButtons[i].enabled = false;
			}else{
				floorButtons[i].displayString = "" + (btnfloor + 1);
				floorButtons[i].enabled = btnfloor != currentFloor;
			}
		}

		btnNextFloors.enabled = floors - (((floorButtonsSection + 1) * floorButtons.length)) > 0;
		btnPreviousFloors.enabled = (floorButtonsSection - 1) >= 0;
	}
	
	private void useElevator(int level){
		MessageUseElevator msg = new MessageUseElevator(level);
		MinaMod.getMinaMod().getPacketDispatcher().sendToServer(msg);
		Minecraft.getMinecraft().player.closeScreen();
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id){
		case BTN_PREVIOUS:
			floorButtonsSection--;
			refreshFloorButtons();
			break;
		case BTN_NEXT:
			floorButtonsSection++;
			refreshFloorButtons();
			break;
		default:
			if(guibutton.id >= BTN_FLOORS_START){
				int level = guibutton.id - BTN_FLOORS_START;
				int relativeFloor1 = ((floorButtonsSection * floorButtons.length) + level) - currentFloor;
				useElevator(relativeFloor1);
			}
			break;
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw text and stuff here
		// the parameters for drawString are: string, x, y, color

		//fontRendererObj.drawString(I18n.translateToLocal("gui.coin_bag.name"), 8, 10, 4210752);
		// draws "Inventory" or your regional equivalent
		fontRenderer.drawString(I18n.translateToLocal("gui.elevator.title"), 8, 10, 4210752);

		String lc = I18n.translateToLocal("gui.elevator.choose_level");
		final int lcwidth = this.fontRenderer.getStringWidth(lc);

		fontRenderer.drawString(lc, (xSize - lcwidth) / 2, 32, 4210752);
		
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
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(btnPreviousFloors);
		this.buttonList.add(btnNextFloors);
		for(GuiButton btn : floorButtons){
			this.buttonList.add(btn);
		}
		
		setupPositions();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
}
