package cc.phantomhost.core.protocol;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Protocol {
    public @Nullable String getResponse(@NotNull String message);
}
