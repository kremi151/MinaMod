package lu.kremi151.minamod.block;

import java.util.Iterator;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

public class BlockPipeBase extends Block {
	public static final float PIPE_MIN_POS = 0.25f;
	public static final float PIPE_MAX_POS = 0.75f;

	public static final ImmutableList<IProperty> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			new FacingPropertyIterable()
	);

	public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
			new FacingAxisIterable()
	);
	
	private static final AxisAlignedBB aabb[];

	private static float getMinBound(int dir) {
		return dir == -1 ? 0 : PIPE_MIN_POS;
	}

	private static float getMaxBound(int dir) {
		return dir == 1 ? 1 : PIPE_MAX_POS;
	}
	
	static{
		int bound = (int) Math.pow(2, EnumFacing.VALUES.length);
		aabb = new AxisAlignedBB[bound];
		for(int b = 0 ; b < bound ; b++){
			boolean bit[] = new boolean[EnumFacing.VALUES.length];
			for(int bb = 0 ; bb < EnumFacing.VALUES.length ; bb++){
				bit[bb] = ((b >> bb) & 1) == 1;
			}
			AxisAlignedBB aabb_ = null;
			for(int bb = 0 ; bb < EnumFacing.VALUES.length ; bb++){
				if(bit[bb]){
					if(aabb_ == null){
						aabb_ = CONNECTED_BOUNDING_BOXES.get(bb);
					}else{
						aabb_ = aabb_.union(CONNECTED_BOUNDING_BOXES.get(bb));
					}
				}
			}
			aabb[b] = aabb_;
		}
	}

	public BlockPipeBase(Material material) {
		super(material);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * Is the neighbouring pipe a valid connection for this pipe?
	 *
	 * @param ownState           This pipe's state
	 * @param neighbourState     The neighbouring pipe's state
	 * @param world              The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring pipe
	 * @return Is the neighbouring pipe a valid connection?
	 */
	protected boolean isValidPipe(IBlockState ownState, IBlockState neighbourState, IBlockAccess world, BlockPos ownPos, EnumFacing neighbourDirection) {
		return neighbourState.getBlock() instanceof BlockPipeBase;
	}

	/**
	 * Can this pipe connect to the neighbouring block?
	 *
	 * @param ownState           This pipe's state
	 * @param worldIn            The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Can this pipe connect?
	 */
	protected boolean canConnectTo(IBlockState ownState, IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection) {
		BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
		Block neighbourBlock = neighbourState.getBlock();

		boolean neighbourIsValidForThis = isValidPipe(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
		boolean thisIsValidForNeighbour = neighbourBlock instanceof BlockPipeBase && ((BlockPipeBase) neighbourBlock).isValidPipe(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, pos, facing));
		}

		return state;
	}

	public final boolean isConnected(IBlockState state, EnumFacing facing) {
		return (Boolean) state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		int aabbi = 0;
		for(int i = 0 ; i < EnumFacing.VALUES.length ; i++){
			aabbi |= (1 << i);
		}
		return aabb[aabbi];
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return getBoundingBox(blockState, worldIn, pos);
    }
	
	protected int getConnections(IBlockState blockState){
		int count = 0;
		for(EnumFacing ef : EnumFacing.VALUES){
			if(isConnected(blockState, ef)){
				count++;
			}
		}
		return count;
	}
	
	protected boolean isMultiConnected(IBlockState blockState){
		return getConnections(blockState) > 2;
	}
	
	private static class FacingPropertyIterable implements Iterable<IProperty>{

		@Override
		public Iterator<IProperty> iterator() {
			return new FacingPropertyIterator();
		}
		
	}
	
	private static class FacingAxisIterable implements Iterable<AxisAlignedBB>{

		@Override
		public Iterator<AxisAlignedBB> iterator() {
			return new FacingAxisIterator();
		}
		
	}
	
	private static class FacingPropertyIterator implements Iterator<IProperty>{
		
		private int i = 0;
		private final IProperty props[];
		
		private FacingPropertyIterator(){
			props = new PropertyBool[EnumFacing.VALUES.length];
			for(int j = 0 ; j < EnumFacing.VALUES.length ; j++){
				props[j] = PropertyBool.create(EnumFacing.VALUES[j].getName());
			}
		}

		@Override
		public boolean hasNext() {
			return i >= 0 && i < EnumFacing.VALUES.length;
		}

		@Override
		public IProperty next() {
			return props[i++];
		}
		
	}
	
	private static class FacingAxisIterator implements Iterator<AxisAlignedBB>{
		
		private int i = 0;
		private final AxisAlignedBB axis[];
		
		private FacingAxisIterator(){
			axis = new AxisAlignedBB[EnumFacing.VALUES.length];
			for(int j = 0 ; j < EnumFacing.VALUES.length ; j++){
				Vec3i directionVec = EnumFacing.VALUES[j].getDirectionVec();
				axis[j] = new AxisAlignedBB(
						getMinBound(directionVec.getX()), getMinBound(directionVec.getY()), getMinBound(directionVec.getZ()),
						getMaxBound(directionVec.getX()), getMaxBound(directionVec.getY()), getMaxBound(directionVec.getZ())
				);
			}
		}

		@Override
		public boolean hasNext() {
			return i >= 0 && i < EnumFacing.VALUES.length;
		}

		@Override
		public AxisAlignedBB next() {
			return axis[i++];
		}
		
	}

}
