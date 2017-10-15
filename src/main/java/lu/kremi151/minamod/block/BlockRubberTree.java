package lu.kremi151.minamod.block;

import java.util.Random;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worldgen.WorldGenSurfacePlant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRubberTree extends BlockBush implements IGrowable{
	
	private static final PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);

	public BlockRubberTree() {
		super(Material.PLANTS, MapColor.RED);
		this.setDefaultState(blockState.getBaseState().withProperty(PART, Part.STAGE_0));
		this.setSoundType(SoundType.PLANT);
	}

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	Part part = state.getValue(PART);
    	if(part.multi) {
    		if(!this.canBlockStay(worldIn, pos, state)) {
    			boolean flag = !state.getValue(PART).hasUpperPart();
                BlockPos blockpos = flag ? pos : pos.up();
                BlockPos blockpos1 = flag ? pos.down() : pos;
                Block block = (Block)(flag ? this : worldIn.getBlockState(blockpos).getBlock());
                Block block1 = (Block)(flag ? worldIn.getBlockState(blockpos1).getBlock() : this);

                if (!flag) this.dropBlockAsItem(worldIn, pos, state, 0);

                if (block == this)
                {
                    worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                }

                if (block1 == this)
                {
                    worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
                }
    		}
    	}else {
    		super.checkAndDropBlock(worldIn, pos, state);
    	}
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state);
        if (state.getValue(PART).isUpper())
        {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());
            return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
        }
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (state.getValue(PART).isUpper())
        {
            return Items.AIR;
        }
        else
        {
            return MinaItems.RUBBER_TREE_BRANCH;
        }
    }
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return state.getValue(PART).age == 7 ? (2 + random.nextInt(3)) : 1;
    }
	
	public void placeAt(World worldIn, BlockPos lowerPos, int age)
    {
        if(age >= 0 && age <= 7) {
        	Part part = Part.BASE_PARTS[age];
        	if(part.hasUpperPart() && !(worldIn.isAirBlock(lowerPos.up()) || worldIn.getBlockState(lowerPos.up()).getBlock() == this))return;
        	worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(PART, part));
        	if(part.hasUpperPart()) {
        		worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(PART, part.getUpperPart()));
        	}
        }
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(state.getValue(PART).multi) {
        	worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(PART, Part.STAGE_5_UPPER), 2);
        }
    }
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (state.getValue(PART).isUpper())
        {
            if (worldIn.getBlockState(pos.down()).getBlock() == this)
            {
                if (player.capabilities.isCreativeMode)
                {
                    worldIn.setBlockToAir(pos.down());
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(pos.down());

                    if (iblockstate.getBlock() == this)
                    {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                    
                }
            }
        }
        else if (worldIn.getBlockState(pos.up()).getBlock() == this)
        {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
		return state.getValue(PART).age < 7;
    }

	@Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return state.getValue(PART).age < 7;
    }

	@Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
		if(state.getValue(PART).isUpper() && worldIn.getBlockState(pos.down()).getBlock() == this) {
			grow(worldIn, rand, pos.down(), worldIn.getBlockState(pos.down()));
		}else if(canGrow(worldIn, pos, state, false)) {
	        int age = state.getValue(PART).age + rand.nextInt(2);
			placeAt(worldIn, pos, Math.min(age, 7));
		}
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(PART).isUpper())
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());
            Part upperPart = iblockstate.getValue(PART).getUpperPart();
            if (upperPart != null && iblockstate.getBlock() == this)
            {
                state = state.withProperty(PART, upperPart);
            }
        }

        return state;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (state.getBlock() ==  this && (state.getValue(PART).multi && !state.getValue(PART).isUpper()) && world.getBlockState(pos.up()).getBlock() == this)
            world.setBlockToAir(pos.up());
        return world.setBlockToAir(pos);
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
		final int age = state.getValue(PART).age;
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
        {
            int i = age;

            if (i < 7)
            {
                float f = BlockCustomCrops.getGrowthChance(this, worldIn, pos) * 0.8f;

                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
                {
                	placeAt(worldIn, pos, i+1);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, new IProperty[] {PART});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PART, Part.values()[MathHelper.clamp(meta, 0, Part.values().length - 1)]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	return state.getValue(PART).ordinal();
    }
	
	public static enum Part implements IStringSerializable{
		STAGE_0(0, false),
		STAGE_1(1, false),
		STAGE_2(2, false),
		STAGE_3(3, false),
		STAGE_4(4, false),
		STAGE_5_LOWER(5, true),
		STAGE_5_UPPER(5, true),
		STAGE_6_LOWER(6, true),
		STAGE_6_UPPER(6, true),
		STAGE_7_LOWER(7, true),
		STAGE_7_UPPER(7, true);
		
		private final static Part BASE_PARTS[] = new Part[8];
		
		static {
			for(Part part : values()) {
				if(!part.multi || part.hasUpperPart()) {
					BASE_PARTS[part.age] = part;
				}
			}
		}
		
		private final int age;
		private final boolean multi;
		private final String serializeableName;
		
		private Part(int age, boolean multi) {
			this.age = age;
			this.multi = multi;
			this.serializeableName = name().toLowerCase();
		}
		
		public boolean isUpper() {
			return multi && !hasUpperPart();
		}
		
		public boolean hasUpperPart() {
			return this == STAGE_5_LOWER || this == STAGE_6_LOWER || this == STAGE_7_LOWER;
		}
		
		@Nullable
		public Part getUpperPart() {
			return (this == STAGE_5_LOWER)?STAGE_5_UPPER:((this == STAGE_6_LOWER)?STAGE_6_UPPER:((this == STAGE_7_LOWER)?STAGE_7_UPPER:null));
		}

		@Override
		public String getName() {
			return serializeableName;
		}
	}
	
	public static class Plant extends WorldGenSurfacePlant.Plant{

		public Plant() {
			super(null, 1);
		}
		
		@Override
		protected void spread(int chunkX, int chunkZ, World world, Random random){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x, y, z);
			MinaBlocks.RUBBER_TREE.placeAt(world, pos, 7);
		}
		
	}

}
