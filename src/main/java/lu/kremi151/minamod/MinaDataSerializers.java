package lu.kremi151.minamod;

import java.io.IOException;

import lu.kremi151.minamod.capabilities.stats.util.StatData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class MinaDataSerializers {

	public static final DataSerializer<StatData> STAT_DATA = new DataSerializer<StatData>()
	{
		@Override
	    public void write(PacketBuffer buf, StatData value)
	    {
	        buf.writeByte((byte)(value.getActual() & 0xFF));
	        buf.writeShort((short)((value.getTraining() + 255) & 0xFFFF));
	    }
		
		@Override
	    public StatData read(PacketBuffer buf) throws IOException
	    {
	        StatData data = new StatData(buf.readByte() & 0xFF, (buf.readShort() & 0xFFFF) - 255);
	        return data;
	    }
		
		@Override
	    public DataParameter<StatData> createKey(int id)
	    {
	        return new DataParameter(id, this);
	    }

		@Override
		public StatData copyValue(StatData value) {
			return value.copy();
		}
	};
	
	static void register() {
		DataSerializers.registerSerializer(STAT_DATA);
	}

}
