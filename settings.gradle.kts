enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "hilt.android" -> useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
                "apollo" -> useModule("com.apollographql.apollo3:apollo-gradle-plugin:${requested.version}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "ComposeGraphQLExample"
include(":app")
include(":feature:common")
include(":feature:main")
include(":feature:search")
include(":feature:setting")
include(":data:graphql")
include(":data:datastore")
include(":data:repository")
include(":domain")
