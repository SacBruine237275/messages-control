package fr.sacbruine.messagescontrol.helpers;

import fr.sacbruine.messagescontrol.Config;
import fr.sacbruine.messagescontrol.enums.MessageMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public final class DeathMessages {

    public static Component resolve(ServerPlayer player, Component vanillaMessage) {
        if (Config.deathMessageMode.get() != MessageMode.CUSTOM) {
            return vanillaMessage;
        }

        BlockPos pos = player.blockPosition();
        return MessageFormatter.format(Config.deathCustomMessage.get(), Map.of(
                "player", Component.literal(player.getName().getString()),
                "death_message", vanillaMessage, // keeps translation + hover
                "dimension", Component.literal(player.level().dimension().identifier().toString()),
                "x", Component.literal(String.valueOf(pos.getX())),
                "y", Component.literal(String.valueOf(pos.getY())),
                "z", Component.literal(String.valueOf(pos.getZ()))));
    }
}
