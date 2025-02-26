import de.aaschmid.gradle.plugins.cpd.Cpd

plugins {
    `java-library`
    java
    checkstyle
    pmd
    id("de.aaschmid.cpd")
    id("com.github.spotbugs")
    `build-dashboard`
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("res")
        }
    }
    test {
        java {
            srcDirs("src")
        }
    }
}

repositories {
    mavenCentral()
    flatDir {
        dirs("lib")
    }
}

buildscript {


    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://plugins.gradle.org/m2/")
        google()
    }
    dependencies {


    }
}

allprojects {
    apply(plugin = "eclipse")

    version = "1.0"

    ext {
        set("appName", "SuperNinjaFrog")
        set("gdxVersion","1.9.14")
        set("roboVMVersion","2.3.12")
        set("box2DLightsVersion","1.5")
        set("ashleyVersion","1.7.3")
        set("aiVersion","1.8.2")
        set("gdxControllersVersion","2.1.0")
    }

    val appName = ext.get("appName") as String
    val gdxVersion = ext.get("gdxVersion") as String
    val roboVMVersion = ext.get("roboVMVersion") as String
    val box2DLightsVersion = ext.get("box2DLightsVersion") as String
    val ashleyVersion = ext.get("ashleyVersion") as String
    val aiVersion = ext.get("aiVersion") as String
    val gdxControllersVersion = ext.get("gdxControllersVersion") as String

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }
}

