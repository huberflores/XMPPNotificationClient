XMPP Android Client for JSON-based notifications
================================================

Android client that handles notifications which are sent from the [XMPP Notification Server](https://github.com/huberflores/XMPPNotificationServer.git). The application stores the notifications within a SQLite database along with the battery level of the device


Installation
------------

- Import code into Eclipse

- Install application in your device



Configuration
-------------

- Host/Port = Openfire IP/5222

- Service = your XMPP domain

- JID credentials (username/password)



FAQ
----

Common error: java.lang.NoClassDefFoundError: org.jivesoftware.smack.ConnectionConfiguration

[SOLVED]
smack.jar is not included within your dependencies when you installed the apk in the device. Try

```xml

right click project -> properties -> java build path -> order and export -> check smack.jar 

```




