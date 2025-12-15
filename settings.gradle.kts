rootProject.name = "ExampleProject"

include("plugin")
include("project:core")

pluginManagement {
    repositories {
        maven("https://repo.hiusers.com/releases")
        gradlePluginPortal()
        mavenCentral()
    }
}