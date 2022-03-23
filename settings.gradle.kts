dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Handlebar"

include(":sample")
include(":handlebar")
include(":handlebar-ksp")
include(":handlebar-annotation")
