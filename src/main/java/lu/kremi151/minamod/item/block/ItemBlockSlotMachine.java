package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.block.BlockSlotMachine;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.util.nbtmath.MathParseException;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSlotMachine extends ItemBlock{

	public ItemBlockSlotMachine(BlockSlotMachine block) {
		super(block);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
			NBTTagCompound stackNBT = stack.getSubCompound("BlockEntityTag");
			if(stackNBT != null) {
				//Little hack to bypass the (stupid? unnecessary?) NBT merges
                NBTBase rpf = stackNBT.getTag("RowPriceFunction");
                if(rpf != null) {
                	TileEntitySlotMachine tileentity = (TileEntitySlotMachine)world.getTileEntity(pos);
                    try {
						tileentity.setRowPriceFunction(tileentity.parseFunction(rpf));
					} catch (MathParseException e) {
						System.err.println("Row price function could not be loaded");
						e.printStackTrace();
					}
                }
			}
			return true;
		}else {
			return false;
		}
    }

}
