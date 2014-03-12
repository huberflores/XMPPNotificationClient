XMPP Android Client for JSON-based notifications
=================================================

Android client that handles notifications which are sent from the [XMPP Notification Server](https://github.com/huberflores/XMPPNotificationServer.git). The application stores the notifications within a SQLite database along with the battery level of the device


Installation
-------------

Build source

- Import code into Eclipse

- Install application in your device

Binary

- Install apk file (XMPPClient.apk)


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


How to cite
-----------
The complete framework (Client/Server) was built for comparison purposes with other notification mechanisms. If you are using the tool for your research, please do not forget to cite


- Flores, Huber, and Satish Srirama. ["Mobile cloud messaging supported by XMPP primitives."](http://dl.acm.org/citation.cfm?id=2482983) Proceeding of the fourth ACM workshop on Mobile cloud computing and services. ACM, 2013. In conjunction with, the 11th International Conference on Mobile Systems, Applications and Services (MobiSys 2013).




