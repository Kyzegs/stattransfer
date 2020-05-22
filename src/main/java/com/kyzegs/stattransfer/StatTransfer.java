package com.kyzegs.stattransfer;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class StatTransfer extends JavaPlugin {
	int sftpPort;
	String sftpHost, sftpUser, sftpPassword, sftpRemoteDir;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		try {
			getConfig().load(getDataFolder() + "/config.yml");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		sftpHost = getConfig().getString("sftp.host");
		sftpPort = getConfig().getInt("sftp.port");
		sftpUser = getConfig().getString("sftp.user");
		sftpPassword = getConfig().getString("sftp.password");
		sftpRemoteDir = getConfig().getString("sftp.remote-dir");

		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					StatTransferUtil.transfer();
				} catch (JSchException | SftpException e) {
					getLogger().warning("SFTP settings in config.yml are most likely incorrect");
					e.printStackTrace();
					cancel();
				}
			}
		}.runTaskTimer(this, 0, 6000);
	}
}
