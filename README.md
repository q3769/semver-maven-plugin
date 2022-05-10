[![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)

# semver-maven-plugin

A simple Maven Plugin to update current project's version in the local POM file, via CLI or scripting, according to the
specifications of [Semantic Versioning 2.0.0](https://semver.org/)

## User story

As a user of this Maven Plugin, I want to update the value of the `version` element in the local `pom.xml` file
according to the Semantic Versioning 2.0.0 specifications, by issuing a Maven command from CLI or scripting.

Notes:

Unlike some other Maven versioning plugins, this one does not include or combine any extra functionalities beyond local
version change. E.g., in and of itself, it does not communicate with any version control system or artifact repository.
The plugin has one single concern on the project's definition - its version in the local `pom.xml` file.

The motivation of the effort is to facilitate using CICD pipeline code/script to automate versions increments. Manual
version updates by developers would only be needed in rare scenarios such as changing the major version. The hope of the
simple design is a cleaner separation of concerns. Instead of going beyond the concern of project version definition,
this plugin serves as an atomic and composable action in whatever work flow code/script you may care to set up.

## Prerequisite

Maven 3.5.4 or better

## Get it...

In pom.xml

```
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>io.github.q3769</groupId>
                <artifactId>semver-maven-plugin</artifactId>
                <version>20220509.0.1</version>
            </plugin>
            ...
```            

## Use it...

*Note: By default, only the parent project's version is processed. Use the* `'-DprocessModule'` *flag if the module
needs to be included in the processing.*

From CLI, assuming you are in the Maven project's default root directory where the pom.xml file is located

### Hard set

```
mvn semver:set-current -Dsemver=blah
```

errors out because `blah` is not a valid SemVer

```
mvn semver:set-current -Dsemver=1.2.3-beta
```

sets the value of the version element of the pom.xml file to be `1.2.3-beta`, regardless of the existing value

### Increment normal version number

```
mvn semver:increment-major
```

increments `1.2.3-beta.1` into `2.0.0`, where `1.2.3-beta.1` is the existing POM value

```
mvn semver:increment-minor -Dsnapshot=true
```

increments `1.2.3 into 1.3.0-SNAPSHOT`. Note that the `snapshot` flag works with all three types of normal version
increment.

```
mvn semver:increment-patch
```

increments `1.2.3-beta.1` into `1.2.4`

```
mvn semver:calendar-major
```

increments `1.23.4` or `20201231.2.3-beta.1` into `20210131.0.0`, assuming today is Jan 31, 2021. If the original POM
version is already newer than that of `<today>.0.0`, error is returned and no update will be performed to the POM.

### Finalize current version

```
mvn semver:finalize-current
```

changes `1.2.3-SNAPSHOT` or `1.2.3-beta.1+build.10` into `1.2.3`, stripping off all additional labels

### Pre-release and Build Metadata labels

```
mvn semver:update-pre-release
```

increments `1.2.3-beta` into `1.2.3-beta.1`

```
mvn semver:update-build-metadata
```

increments `1.2.3-beta.1+build.10` into `1.2.3-beta.1+build.11`

```
mvn semver:update-pre-release -Dset=beta
```

updates `1.2.3-alpha+build.7` into `1.2.3-beta`

```
mvn semver:update-build-metadata -Dset=build.reno
```

updates `1.2.3-alpha` into `1.2.3-alpha+build.reno`

### Pick the newer of the POM version and another SemVer

```
mvn semver:pick-newer -Dsemver=1.3.8-HOTFIX
```

updates current POM's `1.2.3` version to `1.3.8-HOTFIX` because `1.3.8-HOTFIX` is a newer version than `1.2.3`. However,
if the current POM version is `1.3.8`, then no change will be made because, according to the SemVer spec, the
current `1.3.8` is newer than the given `1.3.8-HOTFIX`.

### Merge with another SemVer

```
mvn semver:merge -Dsemver=1.3.10-HOTFIX
```

updates `1.2.0-SNAPSHOT+chi.1` into `1.4.0-SNAPSHOT+chi.1`

Note that the merge strategy here is opinionated; SemVer spec itself does not mention merging. The basic idea is that
the merge is centered around the current POM's version. Other than the SemVer specified precedence, the intents and
purposes of the current POM version take precedence during the merge. First, figure out the newer version between the
current POM's and the given version to merge, per the SemVer spec precedence. If the current POM version is newer, no
change will be made and the current POM version is the merged result. Otherwise, if the given version to merge is newer,
then it is to be incremented on the intended change category - `major`, `minor`, or `patch` - of the *current POM
version*; lastly, the current POM version's pre-release and build metadata labels, if any exist, always stay and serve
as the final merged version's labels.

In this case, the given version `1.3.10-HOTFIX` is newer, so it is to be incremented to form the merge result. The
intended change category of the current POM version `1.2.0-SNAPSHOT+chi.1` is `minor`, so the given
version `1.3.10-HOTFIX` is incremented on its `minor` category, resulting in `1.4.0` per SemVer spec. Lastly, the
current POM version's labels ( in this case `SNAPSHOT` and `chi.1`), if any exist, always stay as they are. Thus, for
the final merged version, we have `1.4.0-SNAPSHOT+chi.1`.

### Verify the current POM version

```
mvn semver:verify-current
```

prints confirmation message if the current version of the local POM is in valid SemVer format, errors otherwise.

```
mvn semver:verify-current -Dforce-stdout -q
```

prints the current POM version and nothing else (e.g. `1.2.3-beta.4+build.5`) in std out if it is a valid SemVer. Note
that, to produce the clean current SemVer only, you need `-q` or `--quiet` option to suppress the usual Maven messages.
