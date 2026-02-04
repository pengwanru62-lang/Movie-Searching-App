// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Ensure all subprojects use the same Kotlin artifacts version as the Kotlin plugin (1.9.22)
subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            // Only force a small set of Kotlin artifacts that are guaranteed to exist at the Kotlin plugin version.
            // Forcing every artifact in group "org.jetbrains.kotlin" caused requests like
            // org.jetbrains.kotlin:kotlin-metadata-jvm to be forced to 1.9.22 (which doesn't exist).
            if (requested.group == "org.jetbrains.kotlin") {
                when (requested.name) {
                    "kotlin-stdlib",
                    "kotlin-stdlib-jdk7",
                    "kotlin-stdlib-jdk8",
                    "kotlin-reflect" -> {
                        useVersion("1.9.22")
                        because("Match core org.jetbrains.kotlin artifacts to plugin version 1.9.22")
                    }
                    // Other kotlin artifacts (for example kotlin-metadata-jvm) should keep their published versions
                    else -> {
                        // leave as-is
                    }
                }
            }
        }
        // Force common stdlib coordinates as an extra safety net
        resolutionStrategy.force(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.22",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.22",
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22"
        )
    }
}
