package lu.kremi151.minamod.block;

import lu.kremi151.minamod.block.tileentity.TileEntityEnergySource;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnergySource extends Block{

	public BlockEnergySource() {
		super(Material.IRON);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityEnergySource();
    }

}
