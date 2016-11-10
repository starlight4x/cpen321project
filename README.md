﻿Time Your Trip Application
This application is still in development. It will be improved and added more features.


What is Time Your Trip Application
This Android mobile application notifies bus passengers when they are arriving at their destination stop. It allows the user to spend the bus ride doing other activities, such as studying and sleeping, without having to constantly check the current stop and without fear of missing their destination stop. 
The app operates such that when the user gets on a bus, he or she will enter the starting stop he/she at, then the app will allow the user to choose the bus number and the destination stop. After that, it accesses real-time TransLink information to retrieve the estimated time it will take for the user to reach the chosen stop. Then, the app will set an alarm based on the retrieved time. 


How to find our source code:
Our source code is on a  public github repository, the following is link to Time Your Trip Application:
https://github.com/Farwhyn/cpen321project 


How to check it out:
This procedure assumes you have already created a repository on GitHub, or have an existing repository owned by someone else you'd like to contribute to.
1. On GitHub, navigate to the main page of the repository. Clone or download button 
2. Under your repository name, click Clone or download.
 Clone URL button 

   1. In the Clone with HTTPs section, click  to copy the clone URL for the repository.
   2. Open Git Bash.
   3. Change the current working directory to the location where you want the cloned directory to be made.
   4. Type git clone, and then paste the URL you copied in Step 2.
   5. git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY

   6. Press Enter. Your local clone will be created.
   7. git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY
Cloning into `Spoon-Knife`...
remote: Counting objects: 10, done.
remote: Compressing objects: 100% (8/8), done.
remove: Total 10 (delta 1), reused 10 (delta 1)
Unpacking objects: 100% (10/10), done.
   8. The code is under “PlanMyTrip” folder.
How to build the project:
This procedure assumes that you have already installed Android Studio.
There are two options for building the project:
      * Through an emulator:
To start an Android emulator such as the default emulator installed in RAD Studio:
      1. Tools->android ->Start the Android SDK Manager (select Start. ...
      2. In the Android SDK Manager, click the Tools menu and select Manage AVDs.
      3. In the Android Virtual Device Manager, select the emulator and click Start.


The app will be uploaded automatically and it can be tested starting by providing whatever expected.


      * Through an actual android phone


How to run tests:
After open the project, switch to the Project first.
 Screen Shot 2016-11-10 at 09.55.50.png 

There are two folders that contain different tests for our application.
      1. In the project source code, go to PlanMyTrip\app\src;
      2. Under the “andoridTest” folder, click on “java”, then the “com.example ….” folder, where the Application tests are;
      3. To run the application tests, click “Run” or right click on the file and select “run ‘ApplicationTest’”. Then Android Studio should open the emulator;
      4. Choose a virtual device. If don’t have any 
      * Click on the “create new virtual device” button;
      * Under the “Category”, select “Phone”; 
      * Choose a device then click on “Next”;
      * Click on “Next” on the following windows then, at last, click on “Finish”;
      * Wait for it to be created then it should be seen on the window.
      1. Click “OK”
      2. The result will show at the bottom “Run:” console.


      1. In the project source code, go to PlanMyTrip\app\src;
      2. Under the “test” folder, click on the “java” then “com.example...”;
      3. To run the unit tests, right click on the file and select “run ‘NameofTheTests’” ;
      4. The result should also shown on the bottom “Run:” console.
 You can always switch back to test the application by clicking on the dropdown list and choose app as shown in the following picture:
 Screen Shot 2016-11-10 at 00.22.21.png 



Structure of the source code directory:
Open the project on Android Studio , then:
On the top left side, click on the app folder
Select the java subfolder
Select com.example.johan.planmytrip where all the activities are:
 Capture11.PNG 

alarmTimer: this activity is responsible for turning on/off the alarm 
Bus:
ConnectDatabase:
DatabaseAccess:
DatabaseOpenHelper:
finalQuery
GPSHandler
MainActivity: this activity is responsible for the launcher page, where the user is expected to enter the bus stop number he/she is at
NextBusesAdapter: this activity is to generate the list view of the buses provided to the user after he entered the starting bus stop
Stop
TranslinkHandler
TranslinkUI


Design patterns used: 
      * Observer pattern
        As for the whole system, UI reflects the change of the states of the APP. Every time the state changes, it updates.