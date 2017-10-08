package lu.kremi151.minamod.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDrillItem {

	boolean onDrillUsed(World world, BlockPos target, EntityPlayer player, ItemStack stack);
}
