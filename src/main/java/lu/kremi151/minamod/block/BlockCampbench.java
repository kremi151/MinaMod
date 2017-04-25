package lu.kremi151.minamod.block;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCampbench extends Block{
	
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
	public static final PropertyEnum WOOD = PropertyEnum.create("wood", BlockPlanks.EnumType.class, BlockPlanks.EnumType.ACACIA, BlockPlanks.EnumType.BIRCH, BlockPlanks.EnumType.DARK_OAK, BlockPlanks.EnumType.SPRUCE);

	protected static final AxisAlignedBB rot_norm_aabb = new AxisAlignedBB(0.125d, 0d, 0.375d, 0.875d, 0.3125d, 0.625d);
	protected static final AxisAlignedBB rot_orth_aabb = new AxisAlignedBB(0.375d, 0d, 0.125d, 0.625d, 0.3125d, 0.875d);
	protected static final AxisAlignedBB rot_dia_aabb = new AxisAlignedBB(0.125d, 0d, 0.125d, 0.875d, 0.3125d, 0.875d);
	
	public BlockCampbench() {
		super(Material.WOOD, MapColor.WOOD);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(ROTATION, 0).withProperty(WOOD, BlockPlanks.EnumType.ACACIA));
		this.setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0 ; i < 4 ; i++)list.add(new ItemStack(itemIn, 1, i));
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch(state.getValue(ROTATION)){
		case 1:
		case 3: return rot_dia_aabb;
		case 2: return rot_orth_aabb;
		default: return rot_norm_aabb;
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(world.isRemote)return false;
		return MinaUtils.makePlayerSitOnBlock(player, world, pos);
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
    	BlockPlanks.EnumType type;
    	switch(meta & 3){
    	case 1:
    		type = BlockPlanks.EnumType.BIRCH;
    		break;
    	case 2:
    		type = BlockPlanks.EnumType.DARK_OAK;
    		break;
    	case 3:
    		type = BlockPlanks.EnumType.SPRUCE;
    		break;
    	default:
    		type = BlockPlanks.EnumType.ACACIA;
    		break;
    	}
        return this.getDefaultState().withProperty(WOOD, type).withProperty(ROTATION, (meta >> 2) & 3);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	int meta = 0;
    	BlockPlanks.EnumType type = (BlockPlanks.EnumType)state.getValue(WOOD);
    	switch(type){
    	case DARK_OAK: meta = 1; break;
    	case SPRUCE: meta = 2; break;
    	case ACACIA: meta = 3; break;
    	default: break;
    	}
        return meta | ((state.getValue(ROTATION) & 3) << 2);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {ROTATION, WOOD});
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
    	IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    	
    	int rot = MinaUtils.positiveModulo(MathHelper.floor((double)(placer.rotationYaw * 8.0F / 360.0F) + 0.5D), 4);
    	
    	return state.withProperty(ROTATION, rot);
    }
    
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot){
		return state.withProperty(ROTATION, (state.getValue(ROTATION) + rot.ordinal()) % 4);
	}
	
	@Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn){
		return state;//TODO
	}

}
