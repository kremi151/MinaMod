package lu.kremi151.minamod.proxy;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.BlockCampfire;
import lu.kremi151.minamod.block.BlockCoconut;
import lu.kremi151.minamod.block.BlockDimmableLight;
import lu.kremi151.minamod.block.BlockEnergyToRedstone;
import lu.kremi151.minamod.block.BlockFilter;
import lu.kremi151.minamod.block.BlockHerb;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.block.BlockLock;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.block.BlockMinaWoodSlab;
import lu.kremi151.minamod.block.BlockPalmLog;
import lu.kremi151.minamod.block.BlockStandaloneLeaf;
import lu.kremi151.minamod.client.GiftColorHandler;
import lu.kremi151.minamod.client.GuiMinaOverlay;
import lu.kremi151.minamod.client.HerbColorHandler;
import lu.kremi151.minamod.client.ItemColorHandler;
import lu.kremi151.minamod.client.LeafColorHandler;
import lu.kremi151.minamod.client.WallCableColorHandler;
import lu.kremi151.minamod.client.blockmodel.MinaModelLoader;
import lu.kremi151.minamod.client.fx.EntityFXSpore;
import lu.kremi151.minamod.client.render.RenderBee;
import lu.kremi151.minamod.client.render.RenderFish;
import lu.kremi151.minamod.client.render.RenderIceGolhem;
import lu.kremi151.minamod.client.render.RenderIceSentinel;
import lu.kremi151.minamod.client.render.RenderPenguin;
import lu.kremi151.minamod.client.render.RenderTurtle;
import lu.kremi151.minamod.client.render.RenderWookie;
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
import lu.kremi151.minamod.network.MessageAddScreenLayer;
import lu.kremi151.minamod.util.ClientEventListeners;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private GuiMinaOverlay overlayHandler;

	public static final KeyBinding KEY_JETPACK = new KeyBinding("gui.key.jetpack.desc", Keyboard.KEY_J, "gui.key.category.minamod");
	public static final KeyBinding KEY_PLAYER_STATS = new KeyBinding("gui.key.player_stats.desc", Keyboard.KEY_M, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULETS = new KeyBinding("gui.key.amulets.desc", Keyboard.KEY_N, "gui.key.category.minamod");

	public static final KeyBinding KEY_AMULET_1 = new KeyBinding("gui.key.amulets.1.desc", Keyboard.KEY_I, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULET_2 = new KeyBinding("gui.key.amulets.2.desc", Keyboard.KEY_O, "gui.key.category.minamod");
	public static final KeyBinding KEY_AMULET_3 = new KeyBinding("gui.key.amulets.3.desc", Keyboard.KEY_P, "gui.key.category.minamod");
	
	@Override
	public IThreadListener getThreadListener(MessageContext context) {
		if(context.side.isClient()) {
			return Minecraft.getMinecraft();
		}else {
			return super.getThreadListener(context);
		}
	}
	
	@Override
	public void registerRenderers() {
		ModelLoaderRegistry.registerLoader(new MinaModelLoader());

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
		/*bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHERRY);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHESTNUT);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_COTTON);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_PEPPEL);*/
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.MINA_LEAVES_A);
		bc.registerBlockColorHandler(LeafColorHandler.get(), MinaBlocks.PALM_LEAVES);
		bc.registerBlockColorHandler(GiftColorHandler.get(), MinaBlocks.GIFT_BOX);
		bc.registerBlockColorHandler(HerbColorHandler.get(), MinaBlocks.HERB_CROP);
		bc.registerBlockColorHandler(new WallCableColorHandler(), MinaBlocks.WALL_CABLE);

		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		/*ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHERRY);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_CHESTNUT);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_COTTON);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.LEAVES_PEPPEL);*/
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.MINA_LEAVES_A);
		ic.registerItemColorHandler(LeafColorHandler.get(), MinaBlocks.PALM_LEAVES);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.KEY);
		ic.registerItemColorHandler(GiftColorHandler.get(), MinaBlocks.GIFT_BOX);
		
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.HERB);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.POWDER);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.MIXTURE);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.SOUL_PEARL);
		ic.registerItemColorHandler(ItemColorHandler.get(), MinaItems.COLORED_BOOK);
	}

	@Override
	public void registerBuildInBlocks() {
		ModelManager mm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();

		//mm.getBlockModelShapes().registerBuiltInBlocks(MinaBlocks.PLATE);
	}

	@Override
	public void registerStateMappings() {
		ModelLoader.setCustomStateMapper(MinaBlocks.PLANKS,
				new StateMap.Builder().withName(BlockMinaPlanks.VARIANT).withSuffix("_planks").build());
		ModelLoader.setCustomStateMapper(MinaBlocks.SAPLING,
				new StateMap.Builder().withName(BlockMinaSapling.TYPE).ignore(BlockMinaSapling.STAGE).withSuffix("_sapling").build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.MINA_LEAVES_A, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		//ModelLoader.setCustomStateMapper(MinaBlocks.MINA_LEAVES_B, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.PALM_LEAVES, new StateMap.Builder().ignore(BlockStandaloneLeaf.CHECK_DECAY, BlockStandaloneLeaf.DECAYABLE).build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.CAMPFIRE, new StateMap.Builder().ignore(BlockCampfire.IGNITED).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.HERB_CROP, new StateMap.Builder().ignore(BlockHerb.TYPE).build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.ICE_ALTAR,
				new StateMap.Builder().ignore(BlockIceAltar.CAN_SPAWN_BOSS).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.HONEYCOMB,
				new StateMap.Builder().ignore(BlockHoneycomb.HAS_BEES).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.LOG_PALM, 
				new StateMap.Builder().ignore(BlockPalmLog.HEAD).build());

		ModelLoader.setCustomStateMapper(MinaBlocks.WOODEN_SLAB,
				new StateMap.Builder().withName(BlockMinaWoodSlab.VARIANT).withSuffix("_slab").build());
		ModelLoader.setCustomStateMapper(MinaBlocks.DOUBLE_WOODEN_SLAB,
				new StateMap.Builder().withName(BlockMinaWoodSlab.VARIANT).withSuffix("_double_slab").build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.COCONUT, 
				new StateMap.Builder().ignore(BlockCoconut.AGE).build());
		
		ModelLoader.setCustomStateMapper(MinaBlocks.DIMMABLE_LIGHT, new StateMap.Builder().ignore(BlockDimmableLight.LIGHT).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.KEY_LOCK, new StateMap.Builder().ignore(BlockLock.POWERED).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.FILTER, new StateMap.Builder().ignore(BlockFilter.ENABLED).build());
		ModelLoader.setCustomStateMapper(MinaBlocks.ENERGY_TO_REDSTONE, new StateMap.Builder().ignore(BlockEnergyToRedstone.OUTPUT).build());
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
	public <T> Optional<T> tryGetClientSideResult(Supplier<T> clientCode){
		return Optional.of(clientCode.get());
	}
	
	@Override
	public void addScreenLayer(int id, boolean forced){
		switch(id){
		case MessageAddScreenLayer.DOGE:
			if(forced || !overlayHandler.isLayerActive(ScreenDoge.class)){
				overlayHandler.addLayer(new ScreenDoge(overlayHandler, false));
			}
			break;
		case MessageAddScreenLayer.DOGE_INFINITE:
			if(forced || !overlayHandler.isLayerActive(ScreenDoge.class)){
				overlayHandler.addLayer(new ScreenDoge(overlayHandler, true));
			}
			break;
		default:
			//overlayHandler.resetLayer();
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
		case COLLECT:
			int dis_n = 3 + MathHelper.floor(Math.random() * 3);
			while(dis_n-- > 0)world.spawnParticle(EnumParticleTypes.REDSTONE, posX + (Math.random() * 0.5d) - 0.25d, posY + (Math.random() * 0.50d) - 0.25d, posZ + (Math.random() * 0.5d) - 0.25d, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		}
	}

	@Override
	public void openBook(ItemStack book) {
		boolean signed = book.hasTagCompound() && book.getTagCompound().hasKey("author", 8);
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(Minecraft.getMinecraft().player, book, !signed));
	}
	
	@Override
	public void showNotification(@Nullable EntityPlayer player, String title, String message, long duration, ItemStack icon) {
		//TODO: Replace with advancement like notification
		TextHelper.sendChatMessage(Minecraft.getMinecraft().player, "[" + title + "] " + message);
	}
	
}
