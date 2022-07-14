package be.zvz.kookie.permission;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class BanList {
    private @NotNull
    final
    String file;
    private @NotNull
    HashMap<String, BanEntry> list = new LinkedHashMap<>();
    private boolean enabled = true;

    public BanList(@NotNull String file) {
        this.file = file;
    }

    public BanList(@NotNull Path p) {
        this(p.toString());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @Nullable
    BanEntry getEntry(@NotNull String name) {
        this.removeExpired();
        return this.list.get(name.toLowerCase());
    }

    public boolean isBanned(@NotNull String name) {
        name = name.toLowerCase();
        if (!this.isEnabled()) {
            return false;
        }
        this.removeExpired();
        return this.list.containsKey(name);
    }

    public void add(@NotNull BanEntry entry) {
        this.list.put(entry.getName(), entry);
    }

    public @NotNull
    BanEntry addBan(@NotNull String target, @Nullable String reason, @Nullable Date expires, @Nullable String source) {
        BanEntry e = new BanEntry(target);
        e.setSource(Optional.ofNullable(source).orElse(e.getSource()));
        e.setExpirationDate(expires);
        e.setReason(Optional.ofNullable(reason).orElse(e.getReason()));
        this.list.put(e.getName(), e);

        this.save();
        return e;
    }

    public void remove(@NotNull String name) {
        this.list.remove(name.toLowerCase());
    }

    public void removeExpired() {
        this.list = (HashMap<String, BanEntry>) this.list.entrySet().stream()
                .filter((entry) -> !entry.getValue().hasExpired())
                .collect(Collectors.toMap(Object::toString, Map.Entry::getValue));
    }

    public void load() {
        try {
            this.list = new LinkedHashMap<>();
            File f = new File(this.file);
            FileUtils.touch(f);
            String[] lines = FileUtils.readFileToString(f, StandardCharsets.UTF_8).trim().split("\n");
            for (String line : lines) {
                if (line.length() > 0 && line.charAt(0) != '#') {
                    @Nullable BanEntry e = BanEntry.Companion.fromString(line);
                    if (e != null) {
                        this.list.put(e.getName(), e);
                    }
                }
            }
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }

    public void save() {
        try {
            this.removeExpired();
            StringBuffer buf = new StringBuffer("# victim name | ban date | banned by | banned until | reason\n\n");
            this.list.forEach((n, e) -> buf.append(e.toString()).append("\n"));
            File f = new File(this.file);
            FileUtils.touch(f);
            FileUtils.writeStringToFile(f, buf.toString(), StandardCharsets.UTF_8);
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
    }
}
