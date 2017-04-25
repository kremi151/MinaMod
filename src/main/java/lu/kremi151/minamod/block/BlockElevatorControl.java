package lu.kremi151.minamod.block;

import java.util.Iterator;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntityElevatorControl;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class BlockElevatorControl extends Block{
	
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockElevatorControl() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(3.0F);
		setResistance(5.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return world.isSideSolid(pos.west(), EnumFacing.EAST, true) ||
               world.isSideSolid(pos.east(), EnumFacing.WEST, true) ||
               world.isSideSolid(pos.north(), EnumFacing.SOUTH, true) ||
               world.isSideSolid(pos.south(), EnumFacing.NORTH, true);
    }

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (facing.getAxis().isHorizontal() && this.canBlockStay(world, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing enumfacing1;

            do
            {
                if (!iterator.hasNext())
                {
                    return this.getDefaultState();
                }

                enumfacing1 = (EnumFacing)iterator.next();
            }
            while (!this.canBlockStay(world, pos, enumfacing1));

            return this.getDefaultState().withProperty(FACING, enumfacing1);
        }
    }


    protected boolean canBlockStay(World world, BlockPos pos, EnumFacing facing)
    {
        return world.isSideSolid(pos.offset(facing.getOpposite()), facing, true);
    }

	protected static final AxisAlignedBB bb_0 = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d);
	protected static final AxisAlignedBB bb_1 = new AxisAlignedBB(0.0d, 0.25d, 0.875d, 1.0d, 0.75d, 1.0d);
	protected static final AxisAlignedBB bb_2 = new AxisAlignedBB(0.0d, 0.25d, 0.0d, 1.0d, 0.75d, 0.125d);
	protected static final AxisAlignedBB bb_3 = new AxisAlignedBB(0.875d, 0.25d, 0.0d, 1.0d, 0.75d, 1.0d);
	protected static final AxisAlignedBB bb_4 = new AxisAlignedBB(0.0d, 0.25d, 0.0d, 0.125d, 0.75d, 1.0d);

	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        switch (SwitchEnumFacing.FACING_LOOKUP[enumfacing.ordinal()])
        {
            case 1:
                return bb_1;
            case 2:
                return bb_2;
            case 3:
                return bb_3;
            case 4:
                return bb_4;
        }
        return bb_0;
	}

    /**
     * Called when a neighboring block changes.
     */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos origin)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        IBlockState bs = world.getBlockState(pos.offset(enumfacing.getOpposite()));
        if (!bs.getBlock().getMaterial(bs).isSolid())
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityElevatorControl();
    }

	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }

	@Override
    public boolean isPassable(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

	@Override
    public boolean isOpaqueCube(IBlockState ibs)
    {
        return false;
    }

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(PermissionAPI.hasPermission(player, MinaPermissions.ALLOW_ELEVATOR_USE)){
			BlockPos standingBlock = new BlockPos(player.posX, player.posY - 1, player.posZ);
			if(world.getBlockState(standingBlock).getBlock() != MinaBlocks.ELEVATOR_FLOOR){
				if(!world.isRemote)TextHelper.sendTranslateableErrorMessage(player, "msg.elevator.too_far_away");
				return true;
			}else if(standingBlock.getX() != pos.getX() || standingBlock.getZ() != pos.getZ()){
				if(!world.isRemote)TextHelper.sendTranslateableErrorMessage(player, "msg.elevator.wrong_build");
				return true;
			}
			int currentFloor = -1;
			int floors = 0;
			for(int y = 0; y < world.getHeight(); y++){
				BlockPos npos = new BlockPos(pos.getX(), y, pos.getZ());
				if(BlockElevatorFloor.isValidLevelFloor(world.getBlockState(npos))){
					if(npos.equals(standingBlock)){
						currentFloor = floors;
					}
					floors++;
				}
			}
			if(currentFloor != -1 && floors != 0){
				player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdElevator, world, floors, currentFloor, 0);
			}else{
				if(!world.isRemote)TextHelper.sendTranslateableErrorMessage(player, "msg.misc.unexpected_error");
			}
		}else{
			TextHelper.sendTranslateableErrorMessage(player, "msg.misc.missing_perm");
		}
        return true;
    }

    static final class SwitchEnumFacing
        {
            static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

            static
            {
                try
                {
                    FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }

	
}
