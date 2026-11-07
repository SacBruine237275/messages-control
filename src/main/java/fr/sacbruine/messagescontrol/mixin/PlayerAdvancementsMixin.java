package fr.sacbruine.messagescontrol.mixin;

import fr.sacbruine.messagescontrol.helpers.AdvancementContext;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Inject(method = "award", at = @At("HEAD"))
    private void messagescontrol$pushAdvancement(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        AdvancementContext.push(advancement);
    }

    @Inject(method = "award", at = @At("RETURN"))
    private void messagescontrol$popAdvancement(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        AdvancementContext.pop();
    }
}
