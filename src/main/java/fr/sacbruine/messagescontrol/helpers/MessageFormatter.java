package fr.sacbruine.messagescontrol.helpers;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import fr.sacbruine.messagescontrol.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;

import java.util.List;
import java.util.Map;

/**
 * Builds a chat Component from a config template.
 * - "&e", "&l"... become real styles (not raw § codes), so they also apply to the variables after them
 * - "{name}" is replaced by the matching Component. A color/format set in the template overrides its own,
 *   its hover is always kept. Use "&r" before a variable to keep its vanilla color.
 *   Unknown variables are left as is.
 * - A variable's plain text value is replaced by its entry in the [aliases] config, if any
 */
public final class MessageFormatter {

    private static final String DEFAULT_NAMESPACE = "minecraft:";

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
                    Component value = variables.get(template.substring(i + 1, end));
                    if (value != null) {
                        value = applyAlias(value);
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

    // Plain text values only: translated ones ({advancement}, {death_message}) must keep their translation and hover
    private static Component applyAlias(Component value) {
        if (!(value.getContents() instanceof PlainTextContents)) {
            return value;
        }

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
        if (formatting.isColor()) {
            return Style.EMPTY.withColor(formatting);
        }
        return style.applyFormat(formatting);
    }
}
