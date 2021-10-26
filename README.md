[![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)
# SemVer Maven Plugin

A simple Maven plugin that manipulates the local POM version via CLI or scripting, according to the Semantic Versioning 2.0.0 [spec](https://semver.org/). 

Unlike some other Maven versioning plugins, this one does not include or combine any extra functionalities beyond local version change. E.g. In and of itself, it does not communicate with version control systems or artifact repositories. The plugin has one single concern on the project's definition - its version. 

The motivation of the effort is to facilitate using CICD pipeline code/script to automate versions increments. Manual version updates by developers would only be needed in rare scenarios such as changing the major version. 

The hope of the simple design is a cleaner separation of concerns. Instead of going beyond the concern of project version definition into regulating the pipeline work flow, for instance, this plugin serves as a composable action in whatever work flow code/script you may care to set up.

## Get it...

In pom.xml

```
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.q3769</groupId>
                <artifactId>semver-maven-plugin</artifactId>
                <version>20210213.0.1</version>
            </plugin>
            ...
```            

## Use it...

From CLI, assuming you are in the Maven project's default root directory where the pom.xml file is located 

### Hard set

```
$ mvn semver:set -Dsemver=blah
```
errors out because 'blah' is not a valid SemVer

```
$ mvn semver:set -Dsemver=1.2.3-beta
```
sets the value of the version element of the pom.xml file to be 1.2.3-beta, regardless of the existing value

### Increment normal version number

```
$ mvn semver:major
```
increments 1.2.3-beta.1 into 2.0.0, where 1.2.3-beta.1 is the existing value

```
$ mvn semver:minor -Dsnapshot=true
```
increments 1.2.3 into 1.3.0-SNAPSHOT. Note that the snapshot flag works with all three types of normal version increment.

```
$ mvn semver:patch
```
increments 1.2.3-beta.1 into 1.2.4


```
$ mvn semver:calMajor
```
increments 1.23.4 or 20201231.2.3-beta.1 into 20210131.0.0, assuming today is Jan 31, 2021. Note that repeated calls on the same calendar day will always succeed, resulting in the same major number bearing today's date, while resetting minor and patch numbers to zeros

### Finalize current version

```
$ mvn semver:final
```
changes 1.2.3-SNAPSHOT or 1.2.3-beta.1+build.10 into 1.2.3, stripping off all additional labels

### Increment pre-release or build meta label

```
$ mvn semver:pr
```
increments 1.2.3-beta into 1.2.3-beta.1

```
$ mvn semver:bm
```
increments 1.2.3-beta.1+build.10 into 1.2.3-beta.1+build.11
