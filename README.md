# SemVer Maven Plugin

A simple Maven plugin that manipulates the local POM version via CLI or scripting, according to the Semantic Versioning 2.0.0 [spec](https://semver.org/). 

Unlike some other Maven versioning plugins, in and of itself, this one does not include or combine any extra functionalities beyond local versioning. E.g. There is no interaction with source code or artifact repositories after the version update.

The project is motivated by the use case of using CICD pipeline code/script to automate a Maven project's version increment, where manual intervention on versioning would only be needed in rare scenarios, such as change of major version. The hope of the simple design is a cleaner separation of concerns. Instead of rigidly regulating and enforcing a certain portion of the pipeline workflow by going beyond the concern of versioning, this plugin serves as an atomic action that is more robust, flexible, and easier to integrate in a pipeline.

## Get it...

In pom.xml

```
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.q3769</groupId>
                <artifactId>semver-maven-plugin</artifactId>
                <version>20210213.0.0</version>
            </plugin>
            ...
```            

## Use it...

From CLI

### Hard set

```
$ mvn semver:set -Dsemver=blah
```
errors out because 'blah' is not a valid SemVer

```
$ mvn semver:set -Dsemver=1.2.3-beta
```
sets the value of the version element in the POM file to be 1.2.3-beta

### Increment normal version number

```
$ mvn semver:major
```
increments 1.2.3-beta.1 into 2.0.0

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
increments 20201231.2.3-beta.1 into 20210213.0.0, assuming today is Feb 13, 2021. Note that repeated calls on the same calendar day will always succeed, with the same major number bearing today's date, while resetting minor and patch numbers to zeros.

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
