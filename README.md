# semver-maven-plugin

Unlike some other versioning plugins for Maven, this one only addresses the concern of versioning, and not that of release. Release is a different and larger-scoped concern. This plugin operates on the value of the version element in the local POM file, according to the SemVer Spec (Semantic Versioning 2.0.0); it does nothing else.

## Get it...

```
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.q3769</groupId>
                <artifactId>semver-maven-plugin</artifactId>
                <version>2.1.0</version>
            </plugin>
            ...
```            

## Use it...

### Hard set

```
$ mvn semver:set -Dsemver=blah
```
errors out because blah is not a valid SemVer text

```
$ mvn semver:set -Dsemver=1.2.3-beta
```
sets the value of the version element in the POM file to be 1.2.3-beta

### Increment normal release digit

```
$ mvn semver:major
```
increments 1.2.3-beta.1 into 2.0.0

```
$ mvn semver:minor -Dsnapshot=true
```
increments 1.2.3-beta.1 into 1.3.0-SNAPSHOT

```
$ mvn semver:patch
```
increments 1.2.3-beta.1 into 1.2.4

### Increment pre-release or build meta portion

```
$ mvn semver:p
```
increments 1.2.3-beta into 1.2.3-beta.1

```
$ mvn semver:b
```
increments 1.2.3-beta.1+build.10 into 1.2.3-beta.1+build.11

### Finalize the current version

```
$ mvn semver:final
```
changes 1.2.3-beta.4+build.5 into final release form 1.2.3, stripping off all meta info if any exists

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://paypal.me/q3769)
