package lu.kremi151.minamod.packet.message.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.packet.message.MessageReportSlotMachine;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageReportSlotMachineHandler extends AbstractServerMessageHandler<MessageReportSlotMachine>{

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageReportSlotMachine message, MessageContext ctx) {
		TileEntitySlotMachine.StateSnapshot snapshot = ((TileEntitySlotMachine)player.world.getTileEntity(message.getPos())).createStateSnapshot();
		File dir = new File(MinaMod.minaConfigPath.get(), "slot_machine_reports");
		dir.mkdirs();
		File report = new File(dir, System.currentTimeMillis() + ".txt");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(report))){
			StringBuilder sb = new StringBuilder();
			for(int j = 0 ; j < snapshot.wheelData[0].length ; j++) {
				for(int i = 0 ; i < snapshot.wheelData.length ; i++) {
					sb.append(snapshot.wheelData[i][j]);
					sb.append('\t');
				}
				sb.append("\r\n");
			}
			sb.append("Last spin mode: " + (snapshot.lastSpinMode != null ? snapshot.lastSpinMode.name() : "none"));
			sb.append("\r\nAwarded for last spin: " + snapshot.awardedForLastSpin);
			
			bw.write(sb.toString());
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