project(":desktop") {
    apply(plugin= "java-library")

    val appName = ext.get("appName") as String
    val gdxVersion = ext.get("gdxVersion") as String
    val roboVMVersion = ext.get("roboVMVersion") as String
    val box2DLightsVersion = ext.get("box2DLightsVersion") as String
    val ashleyVersion = ext.get("ashleyVersion") as String
    val aiVersion = ext.get("aiVersion") as String
    val gdxControllersVersion = ext.get("gdxControllersVersion") as String

    dependencies {
        implementation(project(":core"))
        api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
        api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
        api("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    }
}

project(":core") {
    apply(plugin= "java-library")

    val appName = ext.get("appName") as String
    val gdxVersion = ext.get("gdxVersion") as String
    val roboVMVersion = ext.get("roboVMVersion") as String
    val box2DLightsVersion = ext.get("box2DLightsVersion") as String
    val ashleyVersion = ext.get("ashleyVersion") as String
    val aiVersion = ext.get("aiVersion") as String
    val gdxControllersVersion = ext.get("gdxControllersVersion") as String

    dependencies {
        api("com.badlogicgames.gdx:gdx:$gdxVersion")
        api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    }
}

project(":tests") {
    apply(plugin= "java")

    sourceSets {
        test {
            java {
                srcDirs("src")
            }
        }
    }
    //sourceSets.test.java.srcDirs = ["src/"]

    val appName = ext.get("appName") as String
    val gdxVersion = ext.get("gdxVersion") as String
    val roboVMVersion = ext.get("roboVMVersion") as String
    val box2DLightsVersion = ext.get("box2DLightsVersion") as String
    val ashleyVersion = ext.get("ashleyVersion") as String
    val aiVersion = ext.get("aiVersion") as String
    val gdxControllersVersion = ext.get("gdxControllersVersion") as String

    dependencies {
	     implementation(project(":desktop"))
        implementation(project(":core"))

        implementation("junit:junit:4.+")
        implementation("org.mockito:mockito-core:3.2.4")

        implementation("com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
        implementation("com.badlogicgames.gdx:gdx:$gdxVersion")

        testImplementation("junit:junit:4.+")
        testImplementation("org.mockito:mockito-core:3.2.4")

        testImplementation("com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
        testImplementation("com.badlogicgames.gdx:gdx:$gdxVersion")
        testImplementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    }
}


/*
dependencies {
    implementation("junit:junit:_")
    implementation("org.junit.jupiter:junit-jupiter-api:_")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:_")
    runtimeOnly("org.junit.vintage:junit-vintage-engine:_")
    File("lib")
        .takeIf { it.exists() }
        ?.takeIf { it.isDirectory }
        ?.listFiles()
        ?.filter { it.extension == "jar" }
        ?.forEach { implementation(files("lib/${it.name}")) }
}
*/

tasks.withType<Test>() {
    ignoreFailures = true
    useJUnitPlatform()
}

spotbugs {
    setEffort("max")
    setReportLevel("low")
    showProgress.set(true)
    val excludeFile = File("${project.rootProject.projectDir}/config/spotbugs/excludes.xml")
    if (excludeFile.exists()) {
        excludeFilter.set(excludeFile)
    }
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    ignoreFailures = true
    reports {
        create("html") {
            enabled = true
        }
    }
}

pmd {
    ruleSets = listOf()
    ruleSetConfig = resources.text.fromFile("${project.rootProject.projectDir}/config/pmd/pmd.xml")
    isIgnoreFailures = true
}

cpd {
    isIgnoreFailures = true
}

tasks.withType<Cpd> {
    reports {
        xml.setEnabled(false)
        text.setEnabled(true)
    }
    language = "java"
    minimumTokenCount = 50
    ignoreFailures = true
    source = sourceSets["main"].allJava
}

checkstyle {
    isIgnoreFailures = true
}

typealias QAInfoContainer = Iterable<QAInfo>

interface QAInfo {
    val checker: String
    val lines: IntRange
    val details: String
    val file: String
    val blamedTo: Set<String>
}

fun List<String>.commandOutput(): String {
    val process = ProcessBuilder(this).redirectOutput(ProcessBuilder.Redirect.PIPE).start()
    process.waitFor(1, TimeUnit.MINUTES)
    return process.inputStream.bufferedReader().readText()
}

val authorMatch = Regex("^author\\s+(.+)$")
fun blameFor(file: String, lines: IntRange): Set<String> =
    listOf("git", "blame", "-L", "${lines.start},${lines.endInclusive}", "-p", file)
        .commandOutput().lines()
        .flatMap { line -> authorMatch.matchEntire(line)?.destructured?.toList() ?: emptyList() }
        .toSet()

data class QAInfoForChecker(
    override val checker: String,
    override val file: String,
    override val lines: IntRange = 1..File(file).readText().lines().size,
    override val details: String = "",
    private val blamed: Set<String>? = null,
) : QAInfo {
    override val blamedTo: Set<String> = blamed ?: blameFor(file, lines)
}

operator fun org.w3c.dom.Node.get(attribute: String): String =
    attributes?.getNamedItem(attribute)?.textContent
        ?: throw IllegalArgumentException("No attribute '$attribute' in $this")

fun org.w3c.dom.Node.childrenNamed(name: String): List<org.w3c.dom.Node> =
        childNodes.toIterable().filter { it.nodeName == name }

class PmdQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "file" }
        .flatMap { file -> file.childrenNamed("violation").map{ file to it } }
        .map { (file, violation) ->
            QAInfoForChecker(
                "Sub-optimal Java object-orientation",
                file["name"],
                violation["beginline"].toInt()..violation["endline"].toInt(),
                "[${violation["ruleset"].toUpperCase()}] ${violation.textContent.trim()}",
            )
        }
        .asIterable()
)

class CpdQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "duplication" }
        .map { duplication ->
            val files = duplication.childrenNamed("file")
            val filePaths = files.map { it["path"] }
            val lines = duplication["lines"].toInt()
            val shortFiles = files.map { "${File(it["path"]).name}:${it["line"]}" }
            val ranges = files.map {
                val begin = it["line"].toInt()
                begin..(begin + lines)
            }
            val blamed = filePaths.zip(ranges).flatMap { (file, lines) -> blameFor(file, lines) }.toSet()
            val description = "Duplication of $lines lines" +
                    " and ${duplication["tokens"]} tokens across ${filePaths.toSet().size}" +
                    " files: ${shortFiles.joinToString(prefix = "", postfix = "")}"
            QAInfoForChecker(
                    "Duplications and violations of the DRY principle",
                    files.first()["path"],
                    ranges.first(),
                    description,
                    blamed
            )
        }
        .asIterable()
)

class CheckstyleQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.toIterable()
        .asSequence()
        .filter { it.nodeName == "file" }
        .flatMap { file -> file.childrenNamed("error").map{ file["name"] to it } }
        .map { (file, error) ->
            val line = error["line"].toInt()
            val lineRange = line..line
            QAInfoForChecker("Style errors", file, lineRange, error["message"])
        }
        .asIterable()
)

