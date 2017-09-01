package lu.kremi151.minamod.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.BiMap;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.entity.fish.FishType;
import lu.kremi151.minamod.entity.fish.FishTypes;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

@Mod.EventBusSubscriber(modid = MinaMod.MODID)
public class EntityFish extends EntityAnimal {

	public static final String entityId = "living_fish";
	public static ResourceLocation ID = new ResourceLocation(MinaMod.MODID, entityId);

	private static IForgeRegistry<FishType> REGISTRY;
	public static final ResourceLocation DEFAULT_FISH_TYPE = new ResourceLocation(MinaMod.MODID, "hikari");

	private static final float MAX_WIDTH = 18f / 16f;
	private static final float MAX_HEIGHT = 5f / 16f;

	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	private float randomMotionSpeed = 0.2f;

	private FishType fishType;
	private final LinkedList<EntityAIBase> customAI = new LinkedList<EntityAIBase>();

	private static final DataParameter<Float> SIZE = EntityDataManager.<Float>createKey(EntityFish.class, DataSerializers.FLOAT);
	private static final DataParameter<String> VARIANT = EntityDataManager.<String>createKey(EntityFish.class, DataSerializers.STRING);

	public EntityFish(World worldIn) {
		super(worldIn);
		this.setSize(MAX_WIDTH, MAX_HEIGHT);
	}
	
