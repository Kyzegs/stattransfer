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
		JSch jsch = new JSch();

		Session jschSession = jsch.getSession(plugin.sftpUser, plugin.sftpHost, plugin.sftpPort);
		jschSession.setPassword(plugin.sftpPassword);
		jschSession.connect();

		ChannelSftp channelSftp = (ChannelSftp)jschSession.openChannel("sftp");
		channelSftp.connect();

		String rootPath = Bukkit.getServer().getWorldContainer().getAbsolutePath();
		File statsPath = new File(rootPath, "world/stats");
		for (File f : statsPath.listFiles()) {
			channelSftp.put(f.getAbsolutePath(), plugin.sftpRemoteDir + f.getName());
		}

		channelSftp.exit();
		jschSession.disconnect();
	}
}
