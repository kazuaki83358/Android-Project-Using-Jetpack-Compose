pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*") // Includes Android plugin groups
                includeGroupByRegex("com\\.google.*") // Includes Google plugin groups
                includeGroupByRegex("androidx.*")    // Includes AndroidX plugin groups
            }
        }
        mavenCentral() // For general-purpose dependencies
        gradlePluginPortal() // For Gradle plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Enforces centralized repository usage
    repositories {
        google()       // Google's Maven repository for Android dependencies
        mavenCentral() // Central Maven repository for general dependencies
    }
}

// Sets the name of the root project
rootProject.name = "Firebase-firestore-practice"

// Includes the app module in the project
include(":app")
