package com.janboerman.invsee.spigot;

import com.janboerman.invsee.spigot.internal.Scheduler;

import java.util.UUID;

public class DefaultScheduler implements Scheduler {

    private final InvseePlusPlus plugin;

    public DefaultScheduler(InvseePlusPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void executeSyncPlayer(UUID playerId, Runnable task, Runnable retired) {
        executeSync(task);
    }

    @Override
    public void executeSyncGlobal(Runnable task) {
        executeSync(task);
    }

    private void executeSync(Runnable task) {
        if (plugin.getServer().isPrimaryThread()) {
            task.run();
        } else {
            plugin.getServer().getScheduler().runTask(plugin, task);
        }
    }

    @Override
    public void executeAsync(Runnable task) {
        if (!plugin.getServer().isPrimaryThread()) {
            task.run();
        } else {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    @Override
    public void executeLaterGlobal(Runnable task, long delayTicks) {
        plugin.getServer().getScheduler().runTaskLater(plugin, task, delayTicks);
    }

}
