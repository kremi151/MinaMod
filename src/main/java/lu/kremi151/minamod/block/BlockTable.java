package lu.kremi151.minamod.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.TriConsumer;
import lu.kremi151.minamod.item.block.ItemBlockMulti;
import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import lu.kremi151.minamod.util.registration.ItemRegistrationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTable extends Block{
	
	private static final String[] FORM_NAMES = {"normal", "classic" };
	
	public static final PropertyEnum<TableForm> FORM = PropertyEnum.<TableForm>create("form", TableForm.class);
	
	private static final List<BlockTable> tables = new ArrayList<BlockTable>();
	
	private TableType type;

	public BlockTable(TableType type) {
		super(type.getMaterial());
        this.setDefaultState(this.blockState.getBaseState().withProperty(FORM, TableForm.NORMAL));
		this.setHardness(2F);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.type = type;
		tables.add(this);
	}
	
	@Override
    public int damageDropped(IBlockState state)
    {
        return ((TableForm)state.getValue(FORM)).getMetadata();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (TableForm tform : TableForm.values())
        {
            list.add(new ItemStack(itemIn, 1, tform.getMetadata()));
        }
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FORM, TableForm.getByMeta(meta));
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
    {
        return ((TableForm)state.getValue(FORM)).getMetadata();
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FORM});
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side == EnumFacing.UP;
	}
	
	public TableType getType(){
		return type;
	}
	
	public static Iterator<BlockTable> getTableBlocks(){
		return tables.iterator();
	}
	
	private static void prepareRegistering(TriConsumer<BlockTable, String, String[]> consumer) {
		for(int i = 0 ; i < tables.size() ; i++){
			BlockTable t = tables.get(i);
			String tname = t.getUnlocalizedName().substring(5);
			String[] variantNames = new String[FORM_NAMES.length];
			for(int j = 0 ; j < FORM_NAMES.length; j++){
				if(j == 0){
					variantNames[j] = tname;
				}else{
					variantNames[j] = tname + "_" + FORM_NAMES[j];
				}
			}
			consumer.accept(t, tname, variantNames);
			t.setCreativeTab(MinaCreativeTabs.FURNISHING);
		}
	}
	
	public static void registerTableBlocks(IRegistrationInterface<Block, BlockRegistrationHandler> registry){
		prepareRegistering((t, tname, variantNames) -> registry.register(t, tname).blockOnly().submit());
	}
	
	public static void registerTableItems(IRegistrationInterface<Item, ItemRegistrationHandler> registry){
		prepareRegistering((t, tname, variantNames) -> registry.register(new ItemBlockMulti<BlockTable>(t, FORM_NAMES).setRegistryName(t.getRegistryName()), tname).variantNames(variantNames).submit());
	}
	
	public static void registerFireInfos(){
		for(int i = 0 ; i < tables.size() ; i++){
			BlockTable bt = tables.get(i);
	        if(bt.type.isFlammable())Blocks.FIRE.setFireInfo(bt, 5, 20);
		}
	}
	
	public static enum TableForm implements IStringSerializable{
		
		NORMAL(0, "normal"),
		CLASSIC(1, "classic");
		
		private String name;
		private int meta;
		
		private TableForm(int meta, String name){
			this.meta = meta;
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
		
	    public int getMetadata()
	    {
	        return this.meta;
	    }
	    
	    public static TableForm getByMeta(int meta){
	    	for(int i = 0 ; i < values().length ; i++){
	    		if(values()[i].getMetadata() == meta)return values()[i];
	    	}
	    	return null;
	    }
	}
	
	public static enum TableType{
		Wood(Material.WOOD, 2, 1, 1, true),
		Stone(Material.ROCK, 3, 2, 3, false),
		Rare(Material.IRON, 5, 4, 2, false);
		
		private Material mat;
		private int buy_base;
		private int sell_base;
		private int price_variation;
		private boolean hasCraftingRecipe;
		
		TableType(Material mat, int buy_base, int sell_base, int price_variation, boolean hasCraftingRecipe){
			this.mat = mat;
			this.buy_base = buy_base;
			this.sell_base = sell_base;
			this.price_variation = price_variation;
			this.hasCraftingRecipe = hasCraftingRecipe;
		}
		
		public Material getMaterial(){
			return mat;
		}
		
		public int getBaseBuyPrice(){
			return buy_base;
		}
		
		public int getBaseSellPrice(){
			return sell_base;
		}
		
		public int getPriceVariation(){
			return price_variation;
		}
		
		public boolean hasCraftingRecipe(){ // NO_UCD (unused code)
			return hasCraftingRecipe;
		}
		
		public boolean isFlammable(){
			return mat.getCanBurn();
		}
	}

}
