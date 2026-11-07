package fr.sacbruine.messagescontrol;

import fr.sacbruine.messagescontrol.enums.MessageMode;
import net.neoforged.neoforge.common.ModConfigSpec;

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

    static final ModConfigSpec SPEC = BUILDER.build();
}
