package fr.sacbruine.messagescontrol.helpers;

import fr.sacbruine.messagescontrol.Config;
import fr.sacbruine.messagescontrol.constants.MessageKeys;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class AdvancementMessages {

    public static boolean isHidden(String key, @Nullable AdvancementHolder advancement) {
        if (!Config.enableAdvancementMessage.get()) {
            return true;
        }

        boolean hidden = switch (key) {
            case MessageKeys.ADVANCEMENT_TASK -> Config.hideAdvancementTasks.get();
            case MessageKeys.ADVANCEMENT_GOAL -> Config.hideAdvancementGoals.get();
            case MessageKeys.ADVANCEMENT_CHALLENGE -> Config.hideAdvancementChallenges.get();
            default -> false;
        };

        // Check whitelist, namespaces and exact ids
        if (advancement != null) {
            String advancementId = advancement.id().toString();
            String advancementNamespace = advancement.id().getNamespace();

            List<? extends String> whitelist = Config.advancementWhitelist.get();
            boolean whitelisted = whitelist.contains(advancementId) || whitelist.contains(advancementNamespace);

            if (whitelisted)
                return false;

            return hidden
                    || Config.hiddenAdvancementNamespaces.get().contains(advancementNamespace)
                    || Config.hiddenAdvancementIds.get().contains(advancementId);
        }
        return hidden;
    }

    public static Component format(String key, TranslatableContents vanillaMessage,
            @Nullable AdvancementHolder advancement) {
        Object[] args = vanillaMessage.getArgs();

        return MessageFormatter.format(Config.advancementCustomMessage.get(), Map.of(
                "player", Component.literal(MessageFormatter.playerName(MessageFormatter.toComponent(args[0]))),
                "advancement", MessageFormatter.toComponent(args[1]), // vanilla title, keeps color + hover
                "advancement_id", Component.literal(advancement != null ? advancement.id().toString() : ""),
                "type", Component.literal(key.substring(key.lastIndexOf('.') + 1)))); // task, goal, challenge
    }
}
