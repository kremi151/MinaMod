package lu.kremi151.minamod.commands;

import java.util.Optional;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

public class CommandResetSlotMachine extends MinaPlayerCommandBase{

	CommandResetSlotMachine(CommandBase parent) {
		super(parent);
	}

	@Override
	public String getName() {
		return "reset-slot-machine";
	}

	@Override
	public String getDescription() {
		return "Repairs a mulfunctioning slot machine";
	}

	@Override
	public void execute(MinecraftServer server, EntityPlayer player, String[] arg) throws CommandException {
		if(!player.world.isRemote) {
			Optional<RayTraceResult> op = MinaUtils.rayTrace(player);
			if(op.isPresent()) {
				BlockPos pos = op.get().getBlockPos();
				IBlockState state = player.world.getBlockState(pos);
				if(state.getBlock() == MinaBlocks.SLOT_MACHINE) {
					TileEntitySlotMachine te = (TileEntitySlotMachine) player.world.getTileEntity(pos);
					te.resetState();
					TextHelper.sendTranslateableChatMessage(player, TextFormatting.GREEN, "msg.slotmachine.reset.executed");
				}else {
					TextHelper.sendTranslateableErrorMessage(player, "msg.slotmachine.reset.no_slot_machine");
				}
			}else {
				TextHelper.sendTranslateableErrorMessage(player, "msg.slotmachine.reset.no_slot_machine");
			}
		}
	}

	@Override
	public Optional<String> getPermissionNode() {
		return Optional.of(MinaPermissions.CMD_RESET_SLOT_MACHINE);
	}

}
