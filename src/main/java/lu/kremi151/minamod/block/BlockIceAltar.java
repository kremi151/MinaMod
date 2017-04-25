package lu.kremi151.minamod.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.entity.EntityIceGolhem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockIceAltar extends BlockCustom{

	public static final PropertyBool WHITE_PEARL = PropertyBool.create("white_pearl");
	public static final PropertyBool BLACK_PEARL = PropertyBool.create("black_pearl");
	public static final PropertyBool CAN_SPAWN_BOSS = PropertyBool.create("can_spawn_boss");

	public BlockIceAltar() {
		super(Material.ICE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(WHITE_PEARL, false).withProperty(BLACK_PEARL, false).withProperty(CAN_SPAWN_BOSS, true));
        this.setTickRandomly(true);
        setBlockUnbreakable();
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random r)
    {
		if(r.nextInt(1) == 0){
			EntityIceGolhem nerd = new EntityIceGolhem(world);
			nerd.posX = (double)(pos.getX() - 8 + r.nextInt(8));
			nerd.posZ = (double)(pos.getZ() - 8 + r.nextInt(8));
			nerd.posY = (double)(pos.getY() + 1);
			world.spawnEntity(nerd);
		}
    }

	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WHITE_PEARL, (meta & 1) == 1)
        		.withProperty(BLACK_PEARL, ((meta >> 1) & 1) == 1)
        		.withProperty(CAN_SPAWN_BOSS, ((meta >> 2) & 1) == 1);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return (((Boolean)state.getValue(CAN_SPAWN_BOSS)?1:0) << 2) | (((Boolean)state.getValue(BLACK_PEARL)?1:0) << 1) | ((Boolean)state.getValue(WHITE_PEARL)?1:0);
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {BLACK_PEARL, WHITE_PEARL, CAN_SPAWN_BOSS});
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if((Boolean) state.getValue(BLACK_PEARL)){
			drops.add(new ItemStack(MinaItems.BLACK_PEARL, 1));
		}
		if((Boolean) state.getValue(WHITE_PEARL)){
			drops.add(new ItemStack(MinaItems.WHITE_PEARL, 1));
		}
		return drops;
    }
	
}
