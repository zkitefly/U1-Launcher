plugins {
    `java-library`
}

dependencies {
    api("com.google.code.gson:gson:2.10.1")
    api("com.moandjiezana.toml:toml4j:0.7.2")
    api("org.tukaani:xz:1.9")
    api("org.hildan.fxgson:fx-gson:5.0.0")
    api("org.jenkins-ci:constant-pool-scanner:1.2")
    api("com.github.steveice10:opennbt:1.4")
    api("org.nanohttpd:nanohttpd:2.3.1")
    api("org.apache.commons:commons-compress:1.22")
    api("org.apache.commons:commons-lang3:3.12.0")
    compileOnlyApi("org.jetbrains:annotations:24.0.0")
}
