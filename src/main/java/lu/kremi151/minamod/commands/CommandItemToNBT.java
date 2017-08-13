package lu.kremi151.minamod.commands;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class CommandItemToNBT extends MinaPlayerCommandBase{
	
	CommandItemToNBT(CommandBase parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "item-to-nbt";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		if(!player.inventory.getCurrentItem().isEmpty()){
			ItemStack is = player.inventory.getCurrentItem();
			NBTTagCompound nbt = new NBTTagCompound();
			is.writeToNBT(nbt);
			File f = null;
			try {
				if(arg.length >= 1){
					f = new File(arg[0]);
				}else{
					f = new File("item.nbt");
				}
				CompressedStreamTools.write(nbt, f);
				TextHelper.sendTranslateableChatMessage(player, TextFormatting.GREEN, "msg.cmd.item_to_nbt.saved", f.getAbsolutePath());
			} catch (IOException e) {
				TextHelper.sendTranslateableErrorMessage(player, "msg.cmd.item_to_nbt.failed", f.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.ITEM_TO_NBT_CMD);
	}

	@Override
	public String getDescription() {
		return "Writes the NBT data structure of the current held item to disk";
	}
	
	@Override
	public String[] getArguments() {
		return new String[] {"[<file>]"};
	}

}
