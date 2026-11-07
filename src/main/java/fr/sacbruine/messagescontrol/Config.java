package fr.sacbruine.messagescontrol;

import fr.sacbruine.messagescontrol.enums.MessageMode;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //region Join
    public static final ModConfigSpec.BooleanValue enableJoinMessage =
            BUILDER.comment("Enable the message when a player joins")
                    .comment("true = message shown, false = message hidden")
                    .define("enableJoinMessage", true);

    public static final ModConfigSpec.EnumValue<MessageMode> joinMessageMode =
            BUILDER.comment("Type of join message (if enabled)")
                    .comment("VANILLA = default Minecraft message, CUSTOM = use joinCustomMessage")
                    .defineEnum("joinMessageMode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> joinCustomMessage =
            BUILDER.comment("Custom join message (if joinMessageMode = CUSTOM)")
                    .comment("Variables: {player}, {display_name}, {online}, {max}")
                    .comment("Colors: &e = yellow, &a = green, &c = red, &7 = gray, &l = bold...")
                    .define("joinCustomMessage", "&e{display_name} joined the game ({online}/{max})");
    //endregion

    //region First Join
    public static final ModConfigSpec.BooleanValue enableFirstJoinMessage =
            BUILDER.comment("Enable a special message when a player joins the server for the first time")
                    .comment("Minecraft has no default first join message, so firstJoinCustomMessage is always used when enabled")
                    .comment("true = firstJoinCustomMessage shown, false = regular join message used (see join settings)")
                    .define("enableFirstJoinMessage", false);

    public static final ModConfigSpec.ConfigValue<String> firstJoinCustomMessage =
            BUILDER.comment("Custom first join message (if enableFirstJoinMessage = true)")
                    .comment("Variables: {player}, {display_name}, {online}, {max}")
                    .comment("Colors: &e = yellow, &a = green, &c = red, &7 = gray, &l = bold...")
                    .define("firstJoinCustomMessage", "&e{display_name} joined the game for the first time ({online}/{max})");
    //endregion

    //region Leave
    public static final ModConfigSpec.BooleanValue enableLeaveMessage =
            BUILDER.comment("Enable the message when a player leaves")
                    .comment("true = message shown, false = message hidden")
                    .define("enableLeaveMessage", true);

    public static final ModConfigSpec.EnumValue<MessageMode> leaveMessageMode =
            BUILDER.comment("Type of leave message (if enabled)")
                    .comment("VANILLA = default Minecraft message, CUSTOM = use leaveCustomMessage")
                    .defineEnum("leaveMessageMode", MessageMode.VANILLA);

    public static final ModConfigSpec.ConfigValue<String> leaveCustomMessage =
            BUILDER.comment("Custom leave message (if leaveMessageMode = CUSTOM)")
                    .comment("Variables: {player}, {display_name}, {online}, {max}")
                    .comment("Colors: &e = yellow, &a = green, &c = red, &7 = gray, &l = bold...")
                    .define("leaveCustomMessage", "&e{display_name} left the game ({online}/{max})");
    //endregion

    static final ModConfigSpec SPEC = BUILDER.build();
}
