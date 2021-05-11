# SemVer Maven Plugin

Unlike some other versioning plugins for Maven, this one only addresses the concern of versioning, and not that of release which is a different and larger-scoped concern. This plugin operates on the version value in the local POM file, according to the spec of Semantic Versioning 2.0.0; it does nothing else. 

Motivated by the use case of using CICD pipeline to automate a Maven project's version increment, where manual intervention on versioning would only be needed in rare scenarios, such as major version increment.

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
errors out because blah is not a valid SemVer text

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
increments 20201231.2.3-beta.1 into 20210213.0.0, assuming today is Feb 13, 2021. Note that repeated calls on the same calendar day will silently succeed, with the same major number and resetting minor and patch numbers to zeros.

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
