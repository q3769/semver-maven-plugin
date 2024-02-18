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

/** @author Qingtian Wang */
public enum SemverNormalVersion {

    /** */
    MAJOR {
        @Override
        public long getNormalVersionNumber(Version semver) {
            return semver.majorVersion();
        }

        @Override
        public Version incrementTo(long target, Version semver) {
            if (semver.majorVersion() >= target) {
                throw incrementError(this, target, semver);
            }
            return Version.of(target);
        }
    },

    /** */
    MINOR {
        @Override
        public long getNormalVersionNumber(Version semver) {
            return semver.minorVersion();
        }

        @Override
        public Version incrementTo(long target, Version semver) {
            if (semver.minorVersion() >= target) {
                throw SemverNormalVersion.incrementError(this, target, semver);
            }
            return Version.of(semver.majorVersion(), target);
        }
    },

    /** */
    PATCH {
        @Override
        public long getNormalVersionNumber(Version semver) {
            return semver.patchVersion();
        }

        @Override
        public Version incrementTo(long target, Version semver) {
            if (semver.patchVersion() >= target) {
                throw SemverNormalVersion.incrementError(this, target, semver);
            }
            return Version.of(semver.majorVersion(), semver.minorVersion(), target);
        }
    };

    private static IllegalArgumentException incrementError(
            SemverNormalVersion semverNormalVersion, long target, Version semver) {
        return new IllegalArgumentException(semverNormalVersion + " version of " + semver
                + " is already higher than its increment target " + target);
    }
    /**
     * @param version to check
     * @return the normal version to which the specified semver was incremented from its previous semver
     */
    public static @Nonnull SemverNormalVersion getLastIncrementedNormalVersion(@Nonnull Version version) {
        final long major = version.majorVersion();
        final long minor = version.minorVersion();
        final long patch = version.patchVersion();
        if (major == 0 && minor == 0 && patch == 0) {
            throw new IllegalArgumentException("At least one normal number is expected to be non-zero: " + version);
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
     * @param semver to get category int from
     * @return the int version of the corresponding category of the specified semver
     */
    public abstract long getNormalVersionNumber(Version semver);

    /**
     * @param target intended target
     * @param semver original version
     * @return new version instance with corresponding category number incremented to the specified target
     */
    public abstract Version incrementTo(long target, Version semver);
}
