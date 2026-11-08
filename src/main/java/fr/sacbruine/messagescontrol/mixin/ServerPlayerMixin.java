package fr.sacbruine.messagescontrol.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.sacbruine.messagescontrol.Config;
import fr.sacbruine.messagescontrol.helpers.DeathMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// die() picks one of these three broadcasts from the team's death message visibility.
// The death screen uses a separate packet, so it is not affected.
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @WrapOperation(method = "die", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void messagescontrol$broadcastDeath(PlayerList list, Component message, boolean bypassHiddenChat,
            Operation<Void> original) {
        if (Config.enableDeathMessage.get()) {
            original.call(list, DeathMessages.resolve((ServerPlayer) (Object) this, message), bypassHiddenChat);
        }
    }

    @WrapOperation(method = "die", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"))
    private void messagescontrol$broadcastDeathToTeam(PlayerList list, Player player, Component message,
            Operation<Void> original) {
        if (Config.enableDeathMessage.get()) {
            original.call(list, player, DeathMessages.resolve((ServerPlayer) (Object) this, message));
        }
    }

    @WrapOperation(method = "die", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemToAllExceptTeam(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/network/chat/Component;)V"))
    private void messagescontrol$broadcastDeathExceptTeam(PlayerList list, Player player, Component message,
            Operation<Void> original) {
        if (Config.enableDeathMessage.get()) {
            original.call(list, player, DeathMessages.resolve((ServerPlayer) (Object) this, message));
        }
    }
}
