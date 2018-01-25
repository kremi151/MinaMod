package lu.kremi151.minamod.client;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockElevatorFloor;
import lu.kremi151.minamod.block.tileentity.TileEntityElevatorControl;
import lu.kremi151.minamod.network.MessageUseElevator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElevatorNew extends GuiNoInventory{

	private static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/elevator/default.png");
	
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
	
	private final HashMap<Integer, String> floorNames = new HashMap<>();

	public GuiElevatorNew(int floors, int currentFloor){
		this.floors = floors;
		this.currentFloor = currentFloor;
		
		btnPreviousFloors = new GuiButton(BTN_PREVIOUS, 0, 0, BTN_WIDTH, BTN_HEIGHT, I18n.translateToLocal("gui.elevator.previous"));
		btnNextFloors = new GuiButton(BTN_NEXT, 0, 0, BTN_WIDTH, BTN_HEIGHT, I18n.translateToLocal("gui.elevator.next"));
		
		floorButtonsSection = currentFloor / floorButtons.length;
		
		for(int i = 0 ; i < floorButtons.length ; i++){
			floorButtons[i] = new GuiButton(BTN_FLOORS_START + i, 0, 0, BTN_WIDTH, BTN_HEIGHT, "");
		}
		
		World world = Minecraft.getMinecraft().player.world;
		int currentLevel = -1;
		for(int i = 0 ; i < world.getHeight() ; i++) {
			BlockPos pos = new BlockPos(Minecraft.getMinecraft().player.posX, i, Minecraft.getMinecraft().player.posZ);
			IBlockState state = world.getBlockState(pos);
			if(BlockElevatorFloor.isValidLevelFloor(state)) {
				currentLevel++;
			}else if(state.getBlock() == MinaBlocks.ELEVATOR_CONTROL) {
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityElevatorControl && ((TileEntityElevatorControl)te).hasName()) {
					floorNames.put(currentLevel, ((TileEntityElevatorControl)te).getName());//TODO: Optimize
				}
			}
		}
	}
	
	private void setupPositions(){
		final int btn_x = guiLeft + 14;
		final int btn_y = guiTop + 69;
		
		btnPreviousFloors.x = guiLeft + 62;
		btnPreviousFloors.y = guiTop + 45;
		
		for(int i = 0 ; i < floorButtons.length ; i++){
			GuiButton btn = floorButtons[i];
			final int j = i % 3;
			final int k = i / 3;
			
			btn.x = btn_x + (j * BTN_WIDTH);
			btn.y = btn_y + (k * BTN_HEIGHT);
		}
		
		btnNextFloors.x = guiLeft + 62;
		btnNextFloors.y = guiTop + 173;
		
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
				useElevator(getFloorLevelFromButton(guibutton));
			}
			break;
		}
	}
	
	private int getFloorLevelFromButton(GuiButton btn) {
		if(btn.id >= BTN_FLOORS_START){
			int level = btn.id - BTN_FLOORS_START;
			return ((floorButtonsSection * floorButtons.length) + level) - currentFloor;
		}else {
			return 0;
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(getFloorName(currentFloor), 8, 10, 4210752);
		
		String title = I18n.translateToLocal("gui.elevator.select_level");
		fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 32, 4210752);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		for(GuiButton btn : floorButtons) {
			if(btn.enabled && GuiUtils.isHovering(mouseX, mouseY, btn)) {
				int level = getFloorLevelFromButton(btn);
				String cname = floorNames.get(level);
				if(cname != null) {
					drawHoveringText(cname, mouseX, mouseY);
				}
				break;
			}
		}
	}
	
	private String getFloorName(int level) {
		return floorNames.getOrDefault(level, I18n.translateToLocalFormatted("gui.elevator.level", level + 1));
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
