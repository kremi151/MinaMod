package lu.kremi151.minamod.proxy;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockCampfire;
import lu.kremi151.minamod.block.BlockHerb;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.block.BlockPalmLog;
import lu.kremi151.minamod.block.BlockStandaloneLeaf;
import lu.kremi151.minamod.block.tileentity.TileEntityPlate;
import lu.kremi151.minamod.client.GiftColorHandler;
import lu.kremi151.minamod.client.GuiMinaOverlay;
import lu.kremi151.minamod.client.GuiPlayerStats;
import lu.kremi151.minamod.client.HerbColorHandler;
import lu.kremi151.minamod.client.ItemColorHandler;
import lu.kremi151.minamod.client.LeafColorHandler;
import lu.kremi151.minamod.client.fx.EntityFXSpore;
import lu.kremi151.minamod.client.overlay.OverlayableTexture;
import lu.kremi151.minamod.client.overlay.Overlayable;
import lu.kremi151.minamod.client.overlay.OverlayableString;
import lu.kremi151.minamod.client.render.RenderBee;
import lu.kremi151.minamod.client.render.RenderFish;
import lu.kremi151.minamod.client.render.RenderIceGolhem;
import lu.kremi151.minamod.client.render.RenderIceSentinel;
import lu.kremi151.minamod.client.render.RenderPenguin;
import lu.kremi151.minamod.client.render.RenderTurtle;
import lu.kremi151.minamod.client.render.RenderWookie;
import lu.kremi151.minamod.client.render.tileentity.RenderPlate;
import lu.kremi151.minamod.client.util.ScreenDoge;
import lu.kremi151.minamod.entity.EntityBee;
import lu.kremi151.minamod.entity.EntityFish;
import lu.kremi151.minamod.entity.EntityFrostBall;
import lu.kremi151.minamod.entity.EntityIceGolhem;
import lu.kremi151.minamod.entity.EntityIceSentinel;
import lu.kremi151.minamod.entity.EntityPenguin;
import lu.kremi151.minamod.entity.EntitySoulPearl;
import lu.kremi151.minamod.entity.EntityTurtle;
import lu.kremi151.minamod.entity.EntityWookie;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.packet.message.MessageSetScreenLayer;
import lu.kremi151.minamod.util.ClientEventListeners;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private ArrayList<ItemVariantsData> variantsStack = new ArrayList<ItemVariantsData>();

	private RenderPlate renderPlate;
	// private RenderJoinerTable renderJoinerTable;

	private GuiMinaOverlay overlayHandler;

	public static final KeyBinding KEY_JETPACK = new KeyBinding("gui.key.jetpack.desc", Keyboard.KEY_J, "gui.key.category.minamod");
	public static final KeyBinding KEY_PLAYER_STATS = new KeyBinding("gui.key.player_stats.desc", Keyboard.KEY_M, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULETS = new KeyBinding("gui.key.amulets.desc", Keyboard.KEY_N, "gui.key.category.minamod");

	public static final KeyBinding KEY_AMULET_1 = new KeyBinding("gui.key.amulets.1.desc", Keyboard.KEY_I, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULET_2 = new KeyBinding("gui.key.amulets.2.desc", Keyboard.KEY_O, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULET_3 = new KeyBinding("gui.key.amulets.3.desc", Keyboard.KEY_P, "gui.key.category.minamod");
	
	@Override
	public void registerRenderers() {
		renderPlate = new RenderPlate();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlate.class, renderPlate);

		if(FeatureList.enable_ice_altar){
			RenderingRegistry.registerEntityRenderingHandler(EntityIceSentinel.class, rm -> new RenderIceSentinel(rm));
		}
		RenderingRegistry.registerEntityRenderingHandler(EntityBee.class, rm -> new RenderBee(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityPenguin.class, rm -> new RenderPenguin(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityIceGolhem.class, rm -> new RenderIceGolhem(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, rm -> new RenderTurtle(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityWookie.class, rm -> new RenderWookie(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityFish.class, rm -> new RenderFish(rm));

		RenderingRegistry.registerEntityRenderingHandler(EntityFrostBall.class, rm -> 
			new RenderSnowball<EntityFrostBall>(rm, MinaItems.EVER_SNOW, Minecraft.getMinecraft().getRenderItem())
		);
		RenderingRegistry.registerEntityRenderingHandler(EntitySoulPearl.class, rm ->
			new RenderSnowball<EntitySoulPearl>(rm, MinaItems.SOUL_PEARL, Minecraft.getMinecraft().getRenderItem())
		);

		overlayHandler = new GuiMinaOverlay(Minecraft.getMinecraft());

		MinecraftForge.EVENT_BUS.register(overlayHandler);
	}

	@Override
	public void registerItemAndBlockColors() {
		BlockColors bc = Minecraft.getMinecraft().getBlockColors();
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHERRY);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHESTNUT);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_COTTON);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_PEPPEL);
		bc.registerBlockColorHandler(GiftColorHandler.get(), MinaBlocks.GIFT_BOX);
		bc.registerBlockColorHandler(HerbColorHandler.get(), MinaBlocks.HERB_CROP);

		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHERRY);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHESTNUT);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_COTTON);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_PEPPEL);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.KEY);
		ic.registerItemColorHandler(GiftColorHandler.get(), MinaBlocks.GIFT_BOX);
		
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.HERB);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.POWDER);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.MIXTURE);
		if (FeatureList.enable_soul_pearls)
			ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.SOUL_PEARL);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.COLORED_BOOK);
	}

	@Override
	public void registerBuildInBlocks() {
		ModelManager mm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

		if(FeatureList.enable_plate){
			mm.getBlockModelShapes().registerBuiltInBlocks(MinaBlocks.PLATE);
		}
	}

	@Override
	public void registerStateMappings() {
		// ModelLoader.setCustomStateMapper(MinaBlocks.FILTER, new
		// StateMap.Builder().ignore(BlockFilter.ENABLED).build());

		ModelLoader.setCustomStateMapper(MinaBlocks.PLANKS,
				new StateMap.Builder().withName(BlockMinaPlanks.VARIANT).withSuffix("_planks").build());
		ModelLoader.setCustomStateMapper(MinaBlocks.SAPLING,
				new StateMap.Builder().withName(BlockMinaSapling.TYPE).ignore(BlockMinaSapling.STAGE).withSuffix("_sapling").build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LEAVES_PEPPEL, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LEAVES_CHERRY, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LEAVES_CHESTNUT, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LEAVES_COTTON, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());

		ModelLoader.setCustomStateMapper(MinaBlocks.CAMPFIRE, new StateMap.Builder().ignore(BlockCampfire.IGNITED).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.HERB_CROP, new StateMap.Builder().ignore(BlockHerb.TYPE).build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.ICE_ALTAR,
				new StateMap.Builder().ignore(BlockIceAltar.CAN_SPAWN_BOSS).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.HONEYCOMB,
				new StateMap.Builder().ignore(BlockHoneycomb.HAS_BEES).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LOG_PALM, 
				new StateMap.Builder().ignore(BlockPalmLog.HEAD).build());
	}

	@Override
	public void registerKeyBindings() {
		if(FeatureList.enable_jetpack)ClientRegistry.registerKeyBinding(KEY_JETPACK);
		ClientRegistry.registerKeyBinding(KEY_PLAYER_STATS);
		ClientRegistry.registerKeyBinding(KEY_AMULETS);
		ClientRegistry.registerKeyBinding(KEY_AMULET_1);
		ClientRegistry.registerKeyBinding(KEY_AMULET_2);
		ClientRegistry.registerKeyBinding(KEY_AMULET_3);
	}

	@Override
	public void initClientEvents(){
		MinecraftForge.EVENT_BUS.register(new ClientEventListeners());
	}
	
	@Override
	public void checkInitialPenguinState(EntityPenguin entity){
		if(!entity.getRenderData().checked_water_state){
			entity.getRenderData().was_in_water = entity.isInWater();
			entity.getRenderData().checked_water_state = true;
		}
	}

	@Override
	public void registerOverlays() {
		/*if (MinaMod.getMinaConfig().isDebugging()) {
			this.addOverlay(new OverlayableString(1, "MinaMod version " + MinaMod.VERSION).forever());
			this.addOverlay(new OverlayableString(2, "This mod is still under development").forever());
		}*/
	}

	@Override
	public void addStringOverlay(String txt, long duration) {
		OverlayableString os = new OverlayableString(System.currentTimeMillis(), txt);
		os.setExpiration(System.currentTimeMillis() + 5 + duration);
		addOverlay(os);
	}
	
	@Override
	public void addOverlay(int id, long duration){
		switch(id){
		case 0:
			addOverlay(new OverlayableTexture(System.currentTimeMillis(), GuiPlayerStats.guiTextures, 176, 0, 24, 24).setExpiration(System.currentTimeMillis() + 5 + duration));
			break;
		}
	}

	public void addOverlay(Overlayable o) {
		overlayHandler.addOverlayable(o);
	}

	@Override
	public void clearOverlays() {
		overlayHandler.clearOverlayables();
	}
	
	@Override
	public void setScreenLayer(int id, boolean forced){
		switch(id){
		case MessageSetScreenLayer.DOGE:
			if(forced || !overlayHandler.isLayerActive(ScreenDoge.class)){
				overlayHandler.setLayer(new ScreenDoge(overlayHandler, false));
			}
			break;
		case MessageSetScreenLayer.DOGE_INFINITE:
			if(forced || !overlayHandler.isLayerActive(ScreenDoge.class)){
				overlayHandler.setLayer(new ScreenDoge(overlayHandler, true));
			}
			break;
		default:
			overlayHandler.resetLayer();
			break;
		}
	}

	@Override
	public void executeClientSide(Runnable r) {
		r.run();
	}

	@Override
	public void executeServerSide(Runnable r) {
	}

	@Override
	public boolean isServerSide() {
		return false;
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}

	@Override
	public void registerItem(Item item, String name, String... variantNames) {
		super.registerItem(item, name, variantNames);
		if (variantsStack != null) {
			variantsStack.add(new ItemVariantsData(item, name, MinaUtils.deserializeResourceLocations(MinaMod.MODID, variantNames)));
		}
	}

	private void registerVariantNamesForItem(Item item, String name, ResourceLocation... variantNames) {
		if(item == Items.AIR){
			throw new RuntimeException("Cannot register mesh definition for air item");
		}
		try{
			boolean valid_vn = variantNames != null && variantNames.length > 0;
			if (valid_vn) {				
				if (variantNames.length == 1) {
					ModelLoader.setCustomMeshDefinition(item, new StaticMeshDefinition(new ModelResourceLocation(variantNames[0], "inventory")));
				} else {
					ModelBakery.registerItemVariants(item, variantNames);
					for (int i = 0; i < variantNames.length; i++) {
						ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(variantNames[i], "inventory"));
					}
				}
				
			} else {
				ModelLoader.setCustomMeshDefinition(item, new StaticMeshDefinition(new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, name), "inventory")));
			}
			
		}catch(RuntimeException e){
			MinaMod.errorln("Error registering variant names for item \"" + name + "\" [" + item.getClass() + "]");
			e.printStackTrace();
			throw(e);
		}
	}

	@Override
	public void registerBlock(Block block, String name, String... variantNames) {
		super.registerBlock(block, name, variantNames);
		if (variantsStack != null) {
			variantsStack.add(new ItemVariantsData(Item.getItemFromBlock(block), name, MinaUtils.deserializeResourceLocations(MinaMod.MODID, variantNames)));
		}
	}

	@Override
	public void registerVariantNames() {
		if (variantsStack != null) {
			Iterator<ItemVariantsData> it = variantsStack.iterator();
			while (it.hasNext()) {
				ItemVariantsData bv = it.next();
				registerVariantNamesForItem(bv.item, bv.name, bv.variantNames);
				it.remove();
			}
		}
		variantsStack = null;
	}
	
	@Override
	public void registerFluidModels() {
		
	}
	
	private void registerFluidModel(IFluidBlock fluidBlock) {
		/*ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(MinaMod.MODID, "fluid"), fluidBlock.getFluid().getName());
		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));
		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});*/
	}
	
	private final static double AMULET_AURA_ANGLE = Math.PI / 8.0;

	@Override
	public void spawnParticleEffect(EnumParticleEffect effect, World world, double posX, double posY, double posZ, float red, float green, float blue) {
		switch(effect){
		case SPORE:
			double motionX = world.rand.nextGaussian() * 0.02D;
			double motionY = world.rand.nextGaussian() * 0.02D;
			double motionZ = world.rand.nextGaussian() * 0.02D;
			Particle particleMysterious = new EntityFXSpore(world, posX, posY, posZ, motionX, motionY, motionZ, red,
					green, blue);
			Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
			break;
		case AMULET_AURA:
			for(double i = 0 ; i < 16.0 ; i++){
				double angle = i * AMULET_AURA_ANGLE;
				double x = posX + (0.5 * Math.cos(angle));
				double z = posZ + (0.5 * Math.sin(angle));
				double mx = 0.05 * Math.cos(angle);
				double my = 0.05 * Math.sin(angle);
				world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x, posY, z, mx, -0.03, my, null);
			}
			break;
		case SMOKE:
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (Math.random() * 0.5d) - 0.25d, posY + (Math.random() * 0.5d) - 0.25d, posZ + (Math.random() * 0.5d) - 0.25d, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		}
	}

	@Override
	public void openBook(ItemStack book) {
		boolean signed = book.hasTagCompound() && book.getTagCompound().hasKey("author", 8);
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(Minecraft.getMinecraft().player, book, !signed));
	}

	@SideOnly(Side.CLIENT)
	private class ItemVariantsData {

		final Item item;
		final String name;
		final ResourceLocation[] variantNames;

		private ItemVariantsData(Item item, String name, ResourceLocation... variantNames) {
			this.item = item;
			this.name = name;
			this.variantNames = variantNames;
		}
	}

	@SideOnly(Side.CLIENT)
	private class StaticMeshDefinition implements ItemMeshDefinition {

		private ModelResourceLocation m;

		private StaticMeshDefinition(ModelResourceLocation m) {
			this.m = m;
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return m;
		}

	}

}
