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
 * @author Qingtian Wang
 */
public enum SemverCategory {

    /**
     *
     */
    MAJOR {
        @Override
        public int getNormalInt(Version semver) {
            return semver.getMajorVersion();
        }

        @Override
        public Version incrementTo(int target, Version semver) {
            if (semver.getMajorVersion() >= target) {
                throw new IllegalArgumentException(this + incrementTargetError(target, semver));
            }
            return Version.forIntegers(target);
        }
    },

    /**
     *
     */
    MINOR {
        @Override
        public int getNormalInt(Version semver) {
            return semver.getMinorVersion();
        }

        @Override
        public Version incrementTo(int target, Version semver) {
            if (semver.getMinorVersion() >= target) {
                throw new IllegalArgumentException(this + incrementTargetError(target, semver));
            }
            return Version.forIntegers(semver.getMajorVersion(), target);
        }
    },

    /**
     *
     */
    PATCH {
        @Override
        public int getNormalInt(Version semver) {
            return semver.getPatchVersion();
        }

        @Override
        public Version incrementTo(int target, Version semver) {
            if (semver.getPatchVersion() >= target) {
                throw new IllegalArgumentException(this + incrementTargetError(target, semver));
            }
            return Version.forIntegers(semver.getMajorVersion(), semver.getMinorVersion(), target);
        }
    };

    /**
     * @param version
     *         to check
     * @return the change category the specified version intends to make
     */
    public static @Nonnull SemverCategory getIntendedChangeCategory(@Nonnull Version version) {
        final int major = version.getMajorVersion();
        final int minor = version.getMinorVersion();
        final int patch = version.getPatchVersion();
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

    private static @Nonnull String incrementTargetError(int target, Version semver) {
        return " version " + target + " too small to increment " + semver;
    }

    /**
     * @param semver
     *         to get category int from
     * @return the int version of the corresponding category of the specified semver
     */
    public abstract int getNormalInt(Version semver);

    /**
     * @param target
     *         intended target
     * @param semver
     *         original version
     * @return new version instance with corresponding category number incremented to the specified target
     */
    public abstract Version incrementTo(int target, Version semver);
}
