package lu.kremi151.minamod.util;

import lu.kremi151.minamod.block.tileentity.TileEntityAutoCrafter;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.block.tileentity.TileEntityCollector;
import lu.kremi151.minamod.block.tileentity.TileEntityFilter;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.container.ContainerAmuletInventory;
import lu.kremi151.minamod.container.ContainerAutoCrafter;
import lu.kremi151.minamod.container.ContainerAutoFeeder;
import lu.kremi151.minamod.container.ContainerCoinBag;
import lu.kremi151.minamod.container.ContainerCollector;
import lu.kremi151.minamod.container.ContainerFilter;
import lu.kremi151.minamod.container.ContainerKeyChain;
import lu.kremi151.minamod.container.ContainerLetterbox;
import lu.kremi151.minamod.container.ContainerSelectItem;
import lu.kremi151.minamod.container.ContainerSlotMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MinaGuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id){
		case IDRegistry.guiIdLetterbox:
			return new ContainerLetterbox(player, (TileEntityLetterbox) world.getTileEntity(new BlockPos(x,y,z)));
		case IDRegistry.guiIdAutoFeeder:
			return new ContainerAutoFeeder(player, (TileEntityAutoFeeder) world.getTileEntity(new BlockPos(x,y,z)));
		case IDRegistry.guiIdCoinBag:
			return new ContainerCoinBag(player);
		case IDRegistry.guiIdCollector:
			return new ContainerCollector(player, (TileEntityCollector) world.getTileEntity(new BlockPos(x,y,z)));
		case IDRegistry.guiIdAmulets:
			return new ContainerAmuletInventory(player);
		case IDRegistry.guiIdSlotMachine:
			return new ContainerSlotMachine(player, (TileEntitySlotMachine) world.getTileEntity(new BlockPos(x,y,z)));
		case IDRegistry.guiIdFilter:
			return new ContainerFilter(player, (TileEntityFilter) world.getTileEntity(new BlockPos(x,y,z)));
		case IDRegistry.guiIdSelectItem:
			return new ContainerSelectItem(player, ContainerSelectItem.titleForId(y), x);
		case IDRegistry.guiIdKeyChain:
			return new ContainerKeyChain(player, x);
		case IDRegistry.guiIdExtendedCrafting:
			return new ContainerWorkbench(player.inventory, world, new BlockPos(x, y, z));
		case IDRegistry.guiIdAutoCrafter:
			return new ContainerAutoCrafter(player, (TileEntityAutoCrafter) world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id){
		case IDRegistry.guiIdLetterbox:
			return new lu.kremi151.minamod.client.GuiLetterbox(new ContainerLetterbox(player, (TileEntityLetterbox) world.getTileEntity(new BlockPos(x,y,z))));
		case IDRegistry.guiIdElevator:
			return new lu.kremi151.minamod.client.GuiElevatorNew(x, y);
		case IDRegistry.guiIdAutoFeeder:
			return new lu.kremi151.minamod.client.GuiAutoFeeder(new ContainerAutoFeeder(player, (TileEntityAutoFeeder) world.getTileEntity(new BlockPos(x,y,z))));
		case IDRegistry.guiIdCoinBag:
			return new lu.kremi151.minamod.client.GuiCoinBag(new ContainerCoinBag(player));
		case IDRegistry.guiIdCollector:
			return new lu.kremi151.minamod.client.GuiCollector(new ContainerCollector(player, (TileEntityCollector) world.getTileEntity(new BlockPos(x,y,z))));
		case IDRegistry.guiIdHerbGuide:
			return new lu.kremi151.minamod.client.GuiHerbBook(x==1?EnumHand.MAIN_HAND:EnumHand.OFF_HAND);
		case IDRegistry.guiIdAmulets:
			return new lu.kremi151.minamod.client.GuiAmuletInventory(new ContainerAmuletInventory(player));
		case IDRegistry.guiIdSlotMachine:
			return new lu.kremi151.minamod.client.GuiSlotMachine(new lu.kremi151.minamod.container.ContainerSlotMachineClient(player, (TileEntitySlotMachine) world.getTileEntity(new BlockPos(x,y,z))));
		case IDRegistry.guiIdFilter:
			return new lu.kremi151.minamod.client.GuiFilter(new ContainerFilter(player, (TileEntityFilter) world.getTileEntity(new BlockPos(x,y,z))));
		case IDRegistry.guiIdSelectItem:
			return new lu.kremi151.minamod.client.GuiSelectItem(new ContainerSelectItem(player, ContainerSelectItem.titleForId(y), x));
		case IDRegistry.guiIdKeyChain:
			return new lu.kremi151.minamod.client.GuiKeyChain(new ContainerKeyChain(player, x));
		case IDRegistry.guiIdExtendedCrafting:
			return new lu.kremi151.minamod.client.GuiExtendedCrafting(player.inventory, world, new BlockPos(x, y, z));
		case IDRegistry.guiIdAutoCrafter:
			return new lu.kremi151.minamod.client.GuiAutoCrafter(new ContainerAutoCrafter(player, (TileEntityAutoCrafter) world.getTileEntity(new BlockPos(x,y,z))));
		}
		return null;
	}

}
