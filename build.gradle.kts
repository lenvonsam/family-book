buildscript {
	val springBootVersion = "1.4.3.RELEASE"
	val kotlinVersion = "1.0.6"
	extra["kotlinVersion"] = kotlinVersion

	repositories {
		jcenter()
		mavenCentral()
	}

	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
		classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
	}
}

apply {
	plugin("war")
	plugin("kotlin")
	plugin("kotlin-spring")
	plugin("kotlin-jpa")
	plugin("org.springframework.boot")
}

version = "0.0.1"

configure<JavaPluginConvention> {
	setSourceCompatibility(1.8)
	setTargetCompatibility(1.8)
}

repositories {
	jcenter()
	mavenCentral()
}

val kotlinVersion = extra["kotlinVersion"] as String

dependencies {
	compile("org.springframework.boot:spring-boot-starter-web")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("org.springframework.boot:spring-boot-starter-security")
	compile("mysql:mysql-connector-java")
	compile("com.domingosuarez.boot:spring-boot-starter-jade4j:0.3.1")
	compile("com.h2database:h2")
	compile("cn.leancloud:java-sdk:0.1.+")
	compile("nl.komponents.kovenant:kovenant:3.3.0")
	compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
	compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
	compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.8.4")
	testCompile("org.springframework.boot:spring-boot-starter-test")
}

