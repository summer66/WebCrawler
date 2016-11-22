10/31/2016 PA2 submission

Unzip the archive file. The unzipped file should be a project folder named haoyu.webcrawler.
The project folder contains:
  Folders: 
      .idea
      .settings
      bin
      out
      src
  Files:
      .classpath
      .project
      haoyu.webcrawler.iml
      ReadMe.txt
There are 13 .java files in the source code folder(src) and jsoup-1.10.1.jar
      AlertBox.java 
      BFSCrawler.java 
      BruteForce.java 
      DFSCrawler.java
      FiniteAutomata.java
      KeyWordSearcher.java
      KMP.java
      Main.java
      PageParser.java
      RabinKarp.java 
      StrMatcher.java
      test.java
      URLVerifier.java

Import the entire project into Eclipse and run main.java to use the user interface (ignore the test.java which is used by me to test certain functions of the program).
To use jsoup.jar:
For Eclipse:
1. Right click on the project
2. properties
3. Java Build Path
4. Add external jars
5. Find the JAR
6. OK, OK, OK   

I did not test whether my program would still run normally on Eclipse if only importing the java files instead of the entire project.

Tools used to generate this project:
JDK: jdk1.8.0_101
IDE: IntelliJ IDEA Community Edition 2016.2.4
Operating system: Windows 10
User interface was written in JavaFX
Code was successfully run on Eclipse 4.6.0 (Neon). For more information on this version of Eclipse, please see https://www.eclipse.org/eclipse/development/readme_eclipse_4.6.php 

To correctly run the program, please go to PageParser.java and edit the USER_AGENT according to your browser.I used this website to obtain my USER_AGENT:http://www.whoishostingthis.com/tools/user-agent/

Note that the DFS crawler, finite automata, and KMP string matching methods have not been implemented. Please avoid selecting these options when testing the program.

Considerations regarding robots.txt and generation of log file might be added as desirable features in the future.

Reference:
Introduction to Algorithms: third edition
Algorithms: 4th edition



Sample input and output:
Tested on 10/31/2016
Test result 1:
Inputs: 
BFS, Brute Force, http://www.cnn.com, 30, clinton trump
Outputs: 
On UI:
Completed, 30, 14, 
http://www.cnn.com/
http://www.cnn.com/us/
http://money.cnn.com/
http://www.cnn.com/opinions/
http://www.cnn.com/videos/
http://www.cnn.com/specials/us/energy-and-environment/
http://www.cnn.com/specials/politics/washington-politics/
http://www.cnn.com/specials/politics/national-politics/
http://www.cnn.com/specials/politics/world-politics/
http://money.cnn.com/data/markets/
http://money.cnn.com/technology/
http://money.cnn.com/media/
http://money.cnn.com/pf/
http://money.cnn.com/luxury/

In IDE Console:
http://www.cnn.com/: successful match!
http://www.cnn.com/us/: successful match!
http://www.cnn.com/world/: no match found.
http://www.cnn.com/politics/: no match found.
http://money.cnn.com/: successful match!
http://www.cnn.com/opinions/: successful match!
http://www.cnn.com/health/: no match found.
http://www.cnn.com/entertainment/: no match found.
http://www.cnn.com/style/: no match found.
http://www.cnn.com/travel/: no match found.
http://bleacherreport.com/: no match found.
http://www.cnn.com/videos/: successful match!
Other reasons caused failure when accessing http://cnn.it/go2/
http://www.cnn.com/specials/us/crime-and-justice/: no match found.
http://www.cnn.com/specials/us/energy-and-environment/: successful match!
http://www.cnn.com/specials/us/extreme-weather/: no match found.
http://www.cnn.com/specials/space-science/: no match found.
http://www.cnn.com/africa/: no match found.
http://www.cnn.com/americas/: no match found.
http://www.cnn.com/asia/: no match found.
http://www.cnn.com/europe/: no match found.
http://www.cnn.com/middle-east/: no match found.
http://www.cnn.com/specials/politics/2016-election/: no match found.
http://www.cnn.com/specials/politics/washington-politics/: successful match!
http://www.cnn.com/specials/politics/national-politics/: successful match!
http://www.cnn.com/specials/politics/world-politics/: successful match!
http://money.cnn.com/data/markets/: successful match!
http://money.cnn.com/technology/: successful match!
http://money.cnn.com/media/: successful match!
http://money.cnn.com/pf/: successful match!
http://money.cnn.com/luxury/: successful match!

Test result 2:
Inputs:
BFS, Rabin-Karp, http://xkcd.com/353, 25, random CREATIVE

Outputs in UI: 
Completed, 25, 8,
http://xkcd.com/353/
http://xkcd.com/archive/
http://xkcd.com/
http://xkcd.com/1/
http://xkcd.com/352/
http://c.xkcd.com/random/comic/
http://xkcd.com/354/
http://xkcd.com/1753/

In IDE console:
Prime is: 16453
Prime is: 11071
http://xkcd.com/353/: successful match!
http://xkcd.com/archive/: successful match!
http://what-if.xkcd.com/: no match found.
http://blag.xkcd.com/: no match found.
http://store.xkcd.com/: no match found.
http://xkcd.com/about/: no match found.
http://xkcd.com/: successful match!
http://xkcd.com/1/: successful match!
http://xkcd.com/352/: successful match!
http://c.xkcd.com/random/comic/: successful match!
http://xkcd.com/354/: successful match!
Wrong content type: http://xkcd.com/rss.xml/
Wrong content type: http://xkcd.com/atom.xml/
http://threewordphrase.com/: no match found.
http://www.smbc-comics.com/: no match found.
http://www.qwantz.com/: no match found.
http://oglaf.com/: no match found.
http://www.asofterworld.com/: no match found.
http://buttersafe.com/: no match found.
http://pbfcomics.com/: no match found.
http://questionablecontent.net/: no match found.
http://www.buttercupfestival.com/: no match found.
http://www.mspaintadventures.com/?s=6&p=001901/: no match found.
http://www.jspowerhour.com/: no match found.
http://creativecommons.org/licenses/by-nc/2.5/: no match found.
http://xkcd.com/license.html/: no match found.
http://xkcd.com/1753/: successful match!
