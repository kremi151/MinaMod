package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockMinaPlanks.EnumType;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPalmLog extends BlockStandaloneLog{
	
	public static final PropertyBool HEAD = PropertyBool.create("head");

	public BlockPalmLog(EnumType type) {
		super(type);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(HEAD, false));
        this.setTickRandomly(true);
	}
	
	@Override
	public int tickRate(World worldIn)
    {
        return 200;
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if(!worldIn.isRemote && state.getValue(HEAD) && rand.nextInt(40) == 0){
			EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
			BlockPos cpos = pos.offset(facing);
			if(worldIn.isAirBlock(cpos)){
				worldIn.setBlockState(cpos, MinaBlocks.COCONUT.getDefaultState());
			}
		}
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return super.getStateFromMeta(meta).withProperty(HEAD, (meta & 4) > 0);
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
    {
        return super.getMetaFromState(state) | (state.getValue(HEAD) ? 4 : 0);
    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LOG_AXIS, HEAD});
    }

}