class SpotBugsQAInfoExtractor(root: org.w3c.dom.Element) : QAInfoContainer by (
    root.childNodes.let { childNodes ->
        val sourceDirs = childNodes.toIterable()
            .filter { it.nodeName == "Project" }
            .first()
            .childrenNamed("SrcDir")
        val sourceDir = if (sourceDirs.size == 1) {
                sourceDirs.first()
            } else {
                sourceDirs.find { it.textContent.endsWith("java") }
            } ?: throw IllegalStateException("Invalid source directories: ${sourceDirs.map { it.textContent }}")
        val sourcePath = sourceDir.textContent.trim()
        childNodes.toIterable()
            .asSequence()
            .filter { it.nodeName == "BugInstance" }
            .map { bugDescriptor ->
                val sourceLineDescriptor = bugDescriptor.childrenNamed("SourceLine").first()
                val category = bugDescriptor["category"].takeUnless { it == "STYLE" } ?: "UNSAFE"
                QAInfoForChecker(
                    "Potential bugs",
                    "$sourcePath${File.separator}${sourceLineDescriptor["sourcepath"]}",
                    sourceLineDescriptor["start"].toInt()..sourceLineDescriptor["end"].toInt(),
                    "[$category] ${bugDescriptor.childrenNamed("LongMessage").first().textContent.trim()}",
                )
            }
            .asIterable()
    }
)

fun org.w3c.dom.NodeList.toIterable() = Iterable {
    object : Iterator<org.w3c.dom.Node> {
        var index = 0;
        override fun hasNext(): Boolean = index < length - 1
        override fun next(): org.w3c.dom.Node = item(index++)
    }
}

fun String.endingWith(postfix: String): String = takeIf { endsWith(postfix) } ?: "$this$postfix"

tasks.register("blame") {
    val dependencies = tasks.withType<org.gradle.api.plugins.quality.Checkstyle>() +
            tasks.withType<org.gradle.api.plugins.quality.Pmd>() +
            tasks.withType<com.github.spotbugs.snom.SpotBugsTask>() +
            tasks.withType<de.aaschmid.gradle.plugins.cpd.Cpd>()
    dependsOn(dependencies)
    val output = "${project.buildDir}${File.separator}blame.md"
    outputs.file(output)
    doLast {
        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        val xmlParser = factory.newDocumentBuilder();
        val errors = dependencies
            .flatMap { task -> task.outputs.files.asIterable().filter { it.exists() && it.extension == "xml" } }
            .flatMap<File, QAInfo> {
                val root: org.w3c.dom.Element = xmlParser.parse(it).documentElement
                when (root.tagName) {
                    "pmd" -> PmdQAInfoExtractor(root)
                    "pmd-cpd" -> CpdQAInfoExtractor(root)
                    "checkstyle" -> CpdQAInfoExtractor(root)
                    "BugCollection" -> SpotBugsQAInfoExtractor(root)
                    else -> emptyList<QAInfo>().also { println("Unknown root type ${root.tagName}")}
                }
            }
        val errorsByStudentByChecker: Map<String, Map<String, List<QAInfo>>> = errors
            .flatMap { error -> error.blamedTo.map { it to error } }
            .groupBy { it.first }
            .mapValues { (_, errors) -> errors.map { it.second }.groupBy { it.checker } }
        val report = errorsByStudentByChecker.map { (student, errors) ->
            """
            |# $student
            |
            |${errors.map { it.value.size }.sum()} violations
            |${errors.map { (checker, violations) ->
                """
                |## $checker: ${violations.size} mistakes
                ${ violations.sortedBy { it.details }
                    .joinToString("") {
                        val fileName = File(it.file).name
                        "|* ${it.details.endingWith(".")} In: $fileName@[${it.lines}]\n"
                    }.trimEnd()
                }
                """
            }.joinToString(separator = "", prefix = "", postfix = "")}
            |
            """.trimMargin()
        }.joinToString(separator = "", prefix = "", postfix = "")
        println(report)
        file(output).writeText(report)
    }
}
