package fr.sacbruine.messagescontrol.helpers;

import net.minecraft.advancements.AdvancementHolder;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayDeque;

public final class AdvancementContext {

    private static final ArrayDeque<AdvancementHolder> advancementStack = new ArrayDeque<>();

    public static void push(AdvancementHolder advancement) {
        advancementStack.push(advancement);
    }

    public static void pop() {
        advancementStack.poll();
    }

    @Nullable
    public static AdvancementHolder current() {
        return advancementStack.peek();
    }
}
