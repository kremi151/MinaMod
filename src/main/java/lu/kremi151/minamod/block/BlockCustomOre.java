package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCustomOre extends Block{

    public BlockCustomOre()
    {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	if(this == MinaBlocks.CITRIN_ORE){
    		return MinaItems.CITRIN;
    	}else if(this == MinaBlocks.SAPPHIRE_ORE){
    		return MinaItems.SAPPHIRE;
    	}else if(this == MinaBlocks.RUBY_ORE){
    		return MinaItems.RUBY;
    	}else if(this == MinaBlocks.RARE_SOIL_ORE) {
    		return MinaItems.RARE_SOIL;
    	}else{
    		return Item.getItemFromBlock(this);
    	}
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        if(this == MinaBlocks.RARE_SOIL_ORE) {
        	return MinaItems.RARE_SOIL.getRandomType(RANDOM).getMeta();
        }else {
        	return super.damageDropped(state);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random p_149745_1_)
    {
//        return this == Blocks.lapis_ore ? 4 + p_149745_1_.nextInt(5) : 1;
    	return 1;
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    @Override
	public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(null, random, fortune))
        {
            int j = random.nextInt(fortune + 2) - 1;

            if (j < 0)
            {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    private Random rand = new Random();
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        if (this.getItemDropped(world.getBlockState(pos), rand, fortune) != Item.getItemFromBlock(this))
        {
            int j1 = 0;

            
            if (this == MinaBlocks.CITRIN_ORE)
            {
                j1 = MathHelper.getInt(rand, 3, 7);
            }
//            else if (this == MinaBlocks.blockMountainCrystalOre)
//            {
//                j1 = MathHelper.getRandomIntegerInRange(rand, 2, 4);
//            }

            return j1;
        }
        return 0;
    }

	
}
