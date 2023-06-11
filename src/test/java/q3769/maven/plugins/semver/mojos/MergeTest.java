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

package q3769.maven.plugins.semver.mojos;

import com.github.zafarkhaja.semver.Version;
import elf4j.Logger;
import elf4j.engine.service.LogServiceManager;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import q3769.maven.plugins.semver.SemverCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static q3769.maven.plugins.semver.SemverCategory.MINOR;

class MergeTest {
    static final Logger info = Logger.instance().atInfo();

    Merge mergeMojo = new Merge();

    @AfterAll
    static void cleanup() {
        LogServiceManager.INSTANCE.stop();
    }

    @Nested
    class update {
        @Test
        void whenOriginalVersionIsNewer() throws MojoFailureException {
            Version original = Version.valueOf("1.4.0-SNAPSHOT");
            Version toMerge = Version.valueOf("1.3.4-hotfix");
            info.log("Merging " + toMerge + " to " + original);
            assertTrue(original.compareTo(toMerge) > 0);
            mergeMojo.otherSemVer = toMerge.toString();

            Version updated = mergeMojo.update(original);
            info.log("Merge result: " + updated);

            assertEquals(original, updated);
        }

        @Test
        void whenOriginalVersionIsOlder() throws MojoFailureException {
            Version original = Version.valueOf("1.2.0-SNAPSHOT");
            Version toMerge = Version.valueOf("1.3.4-hotfix");
            info.log("Merging " + toMerge + " to " + original);
            mergeMojo.otherSemVer = toMerge.toString();
            assertTrue(original.compareTo(toMerge) < 0);
            assertEquals(MINOR, SemverCategory.getIntendedChangeCategory(original));

            Version updated = mergeMojo.update(original);
            info.log("Merge result: " + updated);

            assertEquals(toMerge.incrementMinorVersion().setPreReleaseVersion(original.getPreReleaseVersion()),
                    updated);
        }
    }
}