package lu.kremi151.minamod.capabilities.energynetwork;

import javax.annotation.Nullable;

import net.minecraft.world.World;

public class EnergyNetworkHelper {

	public static IEnergyNetwork createNetwork(World world) {
		return new NetworkPointer(new NetworkImpl(world));
	}
	
	@Nullable
	public static IEnergyNetwork combine(IEnergyNetwork a, IEnergyNetwork b) throws IllegalArgumentException{
		if(!(a instanceof NetworkImpl && b instanceof NetworkPointer)) {
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
	
}
