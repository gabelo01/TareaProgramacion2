LAUNCHERS — Quick Access
========================

These 4 .bat files open each module of the application directly.

LOCATION
--------
The launchers/ folder must stay inside TareaProgramacion2/
(next to pom.xml), like this:

  TareaProgramacion2/
  ├── pom.xml
  ├── launchers/
  │   ├── Open_Admin.bat
  │   ├── Open_Kiosk.bat
  │   ├── Open_Employee.bat
  │   ├── Open_Projection.bat
  │   └── README.txt
  └── src/

USAGE
-----
Double-click any .bat to launch that module.
A console window will appear showing build progress,
then the application window opens.
The console closes automatically when you close the app.

DESKTOP SHORTCUT
----------------
1. Right-click the .bat file → Create shortcut
2. Move the shortcut to your Desktop
3. Right-click the shortcut → Properties → Change Icon (optional)

NOTE
----
The launchers use the Maven and Java bundled with NetBeans:
  C:\Program Files\Apache NetBeans\java\maven
  C:\Program Files\Apache NetBeans\jdk

If NetBeans is installed in a different location, update the
MAVEN_HOME and JAVA_HOME lines inside each .bat file.


Password admin:1234

Employees pin: 1111, 2222, 1234
