import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.BuildPluginTask
import org.jetbrains.intellij.platform.gradle.tasks.RunIdeTask


plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.qodana) // Gradle Qodana Plugin
    alias(libs.plugins.kover) // Gradle Kover Plugin
    alias(libs.plugins.grammarkit)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(21)
}

// Configure project's dependencies
repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/version_catalogs.html
dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion"))

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        // Module Dependencies. Uses `platformBundledModules` property from the gradle.properties file for bundled IntelliJ Platform modules.
        bundledModules(providers.gradleProperty("platformBundledModules").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased()).withHeader(false).withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion")
            .map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

// Configure Gradle Kover Plugin - read more: https://kotlin.github.io/kotlinx-kover/gradle-plugin/#configuration-details
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }

    generateParser {
        sourceFile.set(file("src/main/grammar/Cfg.bnf"))
        pathToParser.set("/arma/config/parser/CfgParser.java")
        pathToPsiRoot.set("/arma/config/psi")
        targetRootOutputDir.set(file("src/main/gen"))
        purgeOldFiles.set(true)
    }
    @Suppress("unused")
    val runIdeForUiTests by intellijPlatformTesting.runIde.registering {
        task {
            jvmArgumentProviders += CommandLineArgumentProvider {
                listOf(
                    "-Drobot-server.port=8082",
                    "-Dide.mac.message.dialogs.as.sheets=false",
                    "-Djb.privacy.policy.text=<!--999.999-->",
                    "-Djb.consents.confirmation.enabled=false",
                )
            }
        }

        plugins {
            robotServerPlugin()
        }
    }
}

sourceSets {
    main {
        java.srcDir("src/main/gen")
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateParser")
}

tasks.named("compileJava") {
    dependsOn("generateParser")
}

tasks.withType<Test> {
    enabled = false
}

//Cannot infer type for type parameter 'T'. Specify it explicitly.
tasks.register<Task>("runIdeClean") {
    group = "puotek"
    description = "build.clean and than intellij_platform.runIde"

    dependsOn("clean")
    finalizedBy("runIde")
}

tasks.register<Copy>("copyExample") {
    group = "puotek"
    description = "Duplicates test/resources to test/temp for testing sandbox separation"
    from("src/test/resources/")
    into("src/test/temp/")
}

tasks.register<Task>("bumpVersion") {
    group = "puotek"
    description = "Increment pluginVersion PATCH property by 1"

    doLast {
        val propsFile = file("gradle.properties")
        val text = propsFile.readText()

        val regex = Regex("""pluginVersion\s*=\s*([0-9]+(?:\.[0-9]+)*)""")
        val match = regex.find(text) ?: error("pluginVersion not found in gradle.properties")

        val current = match.groupValues[1]
        val parts = current.split(".").toMutableList()

        val last = parts.last().toInt()
        parts[parts.lastIndex] = (last + 1).toString()

        val next = parts.joinToString(".")
        val newText = text.replace(regex, "pluginVersion=$next")

        propsFile.writeText(newText)

        println("pluginVersion bumped: $current -> $next")
    }
}


tasks.register<Copy>("buildDownloads") {
    group = "puotek"
    description = "Build plugin and copy its ZIP to ~/Downloads"

    // Get the buildPlugin task (type-safe)
    val buildPlugin = tasks.named<BuildPluginTask>("buildPlugin")

    // Ensure the ZIP is created first
    dependsOn(buildPlugin)

    // Copy exactly the archive produced by buildPlugin
    from(buildPlugin.flatMap { it.archiveFile })

    // Target: ~/Downloads
    into(File(System.getProperty("user.home"), "Downloads"))

    finalizedBy("bumpVersion")
}


tasks.named<RunIdeTask>("runIde") {
    args = listOf(
        projectDir.resolve("src/test/temp").absolutePath
    )

    // trust all projects in the sandbox so it doesn't ask
    jvmArgumentProviders += CommandLineArgumentProvider {
        listOf(
            "-Didea.trust.all.projects=true",
            "-Didea.log.level=error"
        )
    }
}
