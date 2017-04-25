package lu.kremi151.minamod.block;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.tileentity.TileEntityLock;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.item.ItemKey;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLock extends Block
{
    public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLock.EnumOrientation.class);

    public BlockLock()
    {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLock.EnumOrientation.NORTH));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        setHardness(0.5F);
		setSoundType(SoundType.STONE);
    }

    @Override
    public boolean isOpaqueCube(IBlockState bs)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState bs)
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.isSideSolid(pos.west(),  EnumFacing.EAST ) ||
               worldIn.isSideSolid(pos.east(),  EnumFacing.WEST ) ||
               worldIn.isSideSolid(pos.north(), EnumFacing.SOUTH) ||
               worldIn.isSideSolid(pos.south(), EnumFacing.NORTH) ||
               worldIn.isSideSolid(pos.down(),  EnumFacing.UP   ) ||
               worldIn.isSideSolid(pos.up(),    EnumFacing.DOWN );
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (worldIn.isSideSolid(pos.offset(facing.getOpposite()), facing))
        {
            return iblockstate.withProperty(FACING, BlockLock.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        else
        {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing enumfacing1;

            do
            {
                if (!iterator.hasNext())
                {
                    if(worldIn.isSideSolid(pos.down(), EnumFacing.UP))
                    {
                        return iblockstate.withProperty(FACING, BlockLock.EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
                    }

                    return iblockstate;
                }

                enumfacing1 = (EnumFacing)iterator.next();
            }
            while (enumfacing1 == facing || !worldIn.isSideSolid(pos.offset(enumfacing1.getOpposite()), enumfacing1));

            return iblockstate.withProperty(FACING, BlockLock.EnumOrientation.forFacings(enumfacing1, placer.getHorizontalFacing()));
        }
    }

//    public static int getMetadataForFacing(EnumFacing facing)
//    {
//        switch (BlockCardReader.SwitchEnumFacing.FACING_LOOKUP[facing.ordinal()])
//        {
//            case 1:
//                return 0;
//            case 2:
//                return 5;
//            case 3:
//                return 4;
//            case 4:
//                return 3;
//            case 5:
//                return 2;
//            case 6:
//                return 1;
//            default:
//                return -1;
//        }
//    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing dir = ((BlockLock.EnumOrientation)state.getValue(FACING)).getFacing();
        if (this.checkForDrop(worldIn, pos) && !worldIn.isSideSolid(pos.offset(dir.getOpposite()), dir))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkForDrop(World worldIn, BlockPos pos)
    {
        if (this.canPlaceBlockAt(worldIn, pos))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }
    

	protected static final AxisAlignedBB bb_1 = new AxisAlignedBB(0.0d, 0.2d, 0.5d - 0.1875d, 0.1875d * 2.0d, 0.8d, 0.5d + 0.1875d);
	protected static final AxisAlignedBB bb_2 = new AxisAlignedBB(1.0d - 0.1875d * 2.0d, 0.2d, 0.5d - 0.1875d, 1.0d, 0.8d, 0.5d + 0.1875d);
	protected static final AxisAlignedBB bb_3 = new AxisAlignedBB(0.5d - 0.1875d, 0.2d, 0.0d, 0.5d + 0.1875d, 0.8d, 0.1875d * 2.0d);
	protected static final AxisAlignedBB bb_4 = new AxisAlignedBB(0.5d - 0.1875d, 0.2d, 1.0d - 0.1875d * 2.0d, 0.5d + 0.1875d, 0.8d, 1.0d);
	protected static final AxisAlignedBB bb_5_6 = new AxisAlignedBB(0.25d, 0.0d, 0.25d, 0.75d, 0.6d, 0.75d);
	protected static final AxisAlignedBB bb_7_8 = new AxisAlignedBB(0.25d, 0.4F, 0.25d, 0.75d, 1.0F, 0.75d);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch (BlockLock.SwitchEnumFacing.ORIENTATION_LOOKUP[((BlockLock.EnumOrientation)state.getValue(FACING)).ordinal()])
        {
            case 1:
                return bb_1;
            case 2:
                return bb_2;
            case 3:
                return bb_3;
            case 4:
                return bb_4;
            case 5:
            case 6:
                return bb_5_6;
            case 7:
            case 8:
                return bb_7_8;
        }
		return null;
	}

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
        	ItemStack is = playerIn.inventory.getCurrentItem();
        	if(!is.isEmpty() && is.hasCapability(IKey.CAPABILITY_KEY, null)){
        		IKey cap = is.getCapability(IKey.CAPABILITY_KEY, null);
            	TileEntityLock te = (TileEntityLock) worldIn.getTileEntity(pos);
            	if(te.isRegistred()){
            		if(te.isPowered()){
            			if(te.checkUUID(cap))te.setPowered(false);
            		}else{
            			if(te.checkUUID(cap))te.setPowered(true);
            		}
            	}else{
            		Optional<UUID> pkey = cap.getPrimaryKey();
            		if(pkey.isPresent()){
                		te.setUUID(pkey.get());
                		te.setPowered(true);
            		}
            	}
    	        worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.3F, te.isPowered() ? 0.6F : 0.5F, false);
    	        worldIn.notifyNeighborsOfStateChange(pos, this, true);
    	        EnumFacing enumfacing1 = ((BlockLock.EnumOrientation)state.getValue(FACING)).getFacing();
    	        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing1.getOpposite()), this, true);
        	}
            return true;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	TileEntityLock te = (TileEntityLock) worldIn.getTileEntity(pos);
        if (te.isPowered())
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, true);
            EnumFacing enumfacing = ((BlockLock.EnumOrientation)state.getValue(FACING)).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, true);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	TileEntityLock te = (TileEntityLock) blockAccess.getTileEntity(pos);
        return te.isPowered() ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	TileEntityLock te = (TileEntityLock) blockAccess.getTileEntity(pos);
        return !te.isPowered() ? 0 : (((BlockLock.EnumOrientation)blockState.getValue(FACING)).getFacing() == side ? 15 : 0);
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, BlockLock.EnumOrientation.byMetadata(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((BlockLock.EnumOrientation)state.getValue(FACING)).getMetadata();

//        if (((Boolean)state.getValue(POWERED)).booleanValue())
//        {
//            i |= 8;
//        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public static enum EnumOrientation implements IStringSerializable
    {
        DOWN_X(0, "down_x", EnumFacing.DOWN),
        EAST(1, "east", EnumFacing.EAST),
        WEST(2, "west", EnumFacing.WEST),
        SOUTH(3, "south", EnumFacing.SOUTH),
        NORTH(4, "north", EnumFacing.NORTH),
        UP_Z(5, "up_z", EnumFacing.UP),
        UP_X(6, "up_x", EnumFacing.UP),
        DOWN_Z(7, "down_z", EnumFacing.DOWN);
        private static final BlockLock.EnumOrientation[] META_LOOKUP = new BlockLock.EnumOrientation[values().length];
        private final int meta;
        private final String name;
        private final EnumFacing facing;

        private EnumOrientation(int meta, String name, EnumFacing facing)
        {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public EnumFacing getFacing()
        {
            return this.facing;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        public static BlockLock.EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static BlockLock.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing)
        {
            switch (BlockLock.SwitchEnumFacing.FACING_LOOKUP[clickedSide.ordinal()])
            {
                case 1:
                    switch (BlockLock.SwitchEnumFacing.AXIS_LOOKUP[entityFacing.getAxis().ordinal()])
                    {
                        case 1:
                            return DOWN_X;
                        case 2:
                            return DOWN_Z;
                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }
                case 2:
                    switch (BlockLock.SwitchEnumFacing.AXIS_LOOKUP[entityFacing.getAxis().ordinal()])
                    {
                        case 1:
                            return UP_X;
                        case 2:
                            return UP_Z;
                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }
                case 3:
                    return NORTH;
                case 4:
                    return SOUTH;
                case 5:
                    return WEST;
                case 6:
                    return EAST;
                default:
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        @Override
		public String getName()
        {
            return this.name;
        }

        static
        {
            BlockLock.EnumOrientation[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockLock.EnumOrientation var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }

    static final class SwitchEnumFacing
        {
            static final int[] FACING_LOOKUP;

            static final int[] ORIENTATION_LOOKUP;

            static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

            static
            {
                try
                {
                    AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
                }
                catch (NoSuchFieldError var16)
                {
                    ;
                }

                try
                {
                    AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
                }
                catch (NoSuchFieldError var15)
                {
                    ;
                }

                ORIENTATION_LOOKUP = new int[BlockLock.EnumOrientation.values().length];

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.EAST.ordinal()] = 1;
                }
                catch (NoSuchFieldError var14)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.WEST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var13)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.SOUTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var12)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.NORTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var11)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.UP_Z.ordinal()] = 5;
                }
                catch (NoSuchFieldError var10)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.UP_X.ordinal()] = 6;
                }
                catch (NoSuchFieldError var9)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.DOWN_X.ordinal()] = 7;
                }
                catch (NoSuchFieldError var8)
                {
                    ;
                }

                try
                {
                    ORIENTATION_LOOKUP[BlockLock.EnumOrientation.DOWN_Z.ordinal()] = 8;
                }
                catch (NoSuchFieldError var7)
                {
                    ;
                }

                FACING_LOOKUP = new int[EnumFacing.values().length];

                try
                {
                    FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityLock();
    }

}
