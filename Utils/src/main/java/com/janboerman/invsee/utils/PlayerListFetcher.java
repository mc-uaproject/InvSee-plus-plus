package com.janboerman.invsee.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PlayerListFetcher implements PluginMessageListener {
    private final JavaPlugin plugin;
    private final Player player;
    private final CompletableFuture<String[]> future;

    public PlayerListFetcher(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.future = new CompletableFuture<>();

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
    }

    public CompletableFuture<String[]> getPlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

        return future;
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerList")) {
            in.readUTF();
            future.complete(in.readUTF().split(", "));

            plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", this);
        }
    }
}
