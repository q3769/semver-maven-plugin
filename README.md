[![](https://img.shields.io/static/v1?label=github&message=repo&color=blue)](https://github.com/q3769/semver-maven-plugin)

A [Maven Plugin](https://maven.apache.org/plugins/index.html) to update local POM version in compliance
with [Semantic Versioning 2.0.0](https://semver.org/)

# User Story

As a user of this Maven Plugin, I want to update my project's version in the local pom.xml file according
to the Semantic Versioning 2.0.0 specifications, by issuing Maven commands from CLI or build script/code.

Notes:

- Unlike some other Maven versioning plugins, this one does not try to include or combine any extra functionalities
  beyond local POM version change. In and of itself e.g. the plugin does not communicate with any version control system
  or artifact repository. The plugin has one single concern on the Maven project - the project's version as defined in
  its local pom.xml file.
- Invokable as an atomic and composable action/step, the plugin aims to suit whatever CI/CD pipeline workflow one may
  care to set up by script/code.

# Prerequisite

Maven 3.5.4 or better

# Get It...

Available
at [![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)

To include in pom.xml:

```
...
<build>
    <plugins>
        <plugin>
            <groupId>io.github.q3769</groupId>
            <artifactId>semver-maven-plugin</artifactId>
        </plugin>
...
```            

# Use It...

- By default, only the parent project's version is processed, module versions are not. Use the `-DprocessModule` command
  flag if you also wish to process modules.

From CLI, assuming you are in the Maven project's default root directory where the pom.xml file is located:

## Hard Set

```shell
mvn semver:set-current -Dsemver=blah
```

errors out because `blah` is not a valid SemVer

```shell
mvn semver:set-current -Dsemver=1.2.3-beta
```

sets the new value of the version element of the pom.xml file to be `1.2.3-beta`, regardless of the original value in
POM

## Increment Normal Version Number

```shell
mvn semver:increment-major
```

increments `1.2.3-beta.1` into `2.0.0`, where `1.2.3-beta.1` is the original POM value

```shell
mvn semver:increment-minor -Dsnapshot=true
```

increments `1.2.3 into 1.3.0-SNAPSHOT`. Note that `snapshot` is a convenience flag to set the target pre-release label
as `SNAPSHOT` with no build meta label. This labeling shorthand only works with a normal version increment - major,
minor, or patch. To set the labels other ways, see examples in the Pre-release and Build Metadata labels section.

```shell
mvn semver:increment-patch
```

increments `1.2.3-beta.1` into `1.2.4`

```shell
mvn semver:calendar-major
```

increments `1.23.4` or `20201231.2.3-beta.1` into `20210131.0.0`, assuming today is Jan 31, 2021. A convenience command
to use calendar date as the SemVer major number. If the original POM version is already the same as or newer
than `<today>.0.0` according to the SemVer spec, then the command errors out and no update will be performed to the POM.

## Finalize Current Version

```shell
mvn semver:finalize-current
```

changes `1.2.3-SNAPSHOT` or `1.2.3-beta.1+build.10` into `1.2.3`, stripping off all additional labels

## Pre-release And Build Metadata Labels

```shell
mvn semver:update-pre-release
```

increments `1.2.3-beta` into `1.2.3-beta.1`

```shell
mvn semver:update-build-metadata
```

increments `1.2.3-beta.1+build.10` into `1.2.3-beta.1+build.11`

```shell
mvn semver:update-pre-release -Dset=beta
```

updates `1.2.3-alpha+build.7` into `1.2.3-beta`

```shell
mvn semver:update-build-metadata -Dset=build.reno
```

updates `1.2.3-alpha` into `1.2.3-alpha+build.reno`

## Pick The Newer Between The POM Version And Another SemVer

```shell
mvn semver:pick-newer -Dsemver=1.3.0-HOTFIX
```

updates the original POM version `1.2.3` to `1.3.0-HOTFIX` because `1.3.0-HOTFIX` is a newer version than `1.2.3`.
However, if the original version were `1.3.0`, then no change would have been made because, according to the SemVer
precedence, the original `1.3.0` is newer than the given `1.3.0-HOTFIX`. (Note that a final SemVer is always newer than
any labeled counterpart, regardless the label's semantics. That is, the final "hot fix" SemVer of `1.3.0` would
be `1.3.1`, not `1.3.0-HOTFIX`.)

## Merge With Another SemVer

```shell
mvn semver:merge -Dsemver=1.3.10-HOTFIX
```

updates `1.2.0-SNAPSHOT+chi.1` into `1.4.0-SNAPSHOT+chi.1`, where `1.2.0-SNAPSHOT+chi.1` is the current POM version.

- This merge strategy is opinionated. The SemVer spec itself only defines the order of precedence among versions, and
  does not mention merging.

The basic idea here is to center the merge process around the current version in the pom.xml file. I.e., the intents and
purposes of the current POM version will dominate those of the given SemVer to merge.

1. Take the newer between the current POM version and the given version to merge, according to the SemVer precedence.

2. If the current POM version is newer, no change will be made and the current POM version is the merge result.
   Otherwise, if the given version to merge is newer, then to form the merge result, the given version is to be
   incremented on the **intended change category of the current POM version** - major, minor, or patch.

3. The current POM version's pre-release and build metadata labels, if any exist, always stay and serve as the final
   merged version's labels.

In this case, the given version `1.3.10-HOTFIX` is newer, so it is to be incremented to form the merge result. The
intended change category of the current POM version `1.2.0-SNAPSHOT+chi.1` is `minor`, so the given version
`1.3.10-HOTFIX` is incremented on its own `minor` category, resulting in `1.4.0` per SemVer spec. (Note that the given
version `1.3.10-HOTFIX`'s intended change category is `patch` but that does not matter per the merge rules, anyway.)
Lastly, the current POM version's labels (in this case `SNAPSHOT` and `chi.1`), if any exist, always stay as they are.
Thus, for the final merged version, we have `1.4.0-SNAPSHOT+chi.1`.

## Verify The Current POM Version

```shell
mvn semver:verify-current
```

prints confirmation message if the current version of the local POM is in valid SemVer format, errors otherwise.

```shell
mvn semver:verify-current -Dforce-stdout -q
```

prints the current POM version and nothing else (e.g. `1.2.3-beta.4+build.5`) in std out if it is a valid SemVer. To
print the current SemVer clean and with nothing else, you need the `-q` or `--quiet` option to suppress the usual Maven
messages.
