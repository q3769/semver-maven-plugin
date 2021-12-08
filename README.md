[![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)

# semver-maven-plugin

A simple Maven Plugin to update current project's version in the local POM file, via CLI or scripting, according to the specifications of Semantic Versioning 2.0.0 

## User story

As a user of this Maven Plugin, I want to update the value of the version element in the local pom.xml file according to the Semantic Versioning 2.0.0 specifications, by issuing a Maven command from CLI or scripting.

Unlike some other Maven versioning plugins, this one does not include or combine any extra functionalities beyond local version change. E.g., in and of itself, it does not communicate with any version control system or artifact repository. The plugin has one single concern on the project's definition - its version in the local pom.xml file. The motivation of the effort is to facilitate using CICD pipeline code/script to automate versions increments. Manual version updates by developers would only be needed in rare scenarios such as changing the major version. The hope of the simple design is a cleaner separation of concerns. Instead of going beyond the concern of project version definition, this plugin serves as an atomic and composable action in whatever work flow code/script you may care to set up.

## Get it...

In pom.xml

```
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.q3769</groupId>
                <artifactId>semver-maven-plugin</artifactId>
                <version>20211207.0.0</version>
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
$ mvn semver:calendar-major
```

increments 1.23.4 or 20201231.2.3-beta.1 into 20210131.0.0, assuming today is Jan 31, 2021. If the original POM version is already newer than that of `<today>.0.0`, error is returned and no update will be performed to the POM. 

### Finalize current version

```
$ mvn semver:final
```

changes 1.2.3-SNAPSHOT or 1.2.3-beta.1+build.10 into 1.2.3, stripping off all additional labels

### Pre-release and build meta label

```
$ mvn semver:pre-release
```

increments 1.2.3-beta into 1.2.3-beta.1

```
$ mvn semver:build-metadata
```

increments 1.2.3-beta.1+build.10 into 1.2.3-beta.1+build.11

```
$ mvn semver:pre-release -Dset=beta
```

updates 1.2.3-alpha+build.7 into 1.2.3-beta

```
$ mvn semver:build-metadata -Dset=build.reno
```

updates 1.2.3-alpha into 1.2.3-alpha+build.reno

### Take the newer of the POM version and another SemVer

```
$ mvn semver:newer -Dsemver=1.3.8-HOTFIX
```

updates current POM's 1.2.3 version to 1.3.8-HOTFIX because 1.3.8-HOTFIX is a newer version than 1.2.3. However, if the current POM version is 1.3.8, then no change will be made because, according to the SemVer spec, the current 1.3.8 is newer than the given 1.3.8-HOTFIX.

### Merge from another SemVer

```
$ mvn semver:merge-from iDsemver=1.3.10-HOTFIX
```

updates 1.2.3-SNAPSHOT into 1.3.11-SNAPSHOT. The basic idea is first, according to the SemVer spec, figure out the target category the POM version is intends to be incrementing. Then compare the POM version with the given one. If the POM version is older or equal to the given, then increment the given on the category number the POM version intends to increment, and use the incremented given one as the result. Otherwise if the POM version is newer than the given, then the POM version stays unchanged. In any case, the pre-release and build metadata labels of the POM version always stay the same.

### Verify the current POM version

```
$ mvn semver:verify
```

prints confirmation message if the current version of the local POM is in valid SemVer format, errors otherwise.
