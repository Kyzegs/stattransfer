# StatTransfer
Uploads player statistics from your Minecraft server to a remote SFTP server

This plugin does no actual stat tracking on its own. Instead, it siphons data from the 'stats' directory in the world folder.
### config.yml

    language: english
    sftp:
       host: localhost # The domain or IP that the plugin should attempt to upload to
       port: 22 # The port on which your SFTP server is listening
       user: username # The username for your SFTP server
       password: password # The password for your SFTP server
       remote-dir: directory # The directory on the SFTP server to which the plugin should upload the player's statistics


### Localization
This plugin supports the following languages:

 - English - Original language
 - Bulgarian - [WolfNT90](https://github.com/WolfNT90)
 - Spanish - [nynxus](https://twitter.com/nynxus)
