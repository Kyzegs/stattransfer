package com.kyzegs.stattransfer;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;

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

		final ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
		channelSftp.connect();

		// Root path is consistent
		final String rootPath = Bukkit.getServer().getWorldContainer().getAbsolutePath();

		for (final World world : Bukkit.getServer().getWorlds()) {
			// We skip worlds with _nether and _the_end at the end of their names
			if (!world.getName().endsWith("_nether") && !world.getName().endsWith("_the_end")) {
				final File statsPath = new File(rootPath, world.getName() + "/stats");
				if (statsPath.exists() && statsPath.listFiles().length >= 0) {
					for (final File f : statsPath.listFiles()) {
						// We determine the directory based on config 'create'worldname-subdirs'
						// variable
						final String dirString = plugin.sftpRemoteDir
								+ (plugin.getConfig().getBoolean("sftp.create-worldname-subdirs", false)
										? world.getName() + "/"
										: "");
						try {
							// We try to make the directory on the SFTP server
							channelSftp.mkdir(dirString);
						} catch (final SftpException e) {
							// We ignore this because if the directory exists this is an error we'll get
							// every time
						}
						channelSftp.put(f.getAbsolutePath(), dirString + f.getName());
					}
				} else {
					Bukkit.getServer().getLogger()
							.info(plugin.getString("log.emptyTransfer").replaceFirst("%world", world.getName()));
				}
			}
		}
		channelSftp.exit();
		jschSession.disconnect();
	}
}
