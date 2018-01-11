package lu.kremi151.minamod.interfaces;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IDiagnosable {

	void onDiagnose(IBlockAccess world, BlockPos pos, ICommandSender subject);
	
}
