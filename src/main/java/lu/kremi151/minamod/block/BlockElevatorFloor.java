package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockElevatorFloor extends Block{
	
	public static final PropertyBool JUMPER = PropertyBool.create("jumper");

	public BlockElevatorFloor() {
		super(Material.IRON);
		setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setDefaultState(this.blockState.getBaseState().withProperty(JUMPER, false));
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	list.add(new ItemStack(itemIn, 1, 0));
    	list.add(new ItemStack(itemIn, 1, 1));
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(JUMPER, (meta & 1) > 0);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(JUMPER) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {JUMPER});
    }
	
	
	public void jumpElevate(EntityLivingBase entity, boolean up){
		BlockPos origin = up ? entity.getPosition() : entity.getPosition().down(2);
		for(int y = origin.getY() ; up?(y <= 255):(y >= 0) ;){
			BlockPos pos = new BlockPos(origin.getX(), y, origin.getZ());
			IBlockState state = entity.world.getBlockState(pos);
			if(state.getBlock() == this && state.getValue(JUMPER)){
				entity.setPositionAndUpdate(entity.posX, pos.getY() + 1.0, entity.posZ);
				entity.setJumping(false);
				break;
			}
			if(up)y++; else y--;
		}
	}
	
	public boolean checkCooldown(EntityLivingBase entity){
		return (entity instanceof EntityPlayer) ? (System.currentTimeMillis() - MinaMod.getProxy().getCooldownDate("ejump", entity.getUniqueID()) >= 0) : true;
	}
	
	public void setCooldown(EntityLivingBase entity){
		if(entity instanceof EntityPlayer)MinaMod.getProxy().setCooldownDate("ejump", entity.getUniqueID(), System.currentTimeMillis() + 500);
	}
	
	public static boolean isValidLevelFloor(IBlockState state){
		return state.getBlock() == MinaBlocks.ELEVATOR_FLOOR && !state.getValue(JUMPER);
	}
	
	public static boolean isValidJumpFloor(IBlockState state){
		return state.getBlock() == MinaBlocks.ELEVATOR_FLOOR && state.getValue(JUMPER);
	}

}
