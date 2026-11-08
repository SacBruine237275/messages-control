package fr.sacbruine.messagescontrol.helpers;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import fr.sacbruine.messagescontrol.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds a chat Component from a config template.
 * - "&e", "&l"... become real styles (not raw § codes), so they also apply to the variables after them
 * - "{name}" is replaced by the matching Component. A color/format set in the template overrides its own,
 *   its hover is always kept. Use "&r" before a variable to keep its vanilla color.
 *   Unknown variables are left as is.
 * - The value of an aliased variable is replaced by its entry in the [aliases] config, if any
 */
public final class MessageFormatter {

    private static final String DEFAULT_NAMESPACE = "minecraft:";

    // Only variables with a known set of values: a player named "task" must not be renamed by a "task" alias
    private static final Set<String> ALIASED_VARIABLES = Set.of("dimension", "type", "advancement_id");

    public static MutableComponent format(String template, Map<String, Component> variables) {
        MutableComponent result = Component.empty();
        StringBuilder text = new StringBuilder();
        Style style = Style.EMPTY;

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);

            // Color / format code: &e, &l, &r...
            if (c == '&' && i + 1 < template.length()) {
                ChatFormatting formatting = ChatFormatting.getByCode(Character.toLowerCase(template.charAt(i + 1)));
                if (formatting != null) {
                    appendText(result, text, style);
                    style = applyFormatting(style, formatting);
                    i++;
                    continue;
                }
            }

            // Variable: {player}, {advancement}...
            if (c == '{') {
                int end = template.indexOf('}', i);
                if (end != -1) {
                    String name = template.substring(i + 1, end);
                    Component value = variables.get(name);
                    if (value != null) {
                        if (ALIASED_VARIABLES.contains(name)) {
                            value = applyAlias(value);
                        }
                        appendText(result, text, style);
                        // The template style wins (color, bold...), the variable keeps the rest (hover, vanilla color if none set)
                        result.append(value.copy().setStyle(style.applyTo(value.getStyle())));
                        i = end;
                        continue;
                    }
                }
            }

            text.append(c);
        }

        appendText(result, text, style);
        return result;
    }

    // Translation args are usually Components, but can be plain Strings/numbers
    public static Component toComponent(Object arg) {
        return arg instanceof Component component ? component : Component.literal(String.valueOf(arg));
    }

    // Real username: the display name's insertion holds it (the display name itself can be a nickname/team prefix)
    public static String playerName(Component displayNameComponent) {
        String insertion = displayNameComponent.getStyle().getInsertion();
        return insertion != null ? insertion : displayNameComponent.getString();
    }

    private static Component applyAlias(Component value) {
        UnmodifiableConfig aliases = Config.aliases.get();
        String text = value.getString();
        // List path: a '.' in an id must not be read as a sub-section
        Object alias = aliases.get(List.of(text));
        if (alias == null && text.startsWith(DEFAULT_NAMESPACE)) {
            alias = aliases.get(List.of(text.substring(DEFAULT_NAMESPACE.length())));
        }
        return alias != null ? Component.literal(String.valueOf(alias)).setStyle(value.getStyle()) : value;
    }

    private static void appendText(MutableComponent result, StringBuilder text, Style style) {
        if (!text.isEmpty()) {
            result.append(Component.literal(text.toString()).setStyle(style));
            text.setLength(0);
        }
    }

    // Same behaviour as vanilla § codes: a color resets the formats, &r resets everything
    private static Style applyFormatting(Style style, ChatFormatting formatting) {
        if (formatting == ChatFormatting.RESET) {
            return Style.EMPTY;
        }
        TextColor color = TextColor.fromLegacyFormat(formatting);
        if (color != null) {
            return Style.EMPTY.withColor(color);
        }
        return style.applyFormat(formatting);
    }
}
