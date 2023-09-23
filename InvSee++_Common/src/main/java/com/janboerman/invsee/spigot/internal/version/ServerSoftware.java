package com.janboerman.invsee.spigot.internal.version;

import org.bukkit.Server;

import static com.janboerman.invsee.spigot.internal.version.MinecraftPlatform.*;
import static com.janboerman.invsee.spigot.internal.version.MinecraftVersion.*;

import java.util.Objects;

public class ServerSoftware {

    //only list supported craftbukkit versions here:
    public static final ServerSoftware
            CRAFTBUKKIT_1_8_8 = new ServerSoftware(CRAFTBUKKIT, _1_8_8),
            CRAFTBUKKIT_1_12_2 = new ServerSoftware(CRAFTBUKKIT, _1_12_2),
            CRAFTBUKKIT_1_15_2 = new ServerSoftware(CRAFTBUKKIT, _1_15_2),
            CRAFTBUKKIT_1_16_5 = new ServerSoftware(CRAFTBUKKIT, _1_16_5),
            CRAFTBUKKIT_1_17_1 = new ServerSoftware(CRAFTBUKKIT, _1_17_1),
            CRAFTBUKKIT_1_18_2 = new ServerSoftware(CRAFTBUKKIT, _1_18_2),
            CRAFTBUKKIT_1_19_4 = new ServerSoftware(CRAFTBUKKIT, _1_19_4),
            CRAFTBUKKIT_1_20_1 = new ServerSoftware(CRAFTBUKKIT, _1_20_1),
            CRAFTBUKKIT_1_20_2 = new ServerSoftware(CRAFTBUKKIT, _1_20_2),
            GLOWSTONE_1_8_8 = new ServerSoftware(GLOWSTONE, _1_8_8),
            GLOWSTONE_1_8_9 = new ServerSoftware(GLOWSTONE, _1_8_9),
            GLOWSTONE_1_12_2 = new ServerSoftware(GLOWSTONE, _1_12_2);

    private MinecraftPlatform platform;
    private MinecraftVersion version;

    public ServerSoftware(MinecraftPlatform platform, MinecraftVersion version) {
        this.platform = platform;
        this.version = version;
    }

    public static ServerSoftware detect(final Server server) {
        final String serverClassName = server.getClass().getName();
        switch (serverClassName) {
            case "org.bukkit.craftbukkit.v1_8_R3.CraftServer":
                return CRAFTBUKKIT_1_8_8;
            case "org.bukkit.craftbukkit.v1_12_R1.CraftServer":
                return CRAFTBUKKIT_1_12_2;
            case "org.bukkit.craftbukkit.v1_15_R1.CraftServer":
                return CRAFTBUKKIT_1_15_2;
            case "org.bukkit.craftbukkit.v1_16_R3.CraftServer":
                return CRAFTBUKKIT_1_16_5;
            case "org.bukkit.craftbukkit.v1_17_R1.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_17: return new ServerSoftware(CRAFTBUKKIT, _1_17);
                    case CraftbukkitMappingsVersion._1_17_1: return CRAFTBUKKIT_1_17_1;
                }
            case "org.bukkit.craftbukkit.v1_18_R1.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_18: return new ServerSoftware(CRAFTBUKKIT, _1_18);
                    case CraftbukkitMappingsVersion._1_18_1: return new ServerSoftware(CRAFTBUKKIT, _1_18_1);
                }
            case "org.bukkit.craftbukkit.v1_18_R2.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_18_2: return CRAFTBUKKIT_1_18_2;
                }
            case "org.bukkit.craftbukkit.v1_19_R1.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_19: return new ServerSoftware(CRAFTBUKKIT, _1_19);
                    case CraftbukkitMappingsVersion._1_19_1: return new ServerSoftware(CRAFTBUKKIT, _1_19_1);
                    case CraftbukkitMappingsVersion._1_19_2: return new ServerSoftware(CRAFTBUKKIT, _1_19_2);
                }
            case "org.bukkit.craftbukkit.v1_19_R2.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_19_3: return new ServerSoftware(CRAFTBUKKIT, _1_19_3);
                }
            case "org.bukkit.craftbukkit.v1_19_R3.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_19_4: return CRAFTBUKKIT_1_19_4;
                }
            case "org.bukkit.craftbukkit.v1_20_R1.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_20: return new ServerSoftware(CRAFTBUKKIT, _1_20);
                    case CraftbukkitMappingsVersion._1_20_1: return CRAFTBUKKIT_1_20_1;
                }
            case "org.bukkit.craftbukkit.v1_20_R2.CraftServer":
                switch (CraftbukkitMappingsVersion.getMappingsVersion(server)) {
                    case CraftbukkitMappingsVersion._1_20_2: return CRAFTBUKKIT_1_20_2;
                }

            case "net.glowstone.GlowServer":
                final String glowstoneGameVersion = GlowstoneGameVersion.getGameVersion();
                switch (glowstoneGameVersion) {
                    case GlowstoneGameVersion._1_8_8: return GLOWSTONE_1_8_8;
                    case GlowstoneGameVersion._1_8_9: return GLOWSTONE_1_8_9;
                    case GlowstoneGameVersion._1_12_2: return GLOWSTONE_1_12_2;
                    default: return new ServerSoftware(GLOWSTONE, MinecraftVersion.fromString(glowstoneGameVersion));
                }
        }

        if (serverClassName.matches("org\\.bukkit\\.craftbukkit\\.v((.?)*)\\.CraftServer")) {
            return new ServerSoftware(CRAFTBUKKIT, null);
        }

        return null;
    }

    @Override
    public String toString() {
        return platform + " version " + version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerSoftware)) return false;

        ServerSoftware that = (ServerSoftware) o;
        return this.platform == that.platform && this.version == that.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(platform, version);
    }

    public MinecraftPlatform getPlatform() {
        return platform;
    }

    public MinecraftVersion getVersion() {
        return version;
    }

}