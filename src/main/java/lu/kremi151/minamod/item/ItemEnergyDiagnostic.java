package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.interfaces.IDiagnosable;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemEnergyDiagnostic extends Item{

	public ItemEnergyDiagnostic() {
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			if(state.getBlock() instanceof IDiagnosable) {
				TextHelper.sendTranslateableChatMessage(player, TextFormatting.AQUA, "msg.diagnose.title");
				((IDiagnosable)state.getBlock()).onDiagnose(worldIn, pos, player);
			}else {
				TextHelper.sendTranslateableChatMessage(player, TextFormatting.RED, "msg.diagnose.fail");
			}
		}
		return EnumActionResult.SUCCESS;
    }
}
