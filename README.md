[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://paypal.me/q3769)

# SemVer Maven Plugin

Unlike some other versioning plugins for Maven, this one only addresses the concern of versioning, and not that of release which is a different and larger-scoped concern. This plugin operates on the version value in the local POM file, according to the spec of Semantic Versioning 2.0.0; it does nothing else. 

Stemmed from the use case of using CICD pipeline to automate and manage a Maven project's version, where manual intervention on versioning would only be needed in rare scenarios, such as major version increment.

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
increments 1.2.3 into 1.3.0-SNAPSHOT

```
$ mvn semver:patch
```
increments 1.2.3-beta.1 into 1.2.4


```
$ mvn semver:calMajor
```
increments 20201231.2.3-beta.1 into 20210213.0.0, assuming today is Feb 13, 2021

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
