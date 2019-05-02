# Welcome
This repo is a modified version of Grey Havens' bang-game(https://github.com/greyhavens/bang-game).
It differs in the following ways, some of which might seem oddly specific because it was used in a private multiplayer setting:

• Added `indianserver.bin` which launches the ITP server; can be used in conjunction with FT's `bangserver.bin`

• Chat whitelist disabled.

• Age restriction disabled for testing.

• Indian Trading Post is freely accessible.

• FG bad board removal: Removed unbalanced boards from FG.

• Bonus points in competitive matches are halved (10 --> 5)

• Added some card packs to the general store; lowered price of 13 and 52 card packs to keep them worth buying.


## Amendments
In this space are amendments to Grey Havens' Readme: Ultimately, follow along with theirs (below) but 
keep these amendments in mind.

When editing `etc/server.properties`, on line 33 you will see this:
```
server_root = /export/bang
```
Change `/export/bang` to the full filepath of wherever you stuck your dylansbang folder, 
mine for example is `/home/dylan/dylansbang`.

Similarly, in `assets/build.gradle` you need the full filepaths in lines 90 & 91 here:
```
  ant.metabundle(actiondef: "/home/USER/dylansbang/assets/rsrc/avatars/actions.xml", 
				 classdef: "/home/USER/dylansbang/assets/rsrc/avatars/classes.xml",
```
If you run 'gradle deploy' at this point and `assets/update_lists` or another script is giving you a permission denied error, you need to make the 
script executable by running: ```chmod +x assets/update_lists```

For a deployment that runs both the FT server and ITP server synchronously, make these changes to your `server.properties`:
Remove the #'s from in front of lines 13,14
```
indian_post.town_id = indian_post
indian_post.server_host = localhost
```
and 27

```server_secret = Is it secret? Is it safe?```

& in `deployment.properties`, 

copy and paste this block into lines 12-21 (port 47625 for ITP worked for me):

```
server_ports = 47624, 47625

#
# The public hostname and ports for the various town lobby servers.

frontier_town.server_host = localhost
frontier_town.server_ports = 47624

indian_post.server_host = localhost
indian_post.server_ports = 47625
```
Then just run indianserver.bin in addition to bangserver.bin as instructed below. 
(You can run `./bangserver | ./indianserver` to run them in the same instance)
 
Everything below this point, including the license, was written by the good folks at Grey Havens.
# Bang! Howdy Pardner

Herein lies the source code to the game Bang! Howdy. This is a game that was created by Three Rings
Design in 2004 and which is still limping along at http://www.banghowdy.com/

A while ago, I started a crazy project to port the game to the [libGDX] game library so that I
could run it on an iPad and in a web browser, but I never really had much time, so that project is
very very far from completion.

The current source code mostly builds, so if you're a super savvy Java programmer, you could
probably get the client and server running locally if you feel like doing some spelunknig into code
and configuration files. If you're just a tourist, you're not going to get very far, so just enjoy
looking at the code and media and please don't pester me with n00b requests about not being able to
get this or that to compile. If you can't get things to compile, there's no way you're going to be
able to do anything meaningful with the code. You ain't gonna get from your house to the top of
Mount Everest if you don't even know how to read a map.

With that nay saying nelly warning out of the way, here are some basic build instructions.

## Building and running

The game is (now) built with [Gradle], so you'll need that installed, and then you can run:

```
gradle deploy
```

which will build everything, process all the resources and prepare things to be run locally.

Assuming that worked, you'll have a `build/client` and `build/server/` directory with a bunch of
jar files in them (among a zillion other directories).

Next you need to copy all the `etc/*.dist` files into `etc/test/` without the `.dist` suffix. For
example, `etc/deployment.properties.dist` gets copied to `etc/test/deployment.properties`. In
theory you can edit those files to tweak the settings for your test deployment, but the defaults
are fine for now so just copy and rename.

You can now run the client, even though it will have no one to talk to:

```
./bin/bangclient
```

That should show you something like this:

![Client screenshot](lib/client-screenshot.jpg)

Now you have the more complex task of setting up the server. When I said above that you don't need
to edit the files in `etc/test/*`, I lied. You have to edit `etc/test/server.properties` and
configure a username and password for a MySQL database which must be running somewhere (I recommend
on your local machine), which the Bang! server can use to store data.

There will be a section that looks like this:

```
#
# The default database mapping; all other definitions will inherit from
# this and need only be specified in cases where they differ from the
# defaults

db.default.driver = com.mysql.jdbc.Driver
db.default.url = jdbc:mysql://DBHOST:3306/bang
db.default.username = USERNAME
db.default.password = PASSWORD

# These overrides are needed for the OOO user database
db.userdb.url = jdbc:mysql://USERDBHOST:3306/ooouser
db.sitedb.url = jdbc:mysql://USERDBHOST:3306/ooouser
```

You need to change `DBHOST`, `USERNAME`, `PASSWORD` to the appropriate values for your MySQL server
(and you can set `USERDBHOST` to the same value as `DBHOST` and keep that on the same server). Then
you need to create a `bang` and `ooouser` database on your MySQL server (or change those names to
database names that you prefer and which you have created).

Then you can run the server like so:

```
./bin/bangserver
```

The first time you run it, it will create a bunch of database tables and eventually it should say
something along the lines of:

```
2016/12/17 18:32:40:993 INFO com.threerings.bang: Running in cluster mode as node 'frontier_town'.
2016/12/17 18:32:41:028 INFO com.threerings.bang: Bang server v0 initialized.
2016/12/17 18:32:41:029 INFO com.threerings.presents: DOMGR running.
2016/12/17 18:32:41:045 INFO com.threerings.narya: Server listening on 0.0.0.0/0.0.0.0:47624.
```

If so, you are tantalizingly close to logging into your own local Bang! Howdy instance. The only
final problem is that there are no users in the user database, so there's no one to log in as.

Not to worry, there's a gradle target that will create a test user in your pristine empty
database. Just run:

```
gradle server:createTestUser
```

And now you can log into your local server with username `test` and password `yeehaw`.

That's about it. You can now hack on your own private Bang! Howdy instance and implement all those
features you always wanted. Good luck!

## License

The Bang! Howdy source code is released under a BSD license. The Bang! Howdy media, 3D models,
texture images, UI images, sound files, everything other than the code, is released under the
Creative Commons [Attribution-NonCommercial 3.0] license.

[libGDX]: https://libgdx.badlogicgames.com/
[Gradle]: https://gradle.org/
[Attribution-NonCommercial 3.0]: https://creativecommons.org/licenses/by-nc/3.0/us/
