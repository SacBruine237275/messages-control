package fr.sacbruine.messagescontrol.mixin;

import fr.sacbruine.messagescontrol.Config;
import fr.sacbruine.messagescontrol.MessagesControl;
import fr.sacbruine.messagescontrol.constants.MessageKeys;
import fr.sacbruine.messagescontrol.constants.PlayerCountOffsets;
import fr.sacbruine.messagescontrol.enums.MessageMode;
import fr.sacbruine.messagescontrol.helpers.AdvancementContext;
import fr.sacbruine.messagescontrol.helpers.AdvancementMessages;
import fr.sacbruine.messagescontrol.helpers.MessageFormatter;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    public abstract void broadcastSystemMessage(Component message,
            Function<ServerPlayer, Component> playerMessageFactory, boolean bypassHiddenChat);

    @Unique
    private boolean messagescontrol$firstJoin = false;

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void messagescontrol$detectFirstJoin(Connection connection, ServerPlayer player,
            CommonListenerCookie cookie,
            CallbackInfo ci) {
        messagescontrol$firstJoin = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) == 0;
    }

    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void messagescontrol$resetFirstJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie,
            CallbackInfo ci) {
        messagescontrol$firstJoin = false;
    }

    @Inject(method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
    private void messagescontrol$onBroadcastSystemMessage(Component serverMessage,
            Function<ServerPlayer, Component> playerMessageFactory, boolean bypassHiddenChat, CallbackInfo ci) {
        ComponentContents contents = serverMessage.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            String key = translatableContents.getKey();
            switch (key) {
                case MessageKeys.PLAYER_JOINED, MessageKeys.PLAYER_JOINED_RENAMED:
                    if (messagescontrol$firstJoin && Config.enableFirstJoinMessage.get()) {
                        messagescontrol$handleMessage(true, MessageMode.CUSTOM, Config.firstJoinCustomMessage.get(),
                                translatableContents, PlayerCountOffsets.JOINING_PLAYER_NOT_YET_COUNTED,
                                bypassHiddenChat, ci);
                    } else {
                        messagescontrol$handleMessage(Config.enableJoinMessage.get(), Config.joinMessageMode.get(),
                                Config.joinCustomMessage.get(), translatableContents,
                                PlayerCountOffsets.JOINING_PLAYER_NOT_YET_COUNTED, bypassHiddenChat, ci);
                    }
                    break;
                case MessageKeys.PLAYER_LEFT:
                    messagescontrol$handleMessage(Config.enableLeaveMessage.get(), Config.leaveMessageMode.get(),
                            Config.leaveCustomMessage.get(), translatableContents,
                            PlayerCountOffsets.LEAVING_PLAYER_STILL_COUNTED, bypassHiddenChat, ci);
                    break;
                case MessageKeys.ADVANCEMENT_TASK, MessageKeys.ADVANCEMENT_GOAL, MessageKeys.ADVANCEMENT_CHALLENGE:
                    messagescontrol$handleAdvancement(key, translatableContents, bypassHiddenChat, ci);
                    break;
                default:
                    break;
            }
        }
    }

    @Unique
    private void messagescontrol$handleMessage(boolean enabled, MessageMode mode, String template,
            TranslatableContents vanillaMessage,
            int playerOffset, boolean bypassHiddenChat, CallbackInfo ci) {
        if (!enabled) {
            ci.cancel(); // hidden
            MessagesControl.logHiddenMessage(MutableComponent.create(vanillaMessage));
            return;
        }
        if (mode == MessageMode.CUSTOM) {
            ci.cancel();
            Component displayName = MessageFormatter.toComponent(vanillaMessage.getArgs()[0]);
            Component customMessage = messagescontrol$formatMessage(template, displayName, playerOffset);
            this.broadcastSystemMessage(customMessage, p -> customMessage, bypassHiddenChat);
        }
        // VANILLA: do nothing, Minecraft sends its own message
    }

    @Unique
    private void messagescontrol$handleAdvancement(String key, TranslatableContents vanillaMessage,
            boolean bypassHiddenChat, CallbackInfo ci) {
        AdvancementHolder advancement = AdvancementContext.current();

        if (AdvancementMessages.isHidden(key, advancement)) {
            ci.cancel();
            MessagesControl.logHiddenMessage(MutableComponent.create(vanillaMessage));
            return;
        }
        if (Config.advancementMessageMode.get() == MessageMode.CUSTOM) {
            ci.cancel();
            Component customMessage = AdvancementMessages.format(key, vanillaMessage, advancement);
            this.broadcastSystemMessage(customMessage, p -> customMessage, bypassHiddenChat);
        }
        // VANILLA: do nothing, Minecraft sends its own message
    }

    @Unique
    private Component messagescontrol$formatMessage(String message, Component displayNameComponent, int playerOffset) {
        PlayerList self = (PlayerList) (Object) this;

        int onlinePlayers = self.getPlayerCount() + playerOffset;
        int maxPlayers = self.getMaxPlayers();

        return MessageFormatter.format(message, Map.of(
                "player", Component.literal(MessageFormatter.playerName(displayNameComponent)),
                "display_name", Component.literal(displayNameComponent.getString()),
                "online", Component.literal(String.valueOf(onlinePlayers)),
                "max", Component.literal(String.valueOf(maxPlayers))));
    }
}
