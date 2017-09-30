package lu.kremi151.minamod.capabilities.energynetwork;

import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class EnergyNetworkHelper {

	public static IEnergyNetwork createNetwork(IBlockAccess world) {
		return new NetworkPointer(new NetworkImpl(world));
	}
	
	@Nullable
	public static IEnergyNetwork combine(IEnergyNetwork a, IEnergyNetwork b) throws IllegalArgumentException{
		if(a == null) {
			return b;
		}else if(b == null) {
			return a;
		}
		if(!(a instanceof NetworkPointer && b instanceof NetworkPointer)) {
			throw new IllegalArgumentException("The supplied networks have to be provided by EnergyNetworkHelper::createNetwork");
		}
		NetworkPointer pa = (NetworkPointer) a, pb = (NetworkPointer) b;
		if(pa.getPointingNetwork() != null && pb.getPointingNetwork() != null) {
			if(pa.getPointingNetwork() != pb.getPointingNetwork()) {
				forceCombine(pa.getPointingNetwork(), pb.getPointingNetwork());
			}
			return pa;
		}else if(pa.getPointingNetwork() != null && pb.getPointingNetwork() == null) {
			pb.swapNetwork(pa.getPointingNetwork());
			pa.getPointingNetwork().pointers.add(pb);
			return pa;
		}else if(pa.getPointingNetwork() == null && pb.getPointingNetwork() != null) {
			pa.swapNetwork(pb.getPointingNetwork());
			pb.getPointingNetwork().pointers.add(pa);
			return pb;
		}else {
			return null;
		}
	}
	
	private static void forceCombine(NetworkImpl a, NetworkImpl b) {
		a.clients.putAll(b.clients);
		a.pointers.addAll(b.pointers);
		for(NetworkPointer sp : b.pointers) {
			sp.swapNetwork(a);
		}
	}
	
	/**
	 * Splits a network
	 * @param old The old network
	 * @param origin The splitting block position
	 * @return An array of resulting networks for each direction. If one direction does not contain a network, the value at it's ordinal position in the array will be null.
	 */
	public static IEnergyNetwork[] split(IEnergyNetwork old, BlockPos splitter) {
		if(old == null) {
			throw new NullPointerException("Cannot split a null network");
		}else if(!(old instanceof NetworkPointer)) {
			throw new IllegalArgumentException("The supplied network has to be provided by EnergyNetworkHelper::createNetwork");
		}
		old = old.copy();
		NetworkImpl ni = ((NetworkPointer)old).getPointingNetwork();
		IEnergyNetwork res[] = new IEnergyNetwork[EnumFacing.VALUES.length];
		if(ni == null) {
			return res;
		}else {
			ni.clients.clear();
			ni.networkBlocks.remove(splitter);
			HashMap<BlockPos, EnumFacing> posToDirection = new HashMap<>();
			for(EnumFacing face : EnumFacing.VALUES) {
				HashSet<BlockPos> extracted = new HashSet<>();
				iterateSplittingEdge(ni, splitter.offset(face), extracted);
				if(extracted.size() > 0) {
					IEnergyNetwork newNetwork = createNetwork(ni.world);
					for(BlockPos epos : extracted) {
						posToDirection.put(epos, face);
						newNetwork.registerNetworkBlock(epos);
					}
					res[face.ordinal()] = newNetwork;
				}else {
					EnumFacing potentialMatch = posToDirection.get(splitter.offset(face));
					if(potentialMatch != null) {
						res[face.ordinal()] = res[potentialMatch.ordinal()];
					}
				}
			}
			return res;
		}
		
	}
	
	private static void iterateSplittingEdge(NetworkImpl source, BlockPos origin, HashSet<BlockPos> extracted) {
		if(source.networkBlocks.contains(origin)) {
			source.networkBlocks.remove(origin);
			extracted.add(origin);
			for(EnumFacing face : EnumFacing.VALUES) {
				BlockPos npos = origin.offset(face);
				iterateSplittingEdge(source, npos, extracted);
			}
		}
	}
	
}
