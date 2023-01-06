/*
 * MIT License
 *
 * Copyright (c) 2023 Qingtian Wang
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
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import q3769.maven.plugins.semver.SemverCategory;
import q3769.maven.plugins.semver.Updater;

/**
 * Merge this POM's version with another SemVer passed in as parameter, and set the merge result as the updated POM
 * version. If the current POM version is newer, no update will be performed. Otherwise, the update version will be
 * decided this way: Take the intended SemVer category of the current POM version; increment the passed in SemVer on the
 * same category number as the currently intended category, and use the incremented result as the new POM version. The
 * pre-release and build metadata labels of the new POM version are the same as the original POM's.
 *
 * @author Qingtian Wang
 */
@Mojo(name = "merge", defaultPhase = LifecyclePhase.NONE)
public class Merge extends Updater {
    /**
     * The other SemVer to be merged with current local POM's version
     */
    @Parameter(property = "semver", defaultValue = "NOT_SET") protected String otherSemVer;

    private static Version setLabels(Version version, String preReleaseLabel, String buildMetadataLabel) {
        Version withLabels = version;
        if (StringUtils.isNotBlank(preReleaseLabel)) {
            withLabels = version.setPreReleaseVersion(preReleaseLabel);
        }
        if (StringUtils.isNotBlank(buildMetadataLabel)) {
            withLabels = version.setBuildMetadata(buildMetadataLabel);
        }
        return withLabels;
    }

    private Version increment(Version version, SemverCategory category) {
        getLog().debug("Incrementing version " + version + " on category " + category);
        switch (category) {
            case MAJOR:
                return version.incrementMajorVersion();
            case MINOR:
                return version.incrementMinorVersion();
            case PATCH:
                return version.incrementPatchVersion();
            default:
                throw new IllegalStateException("Unexpected category: " + category);
        }
    }

    @Override
    protected Version update(Version original) throws MojoFailureException {
        getLog().debug("Merging current version " + original + " with version " + otherSemVer
                + ", result will keep labels of the current...");
        final Version other = requireValidSemVer(otherSemVer);
        if (original.greaterThan(other)) {
            getLog().debug("Current POM version " + original + " is newer than given version " + other);
            return original;
        }
        getLog().debug("Provided version " + other + " is newer than current POM version " + original);
        Version result = increment(other, SemverCategory.getIntendedChangeCategory(original));
        result = setLabels(result, original.getPreReleaseVersion(), original.getBuildMetadata());
        getLog().info("Final merged version: " + result);
        return result;
    }
}
