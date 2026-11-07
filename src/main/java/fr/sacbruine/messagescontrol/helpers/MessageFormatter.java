package fr.sacbruine.messagescontrol.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.Map;

/**
 * Builds a chat Component from a config template.
 * - "&e", "&l"... become real styles (not raw § codes), so they also apply to the variables after them
 * - "{name}" is replaced by the matching Component. A color/format set in the template overrides its own,
 *   its hover is always kept. Use "&r" before a variable to keep its vanilla color.
 *   Unknown variables are left as is.
 */
public final class MessageFormatter {

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
