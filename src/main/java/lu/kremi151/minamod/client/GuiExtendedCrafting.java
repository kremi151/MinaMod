package lu.kremi151.minamod.client;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.client.element.GuiButtonItem;
import lu.kremi151.minamod.network.MessageCreateSketch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiExtendedCrafting extends GuiCrafting{

	private final GuiButtonItem btnSketch = new GuiButtonItem(0, 0, 0, new ItemStack(MinaItems.SKETCH));
	
	public GuiExtendedCrafting(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition) {
		super(playerInv, worldIn, blockPosition);
	}

	public GuiExtendedCrafting(InventoryPlayer playerInv, World worldIn) {
		super(playerInv, worldIn);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0) {
			MinaMod.getMinaMod().getPacketDispatcher().sendToServer(new MessageCreateSketch());
		}
	}
	
	@Override
	public void updateScreen()
    {
		super.updateScreen();
		this.btnSketch.enabled = !((ContainerWorkbench)this.inventorySlots).craftResult.isEmpty() && hasPaper();
    }
	
	private boolean hasPaper() {
		return Minecraft.getMinecraft().player.inventory.hasItemStack(new ItemStack(Items.PAPER));
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(this.btnSketch);
		setupPositions();
		
		btnSketch.enabled = false;
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
	
	private void setupPositions(){
		btnSketch.x = guiLeft + 122;
		btnSketch.y = guiTop + 61;
	}

}
