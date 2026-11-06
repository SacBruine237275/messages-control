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
