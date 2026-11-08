package fr.sacbruine.messagescontrol;

import fr.sacbruine.messagescontrol.enums.MessageMode;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.regex.Pattern;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final String MESSAGE_HELP =
            "Variables: {player}, {display_name}, {online}, {max} | Colors: &e yellow, &a green, &c red, &7 gray, &l bold...";

    // Static blocks run in order with the fields: each push/pop wraps the fields between them in a [section]

    //region Join
    static {
        BUILDER.comment("Message shown when a player joins").push("join");
    }

    public static final ModConfigSpec.BooleanValue enableJoinMessage =
            BUILDER.comment("true = shown, false = hidden")
                    .define("enabled", true);

    public static final ModConfigSpec.EnumValue<MessageMode> joinMessageMode =
            BUILDER.comment("VANILLA = default Minecraft message, CUSTOM = customMessage below")
                    .defineEnum("mode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> joinCustomMessage =
            BUILDER.comment(MESSAGE_HELP)
                    .define("customMessage", "&e{display_name} joined the game ({online}/{max})");

    static {
        BUILDER.pop();
    }
    //endregion

    //region First Join
    static {
        BUILDER.comment("Message shown when a player joins the server for the first time")
                .comment("Minecraft has no default first join message: customMessage is always used when enabled")
                .push("firstJoin");
    }

    public static final ModConfigSpec.BooleanValue enableFirstJoinMessage =
            BUILDER.comment("true = customMessage shown, false = regular [join] message used")
                    .define("enabled", false);

    public static final ModConfigSpec.ConfigValue<String> firstJoinCustomMessage =
            BUILDER.comment(MESSAGE_HELP)
                    .define("customMessage", "&e{display_name} joined the game for the first time ({online}/{max})");

    static {
        BUILDER.pop();
    }
    //endregion

    //region Leave
    static {
        BUILDER.comment("Message shown when a player leaves").push("leave");
    }

    public static final ModConfigSpec.BooleanValue enableLeaveMessage =
            BUILDER.comment("true = shown, false = hidden")
                    .define("enabled", true);

    public static final ModConfigSpec.EnumValue<MessageMode> leaveMessageMode =
            BUILDER.comment("VANILLA = default Minecraft message, CUSTOM = customMessage below")
                    .defineEnum("mode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> leaveCustomMessage =
            BUILDER.comment(MESSAGE_HELP)
                    .define("customMessage", "&e{display_name} left the game ({online}/{max})");

    static {
        BUILDER.pop();
    }
    //endregion

    //region Advancements
    private static final String ADVANCEMENT_MESSAGE_HELP =
            "Variables: {player}, {advancement} (translated title, vanilla hover kept), {advancement_id}, {type} (task, goal, challenge) | Colors: &e yellow, &a green, &c red, &7 gray, &l bold...";

    // namespace: "minecraft", "create"... / id: "minecraft:story/mine_stone"
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("[a-z0-9_.-]+");
    private static final Pattern ADVANCEMENT_ID_PATTERN = Pattern.compile("[a-z0-9_.-]+:[a-z0-9_./-]+");

    static {
        BUILDER.comment("Message shown in chat when a player gets an advancement")
                .comment("Only the chat message is affected: the advancement is still granted and the player's toast is still shown")
                .push("advancements");
    }

    public static final ModConfigSpec.BooleanValue enableAdvancementMessage =
            BUILDER.comment("true = shown, false = hidden")
                    .define("enabled", true);

    public static final ModConfigSpec.EnumValue<MessageMode> advancementMessageMode =
            BUILDER.comment("VANILLA = default Minecraft message, CUSTOM = customMessage below")
                    .defineEnum("mode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> advancementCustomMessage =
            BUILDER.comment(ADVANCEMENT_MESSAGE_HELP)
                    .comment("A color before {advancement} recolors it (hover kept). Use &r before it to keep the vanilla color (green, purple for challenges)")
                    .define("customMessage", "{player} has made the advancement {advancement}");

    // Sub-section [advancements.filters]
    static {
        BUILDER.comment("Hide some advancement messages. The whitelist always wins over these filters")
                .push("filters");
    }

    public static final ModConfigSpec.BooleanValue hideAdvancementTasks =
            BUILDER.comment("true = hide messages for 'task' advancements (most common ones). Example: \"Stone Age\" (minecraft:story/mine_stone)")
                    .define("hideTasks", false);

    public static final ModConfigSpec.BooleanValue hideAdvancementGoals =
            BUILDER.comment("true = hide messages for 'goal' advancements. Example: \"The End... Again...\" (minecraft:end/respawn_dragon)")
                    .define("hideGoals", false);

    public static final ModConfigSpec.BooleanValue hideAdvancementChallenges =
            BUILDER.comment("true = hide messages for 'challenge' advancements. Example: \"Monsters Hunted\" (minecraft:adventure/kill_all_mobs)")
                    .define("hideChallenges", false);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> hiddenAdvancementNamespaces =
            BUILDER.comment("Hide every advancement from these namespaces. Example: [\"create\", \"minecraft\"]")
                    .defineListAllowEmpty("hiddenNamespaces", List.of(), () -> "minecraft",
                            o -> o instanceof String s && NAMESPACE_PATTERN.matcher(s).matches());

    public static final ModConfigSpec.ConfigValue<List<? extends String>> hiddenAdvancementIds =
            BUILDER.comment("Hide these exact advancements. Example: [\"minecraft:story/mine_stone\"]")
                    .defineListAllowEmpty("hiddenAdvancements", List.of(), () -> "minecraft:story/mine_stone",
                            o -> o instanceof String s && ADVANCEMENT_ID_PATTERN.matcher(s).matches());

    public static final ModConfigSpec.ConfigValue<List<? extends String>> advancementWhitelist =
            BUILDER.comment("Always shown, even if hidden by a filter above")
                    .comment("Accepts exact ids (\"minecraft:end/kill_dragon\") or whole namespaces (\"create\")")
                    .defineListAllowEmpty("whitelist", List.of(), () -> "minecraft:end/kill_dragon",
                            o -> o instanceof String s
                                    && (ADVANCEMENT_ID_PATTERN.matcher(s).matches() || NAMESPACE_PATTERN.matcher(s).matches()));

    static {
        BUILDER.pop(2); // filters + advancements
    }
    //endregion

    //region Death
    private static final String DEATH_MESSAGE_HELP =
            "Variables: {player}, {death_message} (vanilla cause, translated), {dimension}, {x}, {y}, {z} | Colors: &e yellow, &a green, &c red, &7 gray, &l bold...";

    static {
        BUILDER.comment("Message shown in chat when a player dies")
                .comment("Only the chat message is affected: the message on the player's death screen is not changed")
                .push("death");
    }

    public static final ModConfigSpec.BooleanValue enableDeathMessage =
            BUILDER.comment("true = shown, false = hidden")
                    .define("enabled", true);

    public static final ModConfigSpec.EnumValue<MessageMode> deathMessageMode =
            BUILDER.comment("VANILLA = default Minecraft message, CUSTOM = customMessage below")
                    .defineEnum("mode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> deathCustomMessage =
            BUILDER.comment(DEATH_MESSAGE_HELP)
                    .define("customMessage", "&c{death_message}");

    static {
        BUILDER.pop();
    }
    //endregion

    static final ModConfigSpec SPEC = BUILDER.build();
}
