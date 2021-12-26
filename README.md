# YoshiPlex Plugin
Main code for my old Minecraft server I made when I was 13 and 14. (2015-2016 ish)

Originally I put code in https://github.com/retrodaredevil/Yoshiplex-Minecraft-Server-Main-Plugin,
but I created this separate repo to add a Gradle build process to this

### Jars distributed in `libs/`
Because many of the APIs I used are not hosted on any maven repositories, added their jar files in the `libs/` folder.

You can see for yourself which ones are there, but here are a few
* LGPL https://github.com/xxmicloxx/NoteBlockAPI
* GNU https://github.com/TigerHix/ScoreboardLib

* https://dev.bukkit.org/projects/connect-four
  * I have modified the Jar file that is contained in the `libs/` folder.
* https://github.com/essentials/Essentials
  * I have modified the Jar file that is contained in the `libs/` folder.


### Useful commands:
* Finding non-ascii characters: `grep -ri --color='auto' -P -n "[\x80-\xFF]" src`