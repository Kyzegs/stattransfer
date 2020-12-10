# StatTransfer
Uploads player statistics from your Minecraft server to a remote SFTP server

This plugin does no actual stat tracking on its own. Instead, it siphons data from the 'stats' directory in the world folder.
### config.yml
```yml
language: english
transfer-task-delay: 6000 # in server ticks. Default is 5 minutes
sftp:
   host: localhost # The domain or IP that the plugin should attempt to upload to
   port: 22 # The port on which your SFTP server is listening
   user: username # The username for your SFTP server
   password: password # The password for your SFTP server
   remote-dir: directory # The directory on the SFTP server to which the plugin should upload the player's statistics
   create-worldname-subdirs: false # Whether or not to make a new sub-directory on the SFTP server for each world's stats. This defaults to FALSE to be backwards compatible with previous iterations, as well as it's only necessary if you run a multi-world Minecraft server
```

### Localization
This plugin supports the following languages:

 - English - Original language
 - Dutch - [Kyzegs](https://github.com/Kyzegs)
 - Bulgarian - [WolfNT90](https://github.com/WolfNT90)
 - Spanish - [nynxus](https://twitter.com/nynxus)
