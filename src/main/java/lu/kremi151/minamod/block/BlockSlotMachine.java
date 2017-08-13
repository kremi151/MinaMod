package lu.kremi151.minamod.block;

import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.util.nbtmath.SerializableConstant;
import lu.kremi151.minamod.util.slotmachine.SlotMachineBuilder;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;

public class BlockSlotMachine extends BlockCustomHorizontal{
	
	public static final PropertyBool IS_TURNING = PropertyBool.create("is_turning");
	
	private ItemStack variantShinyGemCrush = null, variantSweetLuxury = null, variantFree = null;
	
	protected static final AxisAlignedBB aabb = new AxisAlignedBB(2.0 / 16.0, 0d, 2.0 / 16.0, 14.0 / 16.0, 12.0 / 16.0, 14.0 / 16.0);

	public BlockSlotMachine() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(IS_TURNING, ((TileEntitySlotMachine)worldIn.getTileEntity(pos)).isTurning());
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, IS_TURNING});
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
		list.add(new ItemStack(itemIn, 1));
        
        if(variantShinyGemCrush == null) {        	
        	variantShinyGemCrush = new SlotMachineBuilder()
        			.addIcon(MinaItems.RUBY, 3, false)
        			.addIcon(MinaItems.SAPPHIRE, 3, false)
        			.addIcon(Items.EMERALD, 2, false)
        			.addIcon(Items.NETHER_STAR, 1, false)
        			.setPriceForSpinMode(SpinMode.ONE, 1)
        			.setPriceForSpinMode(SpinMode.THREE, 2)
        			.setPriceForSpinMode(SpinMode.FIVE, 3)
        			.setCustomName(TextFormatting.BLUE + "Shiny" + TextFormatting.RED + "Gem" + TextFormatting.GREEN + "Crush")
        			.buildItemStack();
        }
        
        if(variantSweetLuxury == null) {        	
        	variantSweetLuxury = new SlotMachineBuilder()
        			.addIcon(MinaBlocks.CHOCOLATE_CAKE, 4, false)
        			.addIcon(MinaBlocks.STRAWBERRY_CAKE, 4, false)
        			.addIcon(MinaBlocks.HONEY_CAKE, 3, false)
        			.addIcon(MinaBlocks.CREEPER_CAKE, 2, false)
        			.addIcon(MinaItems.CANDY_CANE, 1, true)
        			.addIcon(Items.APPLE, 1, true)
        			.addIcon(Items.MELON, 1, true)
        			.addIcon(Items.GOLDEN_APPLE, 1, false)
        			.setPriceForSpinMode(SpinMode.ONE, 5)
        			.setPriceForSpinMode(SpinMode.THREE, 10)
        			.setPriceForSpinMode(SpinMode.FIVE, 15)
        			.setMaxWin(400.0)
        			.setCherryWin(300.0)
        			.setCustomName(TextFormatting.DARK_AQUA + "Sweet Luxury")
        			.buildItemStack();
        }
        
        if(variantFree == null) {        	
        	variantFree = new SlotMachineBuilder()
        			.addIcon(Items.RECORD_11, 4, false)
        			.addIcon(Items.RECORD_CHIRP, 3, false)
        			.addIcon(Items.RECORD_BLOCKS, 3, false)
        			.addIcon(Items.RECORD_13, 1, false)
        			.addIcon(Blocks.JUKEBOX, 1, true)
        			.setPriceForSpinMode(SpinMode.ONE, 0)
        			.setPriceForSpinMode(SpinMode.THREE, 0)
        			.setPriceForSpinMode(SpinMode.FIVE, 0)
        			.setRowPriceFunction(new SerializableConstant(0.0))
        			.setCustomName("Free Spins")
        			.buildItemStack();
        }
        
        list.add(variantShinyGemCrush.copy());     
        list.add(variantSweetLuxury.copy());   
        list.add(variantFree.copy());
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new TileEntitySlotMachine();
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote && player.hasCapability(ICoinHandler.CAPABILITY, null)){
			if(PermissionAPI.hasPermission(player, MinaPermissions.ALLOW_GAMBLING)) {
				TileEntitySlotMachine te = (TileEntitySlotMachine) world.getTileEntity(pos);
				if(te.setCurrentPlayer(player).isPresent()) {
					player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdSlotMachine, world, pos.getX(), pos.getY(), pos.getZ());
				}else {
					TextHelper.sendTranslateableErrorMessage(player, "gui.slot_machine.msg.inuse");
				}
			}else {
				TextHelper.sendTranslateableErrorMessage(player, "msg.gambling.not_allowed");
			}
		}
		return true;
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if(!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te != null && te instanceof TileEntitySlotMachine) {
				TileEntitySlotMachine sm = (TileEntitySlotMachine) te;
				
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
	            sm.writeSlotMachineToNBT(stack.getOrCreateSubCompound("BlockEntityTag"));
	            
	            spawnAsEntity(worldIn, pos, stack);
			}
		}
		super.breakBlock(worldIn, pos, state);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		NBTTagCompound nbt = stack.getSubCompound("BlockEntityTag");
		if(nbt != null) {
			if(nbt.hasKey("CustomName", 8)) {
				tooltip.add(nbt.getString("CustomName"));
			}
		}else {
			tooltip.add(I18n.translateToLocal("gui.slot_machine.info.original"));
		}
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return aabb;
	}

	@Override
	public boolean isOpaqueCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }
}
