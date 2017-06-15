package lu.kremi151.minamod.block;

import lu.kremi151.minamod.block.BlockMinaPlanks.EnumType;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPalmLog extends BlockStandaloneLog{
	
	public static final PropertyBool HEAD = PropertyBool.create("head");

	public BlockPalmLog(EnumType type) {
		super(type);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(HEAD, false));
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
