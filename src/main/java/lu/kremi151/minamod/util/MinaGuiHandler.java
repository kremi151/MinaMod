package lu.kremi151.minamod.util;

import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.block.tileentity.TileEntityCollector;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.inventory.container.ContainerAmuletInventory;
import lu.kremi151.minamod.inventory.container.ContainerAutoFeeder;
import lu.kremi151.minamod.inventory.container.ContainerCoinBag;
import lu.kremi151.minamod.inventory.container.ContainerCollector;
import lu.kremi151.minamod.inventory.container.ContainerLetterbox;
import net.minecraft.entity.player.EntityPlayer;
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
		}
		return null;
	}

}
