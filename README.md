# User Management Application

## Overview

This Android application demonstrates a simple user management system using the MVVM (Model-View-ViewModel) architectural pattern.
The app fetches user data from a RESTful API, stores it locally using the Room persistence library, and displays it in a RecyclerView. 
It supports CRUD operations and includes enhancements for improved UI/UX.

## Features

- Fetch user data from the ReqRes API.
- Store user data locally using Room database.
- Display user data in a list with RecyclerView and CardView.
- Perform CRUD operations:
    - Add a new user.
    - Update existing user details.
    - Delete a user.
- Enhanced UI/UX with custom views and improved styling.

## Architecture

The application follows the MVVM architectural pattern with the following components:

- **Model**: Manages data and business logic.
    - `UserEntity`: Data model representing a user.
    - `UserRepository`: Handles data operations and API interactions.

- **View**: Handles the user interface and user interactions.
    - `MainActivity`: Main activity displaying the user list.

- **ViewModel**: Acts as an intermediary between the View and Model.
    - `UserViewModel`: Manages UI-related data and communicates with the repository.

## Setup and Installation

### Prerequisites

- Android Studio 4.0 or higher.
- Java Development Kit (JDK) 8 or higher.

### Building and Running

1. **Clone the Repository**

   ```bash
   git clone https://github.com/matanew1/EasySaleAssignment.git

2. **Open the Project in Android Studio**
   - Launch Android Studio.
   - Click on "Open an Existing Project" and navigate to the cloned repository.
   - Wait for the project to sync and build.
   - 
3. **Configure the Emulator or Connect a Device**
   - Set up an Android emulator or connect a physical Android device.
   - Ensure that the device has internet access.

4. **Build and Run the Application**
   - Click on the "Run" button or use Shift + F10.
   - Select your device/emulator and wait for the application to deploy.

5. **Explore the Features**
   - The app should launch on your device/emulator.
   - You can now explore the user management features, including fetching users from the ReqRes API, viewing user lists, and performing CRUD operations.

*Note*
- Ensure that your device or emulator has internet access to fetch data from the ReqRes API.
- The Room database is used for local data storage, so even without internet, you can still view and manage users stored locally.
