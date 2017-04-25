package lu.kremi151.minamod.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class TextHelper {

	@Deprecated
	public static void sendChatMessage(ICommandSender cs, TextFormatting formatting, String text) {
		TextComponentString cct = new TextComponentString(text);
		Style style = new Style();
		style.setColor(formatting);
		cct.setStyle(style);
		cs.sendMessage(cct);
	}

	@Deprecated
	public static void sendChatMessage(ICommandSender cs, String text) {
		sendChatMessage(cs, TextFormatting.RESET, text);
	}

	@Deprecated
	public static void sendErrorMessage(ICommandSender cs, String text) {
		sendChatMessage(cs, TextFormatting.RED, text);
	}
	
	@Deprecated
	public static void sendText(ICommandSender cs, Object... textArgs) throws InvalidTextException{
		ITextComponent comp = buildTextComponent(false, textArgs);
		if(comp != null){
			cs.sendMessage(comp);
		}else{
			throw new InvalidTextException("Text could not be created using the given arguments");
		}
	}

	public static void sendTranslateableChatMessage(ICommandSender cs, TextFormatting formatting, String translationKey, Object... args){
		TextComponentTranslation cct = new TextComponentTranslation(translationKey, args);
		Style style = new Style();
		style.setColor(formatting);
		cct.setStyle(style);
		cs.sendMessage(cct);
	}

	public static void sendTranslateableChatMessage(ICommandSender cs, String translationKey, Object... args) {
		sendTranslateableChatMessage(cs, TextFormatting.RESET, translationKey, args);
	}

	public static void sendTranslateableErrorMessage(ICommandSender cs, String translationKey, Object... args) {
		sendTranslateableChatMessage(cs, TextFormatting.RED, translationKey, args);
	}
	
	public static ITextComponent buildTextComponent(boolean usesTranslation, Object... textArgs){
		TextFormatting format = TextFormatting.RESET;
		ITextComponent comp = null;
		for(int i = 0 ; i < textArgs.length ; i++){
			Object obj = textArgs[i];
			if(obj instanceof TextFormatting){
				format = (TextFormatting)obj;
			}else if(obj instanceof String){
				ITextComponent newc;
				if(usesTranslation){
					newc = new TextComponentTranslation((String)obj);
				}else{
					newc = new TextComponentString((String)obj);
				}
				Style style = new Style();
				style.setColor(format);
				newc.setStyle(style);
				if(comp == null){
					comp = newc;
				}else{
					comp.appendSibling(newc);
				}
			}else if(obj instanceof ITextComponent){
				if(comp == null){
					comp = (ITextComponent)obj;
				}else{
					comp.appendSibling((ITextComponent)obj);
				}
			}else{
				throw new InvalidTextException("Invalid argument type: " + obj.getClass());
			}
		}
		return comp;
	}
	
	public static void sendTranslateableText(ICommandSender cs, Object... textArgs) throws InvalidTextException{
		ITextComponent comp = buildTextComponent(true, textArgs);
		if(comp != null){
			cs.sendMessage(comp);
		}else{
			throw new InvalidTextException("Text could not be created using the given arguments");
		}
	}

	public static class InvalidTextException extends RuntimeException{
		
		public InvalidTextException(String message){
			super(message);
		}
	}
}
