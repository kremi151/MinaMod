package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.capabilities.MinaCapabilities;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.SlotMachineBuilder;
import lu.kremi151.minamod.util.TextHelper;
import lu.kremi151.minamod.util.nbtmath.NBTMathHelper;
import lu.kremi151.minamod.util.nbtmath.SerializableBinaryOperation;
import lu.kremi151.minamod.util.nbtmath.SerializableConstant;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedMapper;
import lu.kremi151.minamod.util.slotmachine.SpinMode;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;

public class BlockSlotMachine extends BlockCustomHorizontal{
	
	private ItemStack variantShinyGemCrush = null, variantSweetLuxury = null;
	
	protected static final AxisAlignedBB aabb = new AxisAlignedBB(2.0 / 16.0, 0d, 2.0 / 16.0, 14.0 / 16.0, 12.0 / 16.0, 14.0 / 16.0);

	public BlockSlotMachine() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
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
        			.setMaxWin(300.0)
        			.setCustomName(TextFormatting.DARK_AQUA + "Sweet Luxury")
        			.buildItemStack();
        }
        
        list.add(variantShinyGemCrush.copy());     
        list.add(variantSweetLuxury.copy());
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
					TextHelper.sendChatMessage(player, "This slot machine is currently in use");
				}
			}else {
				TextHelper.sendTranslateableErrorMessage(player, "msg.gambling.not_allowed");
			}
		}
		return true;
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
