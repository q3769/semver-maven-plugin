/*
 * MIT License
 *
 * Copyright (c) 2020 Qingtian Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package q3769.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import javax.annotation.Nonnull;

/**
 * Enum representing the normal version categories (MAJOR, MINOR, PATCH) of a semantic version.
 * Provides methods to get the version number and increment the version to a target value.
 *
 * <p>Each enum constant overrides the methods to provide specific behavior for the respective
 * version category.
 *
 * @author Qingtian Wang
 */
public enum SemverNormalVersion {

  /** Major version category. */
  MAJOR {
    /**
     * Gets the major version number from the given semantic version.
     *
     * @param semver the semantic version
     * @return the major version number
     */
    @Override
    public long getNormalVersionNumber(Version semver) {
      return semver.majorVersion();
    }

    /**
     * Increments the major version to the specified target value.
     *
     * @param target the target major version number
     * @param semver the original semantic version
     * @return the new semantic version with the major number incremented
     * @throws IllegalArgumentException if the original major version is already higher than the
     *     target
     */
    @Override
    public Version incrementTo(long target, Version semver) {
      if (semver.majorVersion() >= target) {
        throw incrementError(this, target, semver);
      }
      return Version.of(target);
    }
  },

  /** Minor version category. */
  MINOR {
    /**
     * Gets the minor version number from the given semantic version.
     *
     * @param semver the semantic version
     * @return the minor version number
     */
    @Override
    public long getNormalVersionNumber(Version semver) {
      return semver.minorVersion();
    }

    /**
     * Increments the minor version to the specified target value.
     *
     * @param target the target minor version number
     * @param semver the original semantic version
     * @return the new semantic version with the minor number incremented
     * @throws IllegalArgumentException if the original minor version is already higher than the
     *     target
     */
    @Override
    public Version incrementTo(long target, Version semver) {
      if (semver.minorVersion() >= target) {
        throw SemverNormalVersion.incrementError(this, target, semver);
      }
      return Version.of(semver.majorVersion(), target);
    }
  },

  /** Patch version category. */
  PATCH {
    /**
     * Gets the patch version number from the given semantic version.
     *
     * @param semver the semantic version
     * @return the patch version number
     */
    @Override
    public long getNormalVersionNumber(Version semver) {
      return semver.patchVersion();
    }

    /**
     * Increments the patch version to the specified target value.
     *
     * @param target the target patch version number
     * @param semver the original semantic version
     * @return the new semantic version with the patch number incremented
     * @throws IllegalArgumentException if the original patch version is already higher than the
     *     target
     */
    @Override
    public Version incrementTo(long target, Version semver) {
      if (semver.patchVersion() >= target) {
        throw SemverNormalVersion.incrementError(this, target, semver);
      }
      return Version.of(semver.majorVersion(), semver.minorVersion(), target);
    }
  };

  /**
   * Creates an exception indicating that the increment target is invalid.
   *
   * @param semverNormalVersion the version category
   * @param target the target version number
   * @param semver the original semantic version
   * @return the exception to be thrown
   */
  private static IllegalArgumentException incrementError(
      SemverNormalVersion semverNormalVersion, long target, Version semver) {
    return new IllegalArgumentException(semverNormalVersion + " version of " + semver
        + " is already higher than its increment target " + target);
  }

  /**
   * Gets the last incremented normal version category from the given semantic version.
   *
   * @param version the semantic version
   * @return the last incremented normal version category
   * @throws IllegalArgumentException if all version numbers are zero
   */
  public static @Nonnull SemverNormalVersion getLastIncrementedNormalVersion(
      @Nonnull Version version) {
    final long major = version.majorVersion();
    final long minor = version.minorVersion();
    final long patch = version.patchVersion();
    if (major == 0 && minor == 0 && patch == 0) {
      throw new IllegalArgumentException(
          "At least one normal number is expected to be non-zero: " + version);
    }
    if (minor == 0 && patch == 0) {
      return MAJOR;
    }
    if (patch == 0) {
      return MINOR;
    }
    return PATCH;
  }

  /**
   * Gets the version number of the corresponding category from the given semantic version.
   *
   * @param semver the semantic version
   * @return the version number of the corresponding category
   */
  public abstract long getNormalVersionNumber(Version semver);

  /**
   * Increments the version number of the corresponding category to the specified target value.
   *
   * @param target the target version number
   * @param semver the original semantic version
   * @return the new semantic version with the version number incremented
   */
  public abstract Version incrementTo(long target, Version semver);
}
