/*
 * The MIT License
 * Copyright 2020 Qingtian Wang.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package qt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Merge this POM's version with another SemVer passed in as parameter. End result will the higher version of the two,
 * plus an increment according to the current version's category. Same labels from the current semver will be used for
 * the merged version.
 * 
 * @author Qingtian Wang
 */
@Mojo(name = "merge-from", defaultPhase = LifecyclePhase.NONE)
public class MergeFrom extends Updater {

    /**
     * The other SemVer to be merged with current local POM's version
     */
    @Parameter(property = "semver", defaultValue = "NOT_SET", required = false)
    protected String otherSemVer;

    @Override
    protected Version update(Version original) throws MojoFailureException {
        getLog().info("Merging current version: " + original + " from version: " + otherSemVer
                + ", result will keep labels of the original");
        final Version other = requireValidSemVer(otherSemVer);
        Version result = new Version.Builder(other.greaterThanOrEqualTo(original) ? other.getNormalVersion() : original
                .getNormalVersion()).build();
        final SemverCategory originalCategory = SemverCategory.getCategory(original);
        switch (originalCategory) {
            case MAJOR:
                result = result.incrementMajorVersion();
                break;
            case MINOR:
                result = result.incrementMinorVersion();
                break;
            case PATCH:
                result = result.incrementPatchVersion();
                break;
            default:
                throw new IllegalStateException("Unexpected: " + originalCategory);
        }
        final String originalPreRelease = original.getPreReleaseVersion();
        final String originaBuildMetadata = original.getBuildMetadata();
        if (StringUtils.isNotBlank(originalPreRelease))
            result = result.setPreReleaseVersion(originalPreRelease);
        if (StringUtils.isNotBlank(originaBuildMetadata))
            result = result.setBuildMetadata(originaBuildMetadata);
        return result;
    }
}
