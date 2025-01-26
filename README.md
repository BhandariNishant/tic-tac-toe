
## Tic-Tac-Toe App Overview

### Project Information

**Project Name**: Tic-Tac-Toe App  

### App Functionality
This is a classic game Tic-Tac-Toe. Our app supports both single-player and multi-player modes.
It utilises Firebase for authentication of users and for creating a seamless multiplayer game between
two different players. The game saves user statistics and displays them on the Dashboard.

### Known Bugs
No back press on login fragment and dashboard fragment. 
Should not logout during an ongoing game. 

### Task 1: Sign in Screen and Dashboard
- The user must enter an email address and a password. Firebase Authentication is used to validate the 
user and if the password is incorrect, or in invalid format, it is shown using toast messages.
- On Login, the user is taken to the Dashboard containing a list of open two-player games they can join.
- The floating action button can be used to make a new game.
- The statistics are shown at the top of the screen. These statistics are saved in Firebase Realtime Database everytime the user finishes a game.

### Task 2: Single-Player Mode 
- The user can play against the computer which makes random moves until the game is over.
- At the end, the user is shown a dialog box letting them know of the game result and taken 
back to the dashboard.

### Task 3: Two-Player Mode
- The user can make a new game or join a pre-existing game.
- We have created different fragments and view models for both modes of the game for simplicity.
- The player who makes the game is assigned first player and the next player who joins is assigned second player.
- The game locks itself after the second player joins and noone can join after that. 
- The players can forfeit the game or play till a result is declared which will be reflected on each of their stats.

### Task 4: Enhancing Accessibility

- Utilized TalkBack service for accessibility testing, ensuring audible guidance and screen element identification.
- Employed Accessibility Scanner, identifying issues like text contrast, image color contrast, and lack of speakable text. 

### How to host and run the app

To host and run a Java Android app with Firebase authentication and a real-time database, follow these steps:

1. *Firebase Setup:*
    - [Firebase Console](https://console.firebase.google.com/).
    - Create a new Firebase project.
    - Add your Android app to the project:
        - Click on the Android icon to add an app.
        - Enter your app's package name.
        - Download the `google-services.json` file and put in your app directory. You may re-download the file if any error persists.

2. *Android Studio Configuration:*
    - Add Firebase SDK to your project:
        - Add the classpath in your project level `build.gradle`:
        ```
          gradle
          buildscript {
          dependencies {
          // Add this line
          classpath 'com.google.gms:google-services:4.4.0' // use the latest version
          }
          }

        ```
    - Add the apply plugin line at the bottom of your app-level `build.gradle`:
    ```
       gradle
       apply plugin: 'com.google.gms.google-services'
   ```

    - Add dependencies for Firebase Authentication and Realtime Database: use latest versions
   ```
    implementation platform('com.google.firebase:firebase-bom:33.6.0')
    implementation("com.google.firebase:firebase-auth")

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation 'com.google.firebase:firebase-analytics'
    
    implementation 'com.google.firebase:firebase-database:21.0.0'
      ```


### e. Testing Strategy and Outcomes

- Didn't adopt a test-driven development approach but used test cases to check the logic of our app later in the development process.
- Conducted instrumented/UI tests.

### f. Time Investment for Completion

- **Total Duration**: 45-50 hours.

### g. Assignment Difficulty 

On a scale from 1 to 10, the assignment's difficulty is rated at **9**.
