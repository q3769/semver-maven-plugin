# semver-maven-plugin

Unlike some other Maven plugins, this one only addresses the concern of versioning, and not that of release. Release is a different and larger-scoped concern. This plugin operates on the value of the version element in the local POM file, according to the SemVer spec; it does nothing else.


## Hard set

```
$ mvn semver:set -Dsemver=blah
# errors out because blah is not a valid SemVer text
```

```
$ mvn semver:set -Dsemver=1.2.3-beta
# sets the value of the version element in the POM file to be 1.2.3-beta
```

## Increment normal release digit

```
$ mvn semver:major
# increments 1.2.3-beta.1 into 2.0.0
```

```
$ mvn semver:minor -Dsnapshot=true
# increments 1.2.3-beta.1 into 1.3.0-SNAPSHOT
```

```
$ mvn semver:patch
# increments 1.2.3-beta.1 into 1.2.4
```

## Increment pre-release or build meta portion

```
$ mvn semver:pre
# increments 1.2.3-beta into 1.2.3-beta.1
```

```
$ mvn semver:buildmeta
# increments 1.2.3-beta.1+build.10 into 1.2.3-beta.1+build.11
```
