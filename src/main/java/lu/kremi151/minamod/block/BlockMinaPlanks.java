package lu.kremi151.minamod.block;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMinaPlanks extends BlockCustom
{
    public static final PropertyEnum<BlockMinaPlanks.EnumType> VARIANT = PropertyEnum.<BlockMinaPlanks.EnumType>create("variant", BlockMinaPlanks.EnumType.class);

    public BlockMinaPlanks()
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockMinaPlanks.EnumType.PEPPEL));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (BlockMinaPlanks.EnumType type : BlockMinaPlanks.EnumType.values())
        {
            list.add(new ItemStack(itemIn, 1, type.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockMinaPlanks.EnumType.byMetadata(meta));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state)
    {
        return ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMapColor();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    public static enum EnumType implements IStringSerializable
    {
        PEPPEL(0, "peppel", MapColor.GRAY, MinaUtils.convertRGBToDecimal(14, 115, 29)),
        COTTON(1, "cotton", MapColor.SILVER, MinaUtils.convertRGBToDecimal(23, 71, 30)),
        CHESTNUT(2, "chestnut", MapColor.BROWN, MinaUtils.convertRGBToDecimal(72, 171, 87)),
        CHERRY(3, "cherry", MapColor.PINK, MinaUtils.convertRGBToDecimal(152, 199, 50));

    	public static final String[] subVariantNames, variantNamesPlanks;
        private static final BlockMinaPlanks.EnumType[] META_LOOKUP = new BlockMinaPlanks.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        /** The color that represents this entry on a map. */
        private final MapColor mapColor;
        private final int leafColor;

        private EnumType(int metaIn, String nameIn, MapColor mapColorIn, int leafColor)
        {
            this(metaIn, nameIn, nameIn, mapColorIn, leafColor);
        }

        private EnumType(int metaIn, String nameIn, String unlocalizedNameIn, MapColor mapColorIn, int leafColor)
        {
            this.meta = metaIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.mapColor = mapColorIn;
            this.leafColor = leafColor;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        /**
         * The color which represents this entry on a map.
         */
        public MapColor getMapColor()
        {
            return this.mapColor;
        }
        
        public int getLeafColor(){
        	return this.leafColor;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        public static BlockMinaPlanks.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
        	subVariantNames = new String[values().length];
        	variantNamesPlanks = new String[values().length];
            for (BlockMinaPlanks.EnumType BlockMinaPlanks$enumtype : values())
            {
                META_LOOKUP[BlockMinaPlanks$enumtype.getMetadata()] = BlockMinaPlanks$enumtype;
            }
            for(int i = 0 ; i < values().length ; i++){
            	subVariantNames[i] = values()[i].name;
            	variantNamesPlanks[i] = values()[i].name + "_planks";
            }
        }
    }

}
