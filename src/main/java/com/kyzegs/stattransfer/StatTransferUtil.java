package com.kyzegs.stattransfer;

import java.io.File;

import org.bukkit.Bukkit;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class StatTransferUtil {
	private static StatTransfer plugin = StatTransfer.getPlugin(StatTransfer.class);

	public static void transfer() throws JSchException, SftpException {
		JSch.setConfig("StrictHostKeyChecking", "no");
		final JSch jsch = new JSch();

		final Session jschSession = jsch.getSession(plugin.sftpUser, plugin.sftpHost, plugin.sftpPort);
		jschSession.setPassword(plugin.sftpPassword);
		jschSession.connect();

		final ChannelSftp channelSftp = (ChannelSftp)jschSession.openChannel("sftp");
		channelSftp.connect();

		final String rootPath = Bukkit.getServer().getWorldContainer().getAbsolutePath();
		final File statsPath = new File(rootPath, "world/stats");
		if (statsPath.exists() && statsPath.listFiles().length >= 0) {
			for (final File f : statsPath.listFiles()) {
				channelSftp.put(f.getAbsolutePath(), plugin.sftpRemoteDir + f.getName());
			}
		} else {
			plugin.getServer().getLogger().warning("'world/stats' doesn't exist, or nothing to transfer.");
		}

		channelSftp.exit();
		jschSession.disconnect();
	}
}
