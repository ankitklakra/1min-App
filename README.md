# 1 Minute News App

## Overview

This project is an Android app called "1 Minute News," which allows users to view short news stories within one minute. The app displays a series of images and corresponding descriptions, each representing a news story. Users can swipe through the stories and read brief summaries of current news topics.

Google Playstore link- https://play.google.com/store/apps/details?id=com.startop.a1min

## Features

- Display short news stories with images and descriptions.
- Swipe left or right to navigate between stories.
- Automatic story progression with a 6-second interval per story.
- Pause and resume story progression by tapping on the screen.
- Share the app link via various communication channels.

## Technologies Used

- Android SDK: The app is developed using Java and the Android SDK, making it compatible with Android devices.
- Firebase Firestore: The Firestore database is used to store news stories with their respective metadata, such as image URI, timestamps, and titles.
- Glide: The Glide library is utilized to load and display images from remote URLs efficiently.
- StoriesProgressView: The StoriesProgressView library is used to create a smooth and interactive story viewing experience.

## Getting Started

1. Clone the repository to your local machine.
2. Import the project into Android Studio.
3. Set up the required dependencies and SDK versions if prompted by Android Studio.
4. Ensure you have the necessary Android Virtual Device (AVD) or a physical Android device to run the app.

## How to Use

1. Launch the app on your Android device.
2. The app will retrieve the latest news stories from the Firestore database and display them in a chronological order.
3. Swipe left or right to view different news stories.
4. Each story is accompanied by a short title and description, along with an image relevant to the news topic.
5. The stories will automatically progress every 6 seconds, but you can pause and resume the progression by tapping on the screen.
6. To share the app link with others, tap on the "Share" button and select the desired communication channel.

## Notes

- The app fetches the latest news stories from the Firestore database within a given time range (start and end timestamps) to ensure only relevant news is displayed.
- Cached stories are used to improve app performance and reduce database queries.
- The app utilizes the StoriesProgressView library to provide an engaging and user-friendly interface for navigating through the news stories.

## License

This project is licensed under the [MIT License](LICENSE), granting you the freedom to modify and distribute the code as per the terms of the license.

## Acknowledgments

Special thanks to [jp-shts](https://github.com/jp-shts) for providing the StoriesProgressView library, which significantly enhances the user experience in this app.

## Author

This app is developed by Ankit Kumar Lakra, and it's part of a personal project. Contact information can be found in the project's repository.

## Support

For any inquiries or support related to the app, please create an issue in the project's repository on GitHub.

---

Before running a project please check the build.gradle, proguard-rules.pro, gradle.properties files.