	private static FishType getRandomType(Random random){
		List<FishType> types = REGISTRY.getValues();
		return types.get(random.nextInt(types.size()));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		fishType = REGISTRY.getValue(DEFAULT_FISH_TYPE);
		this.getDataManager().register(VARIANT, fishType.getRegistryName().toString());
		onFishTypeChanged();

		this.getDataManager().register(SIZE, Float.valueOf(
				fishType.getMinSize() + (getRNG().nextFloat() * (fishType.getMaxSize() - fishType.getMinSize()))));
		setScaleForAge(isChild());
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		setFishType(getRandomType(rand));
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(VARIANT)) {
			fishType = REGISTRY.getValue(new ResourceLocation(getDataManager().get(VARIANT)));
			if(fishType == null){
				fishType = REGISTRY.getValue(DEFAULT_FISH_TYPE);
			}

			float size = getSizeF();
			if (isChild() && size != fishType.getChildSize()) {
				setSizeF(fishType.getChildSize());
			} else if (size > fishType.getMaxSize()) {
				setSizeF(fishType.getMaxSize());
			} else if (size < fishType.getMinSize()) {
				setSizeF(fishType.getMinSize());
			}

			onFishTypeChanged();
		}
		super.notifyDataManagerChange(key);
	}

	public float getScale() {
		return isChild() ? fishType.getChildSize() : getSizeF();
	}

	public float getSizeF() {
		return getDataManager().get(SIZE);
	}

	public FishType getFishType() {
		return fishType;
	}

	public void setFishType(FishType type) {
		this.fishType = type;
		getDataManager().set(VARIANT, type.getRegistryName().toString());
	}

	private void onFishTypeChanged() {
		if (customAI != null) {
			for (EntityAIBase ai : customAI) {
				this.tasks.removeTask(ai);
			}
			customAI.clear();
			fishType.addCustomAI(customAI);
			final int length = customAI.size();
			for (int i = 0; i < length; i++) {
				this.tasks.addTask(i + 5, customAI.get(i));
			}
		}
	}

	public void setSizeF(float size) {
		size = Math.min(fishType.getMaxSize(), Math.max(fishType.getMinSize(), size));
		getDataManager().set(SIZE, size);
		setScale(size);
	}

	@Override
	public boolean canDespawn() {
		return fishType.canDespawn();
	}

	@Override
	public void setScaleForAge(boolean child) {
		this.setScale(child ? fishType.getChildSize() : getSizeF());
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityFish(ageable.world);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityFish.AIMoveRandom(this));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setFloat("Size", this.getDataManager().get(SIZE));
		nbt.setString("Type", fishType.getRegistryName().toString());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("Size", 5)) {
			setSizeF(nbt.getFloat("Size"));
		}
		if (nbt.hasKey("Type", 8)) {
			FishType type = REGISTRY.getValue(new ResourceLocation(nbt.getString("Type")));
			if (type != null) {
				setFishType(type);
			}
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		MinaMod.getLogger().warn("MinaMod Fish died, cause: " + cause + ", size: " + this.getSizeF());
	}

	@Override
	public void onEntityUpdate() {
		int i = this.getAir();
		super.onEntityUpdate();

		if (this.isEntityAlive() && !this.fishType.canStayOutOfWater(this) && !this.isInWater()) {
			--i;
			this.setAir(i);

			if (this.getAir() == -20) {
				this.setAir(0);
				this.attackEntityFrom(DamageSource.DROWN, 2.0F);
			}
		} else {
			this.setAir(300);
		}
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.inWater) {

			if (!this.world.isRemote) {
				this.motionX = (double) (this.randomMotionVecX * this.randomMotionSpeed);
				this.motionY = (double) (this.randomMotionVecY * this.randomMotionSpeed);
				this.motionZ = (double) (this.randomMotionVecZ * this.randomMotionSpeed);

				if (this.motionY > 0.0 && world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY + 0.5), MathHelper.floor(posZ))).getBlock() != Blocks.WATER) {
					this.motionY = 0.0;
				}
			}

			float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float) MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float) Math.PI)
					- this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
		} else {
			if (!this.world.isRemote) {
				this.motionX = 0.0D;
				this.motionZ = 0.0D;

				if (this.isPotionActive(MobEffects.LEVITATION)) {
					this.motionY += 0.05D
							* (double) (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1)
							- this.motionY;
				} else if (!this.hasNoGravity()) {
					this.motionY -= 0.08D;
				}

				this.motionY *= 0.9800000190734863D;
			}

		}
	}

	public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		this.randomMotionVecX = randomMotionVecXIn;
		this.randomMotionVecY = randomMotionVecYIn;
		this.randomMotionVecZ = randomMotionVecZIn;
	}

	public boolean hasMovementVector() {
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}

	@Override
	protected void onGrowingAdult() {
		setScaleForAge(false);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return fishType.isFavoriteFood(stack);
	}

	static class AIMoveRandom extends EntityAIBase {
		private final EntityFish fish;

		public AIMoveRandom(EntityFish fish) {
			this.fish = fish;
		}

		@Override
		public boolean shouldContinueExecuting() {
			// return fish.randomMotionVecY <= 0.0 ||
			// fish.world.getBlockState(fish.getPosition().up()).getBlock() ==
			// Blocks.WATER;
			return shouldExecute();
		}

		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void updateTask() {
			/*
			 * int i = this.fish.getAge();
			 * 
			 * if (i > 100) { this.fish.setMovementVector(0.0F, 0.0F, 0.0F); }
			 * else
			 */if (this.fish.getRNG().nextInt(50) == 0 || !this.fish.inWater || !this.fish.hasMovementVector()) {
				float f = this.fish.getRNG().nextFloat() * ((float) Math.PI * 2F);
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + this.fish.getRNG().nextFloat() * 0.2F;
				float f3 = MathHelper.sin(f) * 0.2F;
				this.fish.setMovementVector(f1, f2, f3);
			}
		}
	}

	@SubscribeEvent
	public static void registerFishTypes(RegistryEvent.Register<FishType> event) {
		FishTypes.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void createFishRegistry(RegistryEvent.NewRegistry event) {
		/*REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation(MinaMod.MODID, "fishtypes"),
				FishType.class, new ResourceLocation(MinaMod.MODID, "hikari"), 0, 15, true, FishCallbacks.get(),
				FishCallbacks.get(), FishCallbacks.get(), FishCallbacks.get());*/
		
		if(REGISTRY == null){
			REGISTRY = new RegistryBuilder<FishType>()
							.setName(new ResourceLocation(MinaMod.MODID, "fishtypes"))
							.setDefaultKey(DEFAULT_FISH_TYPE)
							.setIDRange(0, 15)
							.setType(FishType.class)
							.create();
		}else{
			throw new RuntimeException("Fish type registry already created");
		}
	}

}
