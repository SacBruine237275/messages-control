package fr.sacbruine.messagescontrol.mixin;

import fr.sacbruine.messagescontrol.Config;
import fr.sacbruine.messagescontrol.enums.MessageMode;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
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
import java.util.function.Function;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    public abstract void broadcastSystemMessage(Component message,
            Function<ServerPlayer, Component> playerMessageFactory, boolean bypassHiddenChat);

    @Unique
    private boolean messagecontrol$firstJoin = false;

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void detectFirstJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie,
            CallbackInfo ci) {
        messagecontrol$firstJoin = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) == 0;
    }

    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void resetFirstJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie,
            CallbackInfo ci) {
        messagecontrol$firstJoin = false;
    }

    @Inject(method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
    private void onBroadcastSystemMessage(Component serverMessage,
            Function<ServerPlayer, Component> playerMessageFactory, boolean bypassHiddenChat, CallbackInfo ci) {
        ComponentContents contents = serverMessage.getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            String key = translatableContents.getKey();
            switch (key) {
                case "multiplayer.player.joined", "multiplayer.player.joined.renamed":
                    if(messagecontrol$firstJoin && Config.enableFirstJoinMessage.get()){
                        messagescontrol$handleMessage(true,MessageMode.CUSTOM,Config.firstJoinCustomMessage.get(),
                                translatableContents,1,bypassHiddenChat,ci);
                    }else {
                        messagescontrol$handleMessage(Config.enableJoinMessage.get(), Config.joinMessageMode.get(),
                                Config.joinCustomMessage.get(), translatableContents, 1, bypassHiddenChat, ci);
                    };
                    break;
                case "multiplayer.player.left":
                    messagescontrol$handleMessage(Config.enableLeaveMessage.get(), Config.leaveMessageMode.get(),
                            Config.leaveCustomMessage.get(), translatableContents, -1, bypassHiddenChat, ci);
                    break;
                default:
                    break;
            }
        }
    }

    private void messagescontrol$handleMessage(boolean enabled, MessageMode mode, String template,
            TranslatableContents vanillaMessage,
            int playerOffset, boolean bypassHiddenChat, CallbackInfo ci) {
        if (!enabled) {
            ci.cancel(); // hidden
            return;
        }
        if (mode == MessageMode.CUSTOM) {
            ci.cancel();
            Component displayName = (Component) vanillaMessage.getArgs()[0];
            Component customMessage = Component
                    .literal(messagescontrol$formatMessage(template, displayName, playerOffset));
            this.broadcastSystemMessage(customMessage, p -> customMessage, bypassHiddenChat);
        }
        // VANILLA: do nothing, Minecraft sends its own message
    }

    private String messagescontrol$formatMessage(String message, Component displayNameComponent, int playerOffset) {
        PlayerList self = (PlayerList) (Object) this;

        int onlinePlayers = self.getPlayerCount() + playerOffset;
        int maxPlayers = self.getMaxPlayers();
        String displayName = displayNameComponent.getString();
        String insertion = displayNameComponent.getStyle().getInsertion();
        String playerName = insertion != null ? insertion : displayName;

        return message
                .replace("{player}", playerName)
                .replace("{display_name}", displayName)
                .replace("{online}", String.valueOf(onlinePlayers))
                .replace("{max}", String.valueOf(maxPlayers))
                .replaceAll("&([0-9a-fk-or])", "§$1"); // &e -> §e (Minecraft color codes)

    }
}
