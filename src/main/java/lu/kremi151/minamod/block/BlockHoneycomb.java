package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.entity.EntityBee;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHoneycomb extends BlockCustomHorizontal{

    public static final PropertyBool HAS_HONEY = PropertyBool.create("has_honey");
    public static final PropertyBool HAS_BEES = PropertyBool.create("has_bees");

	public BlockHoneycomb() {
		super(Material.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HAS_HONEY, false).withProperty(HAS_BEES, false));
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setTickRandomly(true);
		setHardness(1.0F);
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public Item getItemDropped(IBlockState a1, Random a2, int a3){
		return MinaItems.HONEYWABE;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random r)
    {
    	super.updateTick(world, pos, state, r);
    	if(!world.isRemote && !world.isRaining()){
    		if(state.getValue(HAS_BEES)){
				if(r.nextInt(14) == 0){
        			world.setBlockState(pos, state.withProperty(HAS_HONEY, true));
    			}
    			if(FeatureList.enable_bees && r.nextInt(2) == 0){
    				EnumFacing ef = (EnumFacing)state.getValue(FACING);
    				BlockPos spawnPos = pos.add(ef.getDirectionVec());
    				EntityBee b = new EntityBee(world);
    				b.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
    				world.spawnEntity(b);
    			}
			}else{
				int rx = (pos.getX() - 4) + r.nextInt(9), rz = (pos.getZ() - 4) + r.nextInt(9);
				int ry = MinaUtils.getHeightValue(world, rx, rz);
				
				int factor = (world.getBlockState(new BlockPos(rx,ry,rz)).getBlock() instanceof BlockFlower)?5:200;
				
				if(r.nextInt(factor) == 0){
					world.setBlockState(pos, state.withProperty(HAS_BEES, true));
				}
			}
    	}
    }
	
	@Override
	public int quantityDropped(Random r){
		return 2 + r.nextInt(3);
	}
	
	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    }
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(entityIn.getClass() == EntityBee.class){
        	entityIn.setDead();
			if(!(Boolean) state.getValue(HAS_BEES)){
				worldIn.setBlockState(pos, state.withProperty(HAS_BEES, true));
			}
        }
    }

	@Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		if(!worldIn.isRemote && state.getValue(HAS_BEES)){
			int q = 7 + worldIn.rand.nextInt(7);
			for(int i = 0 ; i < q ; i++){
				EntityBee b = new EntityBee(worldIn);
				b.setPosition(pos.getX(), pos.getY(), pos.getZ());
				worldIn.spawnEntity(b);
			}
		}
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(HAS_HONEY, (meta & 4) > 0).withProperty(HAS_BEES, (meta & 8) > 0);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
		boolean hh = (Boolean) state.getValue(HAS_HONEY);
		boolean bb = (Boolean) state.getValue(HAS_BEES);
		int meta = ((bb?1:0) << 3) | ((hh?1:0) << 2) | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        return meta;
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, HAS_HONEY, HAS_BEES});
    }

}
