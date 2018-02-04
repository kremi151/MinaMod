package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.block.tileentity.TileEntityHerbCrop;
import lu.kremi151.minamod.enums.EnumHerb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHerb extends Item implements net.minecraftforge.common.IPlantable{

	public ItemHerb() {
		super();
		this.setHasSubtypes(true);
		this.setCreativeTab(MinaCreativeTabs.MIXTURES);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item.herb." + EnumHerb.getByHerbId((byte)stack.getMetadata(), EnumHerb.GRAY).getName();
    }
	
	@Override
	public int getMetadata(ItemStack stack)
    {
        return stack.getItemDamage() != OreDictionary.WILDCARD_VALUE ? MathHelper.clamp(stack.getItemDamage(), 0, EnumHerb.values().length - 1) : OreDictionary.WILDCARD_VALUE;
    }
	
	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
		if(isInCreativeTab(tab)) {
			EnumHerb[] eh = EnumHerb.values();
			for(int i = 0 ; i < eh.length ; i++){
		        subItems.add(new ItemStack(this, 1, eh[i].getHerbId()));
			}
		}
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState state = worldIn.getBlockState(pos);
        ItemStack stack = playerIn.getHeldItem(hand);
    	EnumHerb eh = getType(stack);
        if (eh.isPlantable() && facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, stack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
        {
        	BlockPos npos = pos.up();
            worldIn.setBlockState(npos, MinaBlocks.HERB_CROP.getDefaultState());
            TileEntityHerbCrop hc = new TileEntityHerbCrop();
            hc.setType(eh);
            hc.setMutability(getMutability(stack));
            worldIn.setTileEntity(npos, hc);
			worldIn.notifyBlockUpdate(pos, state, state, 3);
            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        return net.minecraftforge.common.EnumPlantType.Crop;
    }

    @Override
    public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        return MinaBlocks.HERB_CROP.getDefaultState();
    }
    
    public float getMutability(ItemStack stack){
    	/*NBTTagCompound nbt = stack.getOrCreateSubCompound("Herb");
    	if(nbt.hasKey("Mutability", 99)){
    		return nbt.getFloat("Mutability");
    	}else{
    		EnumHerb eh = EnumHerb.getByHerbId((byte)stack.getMetadata());
    		nbt.setFloat("Mutability", eh.getDefaultMutability());
    		return eh.getDefaultMutability();
    	}*/
    	return EnumHerb.getByHerbId((byte)stack.getMetadata(), EnumHerb.GRAY).getDefaultMutability();//TODO
    }
    
    public void setMutability(ItemStack stack, float v){
    	NBTTagCompound nbt = stack.getOrCreateSubCompound("Herb");
    	nbt.setFloat("Mutability", v);
    }
    
    public EnumHerb getType(ItemStack stack){
    	return EnumHerb.getByHerbId((byte)stack.getMetadata(), EnumHerb.GRAY);
    }
    

}
