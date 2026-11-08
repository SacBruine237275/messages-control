package fr.sacbruine.messagescontrol;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MessagesControl.MODID)
public class MessagesControl {
    public static final String MODID = "messagescontrol";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MessagesControl(ModContainer container){
        container.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }

    public static void logHiddenMessage(Component message) {
        if (Config.logToConsole.get()) {
            LOGGER.info(message.getString());
        }
    }
}
