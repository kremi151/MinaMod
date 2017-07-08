package lu.kremi151.minamod.village;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import lu.kremi151.minamod.entity.EntityWookie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WookieVillage {

	private World world;
	private BlockPos center;
	private int villageRadius;
	private int numWookies;
    private int tickCounter;
    private final Map<UUID, Integer> playerReputation = Maps.<UUID, Integer>newHashMap();

    public WookieVillage()
    {
    }

    public WookieVillage(World worldIn)
    {
        this.world = worldIn;
    }

    public void setWorld(World worldIn)
    {
        this.world = worldIn;
    }
    
    public void tick(int tickCounterIn)
    {
        this.tickCounter = tickCounterIn;

        if (tickCounterIn % 20 == 0)
        {
            this.updateNumWookies();
        }
    	
    }

    private void updateNumWookies()
    {
        List<EntityWookie> list = this.world.<EntityWookie>getEntitiesWithinAABB(EntityWookie.class, new AxisAlignedBB((double)(this.center.getX() - this.villageRadius), (double)(this.center.getY() - 4), (double)(this.center.getZ() - this.villageRadius), (double)(this.center.getX() + this.villageRadius), (double)(this.center.getY() + 4), (double)(this.center.getZ() + this.villageRadius)));
        this.numWookies = list.size();

        if (this.numWookies == 0)
        {
            this.playerReputation.clear();
        }
    }

    private Vec3d findRandomSpawnPos(BlockPos pos, int x, int y, int z)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = pos.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);

            if (this.isBlockPosWithinSqVillageRadius(blockpos) && this.isAreaClearAround(new BlockPos(x, y, z), blockpos))
            {
                return new Vec3d((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            }
        }

        return null;
    }
    /**
     * Checks to see if the volume around blockLocation is clear and able to fit blockSize
     */
    private boolean isAreaClearAround(BlockPos blockSize, BlockPos blockLocation)
    {
        if (!this.world.getBlockState(blockLocation.down()).isTopSolid())
        {
            return false;
        }
        else
        {
            int i = blockLocation.getX() - blockSize.getX() / 2;
            int j = blockLocation.getZ() - blockSize.getZ() / 2;

            for (int k = i; k < i + blockSize.getX(); ++k)
            {
                for (int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l)
                {
                    for (int i1 = j; i1 < j + blockSize.getZ(); ++i1)
                    {
                        if (this.world.getBlockState(new BlockPos(k, l, i1)).isNormalCube())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    public BlockPos getCenter()
    {
        return this.center;
    }

    public int getVillageRadius()
    {
        return this.villageRadius;
    }

    public int getNumWookies()
    {
        return this.numWookies;
    }

    /**
     * Checks to see if the distance squared between this BlockPos and the center of this Village is less than this
     * Village's villageRadius squared
     */
    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos)
    {
        return this.center.distanceSq(pos) < (double)(this.villageRadius * this.villageRadius);
    }

    /**
     * Return the village reputation for a player
     */
    public int getPlayerReputation(UUID playerUUID)
    {
        Integer integer = (Integer)this.playerReputation.get(playerUUID);
        return integer == null ? 0 : integer.intValue();
    }

    /**
     * Modify a players reputation in the village. Use positive values to increase reputation and negative values to
     * decrease. <br>Note that a players reputation is clamped between -30 and 10
     */
    public int modifyPlayerReputation(UUID playerUUID, int reputation)
    {
        int i = this.getPlayerReputation(playerUUID);
        int j = MathHelper.clamp(i + reputation, -30, 10);
        this.playerReputation.put(playerUUID, Integer.valueOf(j));
        return j;
    }

    /**
     * Return whether this player has a too low reputation with this village.
     */
    public boolean isPlayerReputationTooLow(UUID playerUUID)
    {
        return this.getPlayerReputation(playerUUID) <= -15;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound compound)
    {
        this.numWookies = compound.getInteger("PopSize");
        this.villageRadius = compound.getInteger("Radius");
        this.tickCounter = compound.getInteger("Tick");
        this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
    
        NBTTagList nbttaglist1 = compound.getTagList("Players", 10);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);

            this.playerReputation.put(UUID.fromString(nbttagcompound1.getString("UUID")), Integer.valueOf(nbttagcompound1.getInteger("S")));
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NBTTagCompound compound)
    {
        compound.setInteger("PopSize", this.numWookies);
        compound.setInteger("Radius", this.villageRadius);
        compound.setInteger("Tick", this.tickCounter);
        compound.setInteger("CX", this.center.getX());
        compound.setInteger("CY", this.center.getY());
        compound.setInteger("CZ", this.center.getZ());

        NBTTagList reputations = new NBTTagList();

        for (Map.Entry<UUID, Integer> entry : this.playerReputation.entrySet())
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            PlayerProfileCache playerprofilecache = this.world.getMinecraftServer().getPlayerProfileCache();

            nbttagcompound1.setString("UUID", entry.getKey().toString());
            nbttagcompound1.setInteger("S", entry.getValue().intValue());
            reputations.appendTag(nbttagcompound1);
        }

        compound.setTag("Players", reputations);
    }
}
