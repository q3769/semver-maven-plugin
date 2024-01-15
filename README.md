# semver-maven-plugin

A [Maven Plugin](https://maven.apache.org/plugins/index.html) to update local POM version in compliance
with [Semantic Versioning 2.0.0](https://semver.org/)

## User story

As a user of this Maven Plugin, I want to update my project's version in the local pom.xml file according
to the Semantic Versioning 2.0.0 specifications, by issuing Maven commands from CLI or build script/code.

Notes:

- Unlike some other Maven versioning plugins, this one does not try to include or combine any extra functionalities
  beyond local POM version change. In and of itself, for example, the plugin does not communicate with any version
  control system or artifact repository: It has one single concern - the project's version as defined in the local
  pom.xml file.
- Command-line-invokable as an atomic and composable action/step, the plugin aims to suit whatever CI/CD pipeline
  workflow one may care to set up by script/code.

## Prerequisite

Maven 3.5.4 or better

## Get it...

[![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)

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

## Use it...

- By default, only the parent project's version is processed, module versions are not. Use the `-DprocessModule` command
  flag if you also wish to process modules.

From CLI, assuming you are in the Maven project's default root directory where the pom.xml file is located:

### Hard set

```shell
mvn semver:set-current -Dsemver=blah
```

errors out because `blah` is not a valid SemVer

```shell
mvn semver:set-current -Dsemver=1.2.3-beta
```

sets the new value of the version element of the pom.xml file to be `1.2.3-beta`, regardless of the original value in
POM

### Increment normal version number

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

Assuming now is 1PM on Jan 31, 2021 in UTC time zone, then:

```shell
mvn semver:calendar-major
```

increments `1.2.3` into `2021.0.0`; `20201225.2.3-beta.1` into `20210131.0.0`; `2021.2.3`
into `202101.0.0`; `20210131.2.3` into `2021013113.0.0`, etc...

This goal uses the current calendar datetime as the target `major` number: It tries to increase the accuracy
of the current time stamp until the value is larger than the original `major` version number; then uses that
time stamp value as the replacement. If the original `major` number is too large to replace even when increasing
the current time accuracy to milliseconds, then the goal errors out and no update will be performed to the POM.

```shell
mvn semver:calendar-minor
```

applies similar manipulations as with `semver:calendar-major`, to the `minor` normal version number of the original
semver.

```shell
mvn semver:calendar-patch
```

applies similar manipulations as with `semver:calendar-major`, to the `patch` normal version number of the original
semver.

### Update pre-release version and build metadata

```shell
mvn semver:update-pre-release
```

increments `1.2.3-beta` into `1.2.3-beta.1`

```shell
mvn semver:update-build-metadata
```

increments `1.2.3-beta.1+build.10` into `1.2.3-beta.1+build.11`.

Using this goal alone, without the `Dset=...` parameter (mentioned later), is deprecated as SemVer spec
excludes using build metadata for precedence or equavalance comparison; thus, there is no definitive way
to increment build metadata.

```shell
mvn semver:update-pre-release -Dset=beta
```

updates/sets `1.2.3-alpha+build.7` into `1.2.3-beta`

```shell
mvn semver:update-build-metadata -Dset=build.reno
```

updates/sets `1.2.3-alpha` into `1.2.3-alpha+build.reno`

### Pick the newer between the pom version and another semver

```shell
mvn semver:pick-newer -Dsemver=1.3.0-HOTFIX
```

updates the original POM version `1.2.3` to `1.3.0-HOTFIX` because `1.3.0-HOTFIX` is a newer version than `1.2.3`.
However, if the original version were `1.3.0`, then no change would have been made because, according to the SemVer
precedence, the original `1.3.0` is newer than the given `1.3.0-HOTFIX`. (Note that a final SemVer is always newer
than any labeled counterpart, regardless the label's semantics. That is, the final/stable "hot fix" SemVer of `1.3.0`
would be `1.3.1`, not `1.3.0-HOTFIX` or `1.3.1-HOTFIX`.)

### Merge with another semver

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
   incremented on the **last incremented normal version of the current POM version** - `major`, `minor`, or `patch`.

4. The current POM version's pre-release and build metadata labels, if any exist, always stay and serve as the final
   merged version's labels.

In this case, the given version `1.3.10-HOTFIX` is newer, so it is to be incremented to form the merge result. The
last incremented normal version of the current POM version `1.2.0-SNAPSHOT+chi.1` is `minor`, so the given version
`1.3.10-HOTFIX` is incremented on its own `minor` version number, resulting in `1.4.0` per SemVer spec. (Note that the
given version `1.3.10-HOTFIX`'s last incremented normal version is `patch` but that anyway does not matter per the merge
rules here.) Then, the current POM version's labels (in this case `SNAPSHOT` and `chi.1`), if any exist, always stay as
they are. Thus, for the final merged version, we have `1.4.0-SNAPSHOT+chi.1`.

Assuming now is 1PM on Jan 31, 2021, then:

```shell
mvn semver:merge-calendar -Dsemver=1.3.10-HOTFIX
```

updates `1.2.0-SNAPSHOT+chi.1` into `1.2021.0-SNAPSHOT+chi.1`, where `1.2.0-SNAPSHOT+chi.1` is the current POM version.
This goal performs similar functions as with `semver:merge`, but using calendar value as the update result instead of
simple increment.

### Finalize current version

```shell
mvn semver:finalize-current
```

changes `1.2.3-SNAPSHOT` or `1.2.3-beta.1+build.10` into `1.2.3`, stripping off all additional labels

### Verify the current pom version

```shell
mvn semver:verify-current
```

prints confirmation message if the current version of the local POM is in valid SemVer format, errors otherwise.

```shell
mvn semver:verify-current -Dforce-stdout -q
```

prints the current POM version and nothing else (e.g. `1.2.3-beta.4+build.5`) in std out if it is a valid SemVer. 
For a clean print out of the project's SemVer with nothing else, you need the `-q` or `--quiet` option to suppress 
the usual Maven messages.
