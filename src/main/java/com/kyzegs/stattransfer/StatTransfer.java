package com.kyzegs.stattransfer;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class StatTransfer extends JavaPlugin {
	int sftpPort, taskDelay;
	String sftpHost, sftpUser, sftpPassword, sftpRemoteDir;

	private BukkitTask objBukkitTaskInstance;

	private final Runnable objBukkitRunnableStatTransfer = new Runnable() {
		@Override
		public void run() {
			try {
				StatTransferUtil.transfer();
			} catch (JSchException | SftpException e) {
				getLogger().warning(getString("log.JSchSftpExceptionWarning"));
				e.printStackTrace();
			} finally {
				objBukkitTaskInstance.cancel();
				objBukkitTaskInstance = getServer().getScheduler()
						.runTaskLater(StatTransfer.getPlugin(StatTransfer.class), objBukkitRunnableStatTransfer,
								taskDelay);
			}
		}
	};

	@Override
	public void onEnable() {
		saveDefaultConfig();

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		try {
			getConfig().load(getDataFolder() + "/config.yml");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		try {
			taskDelay = getConfig().getInt("transfer-task-delay", 6000);
			sftpHost = getConfig().getString("sftp.host");
			sftpPort = getConfig().getInt("sftp.port");
			sftpUser = getConfig().getString("sftp.user");
			sftpPassword = getConfig().getString("sftp.password");
			sftpRemoteDir = getConfig().getString("sftp.remote-dir");
			objBukkitTaskInstance = getServer().getScheduler().runTaskLater(this, objBukkitRunnableStatTransfer,
					taskDelay);
		} catch (final Exception e) {
			e.printStackTrace();
			getPluginLoader().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
	}

	public String getString(String key) {
		try {
			return ResourceBundle
					.getBundle("com.kyzegs.stattransfer.locale."
							+ getConfig().getString("language", "english").toLowerCase())
					.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
