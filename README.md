[![Maven Central](https://img.shields.io/maven-central/v/io.github.q3769/semver-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.q3769%22%20AND%20a:%22semver-maven-plugin%22)

# semver-maven-plugin

A simple Maven Plugin to update current project's version in the local POM file, via CLI or scripting, according to the
specifications of [Semantic Versioning 2.0.0](https://semver.org/)

## User story

As a user of this Maven Plugin, I want to update the value of the `version` element in the local pom.xml file according
to the Semantic Versioning 2.0.0 specifications, by issuing a Maven command from CLI or scripting.

Notes:

- Unlike some other Maven versioning plugins, this one does not try to include or combine any extra functionalities
  beyond local POM version change. In and of itself e.g. the plugin does not communicate with any version control system
  or artifact repository. The plugin has one single concern on the Maven project - the project's version as defined in
  its local pom.xml file.
- Invokable as an atomic and composable action/step, the plugin aims to suit whatever workflow you may care to set up in
  the code/script of your CI/CD pipeline.

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
                <version>20220509.0.6</version>
            </plugin>
            ...
```            

## Use it...

*Caution: By default, only the parent project's version is processed, module versions are not. Use
the **`-DprocessModule`** command flag if you also wish to process modules.*

From CLI, assuming you are in the Maven project's default root directory where the pom.xml file is located:

### Hard set

```
mvn semver:set-current -Dsemver=blah
```

errors out because `blah` is not a valid SemVer

```
mvn semver:set-current -Dsemver=1.2.3-beta
```

sets the value of the version element of the pom.xml file to be `1.2.3-beta`, regardless of the original POM version's
value

### Increment normal version number

```
mvn semver:increment-major
```

increments `1.2.3-beta.1` into `2.0.0`, where `1.2.3-beta.1` is the original POM value

```
mvn semver:increment-minor -Dsnapshot=true
```

increments `1.2.3 into 1.3.0-SNAPSHOT`. Note that `snapshot` is just a convenience flag to set the target pre-release
label as `SNAPSHOT`; this works with all three types of normal version increment. To set pre-release label to other
verbiage, see examples in the Pre-release and Build Metadata labels section.

```
mvn semver:increment-patch
```

increments `1.2.3-beta.1` into `1.2.4`

```
mvn semver:calendar-major
```

increments `1.23.4` or `20201231.2.3-beta.1` into `20210131.0.0`, assuming today is Jan 31, 2021. If the original POM
version is already the same as or newer than `<today>.0.0`, error is returned and no update will be performed to the
POM.

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

updates the original POM's `1.2.3` version to `1.3.8-HOTFIX` because `1.3.8-HOTFIX` is a newer version than `1.2.3`.
However, if the original POM version were `1.3.8`, then no change would have been made because, according to the
SemVer precedence, the original `1.3.8` is newer than the given `1.3.8-HOTFIX`.

### Merge with another SemVer

```
mvn semver:merge -Dsemver=1.3.10-HOTFIX
```

updates `1.2.0-SNAPSHOT+chi.1` into `1.4.0-SNAPSHOT+chi.1`, where `1.2.0-SNAPSHOT+chi.1` is the current POM version.

*Note: This merge strategy is opinionated. SemVer spec itself does not mention merging.*

The basic idea here is to center the merge process around the current POM version. I.e., the intents and purposes of the
current POM version will dominate the merge.

1. Take the newer of the current POM version and the given version to merge, according to the SemVer precedence.

2. If the current POM version is newer, no change will be made and the current POM version is the merge result.
   Otherwise, if the given version to merge is newer, then, to form the merge result, the given version is to be
   incremented on the **intended change category of the current POM version** - `major`, `minor`, or `patch`.

3. The current POM version's pre-release and build metadata labels, if any exist, always stay and serve as the final
   merged version's labels.

In this case, the given version `1.3.10-HOTFIX` is newer, so it is to be incremented to form the merge result. The
intended change category of the current POM version `1.2.0-SNAPSHOT+chi.1` is `minor`, so the given version
`1.3.10-HOTFIX` is incremented on its own `minor` category, resulting in `1.4.0` per SemVer spec. Lastly, the
current POM version's labels (in this case `SNAPSHOT` and `chi.1`), if any exist, always stay as they are. Thus, for
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
