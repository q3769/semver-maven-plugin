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
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Increment one of the three numbers in the normal release portion of the SemVer text
 * 
 * @author Qingtian Wang
 */
public abstract class NormalNumberIncrementer extends Updater {

    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String HYPHEN = "-";

    /**
     * Flag to append SNAPSHOT as the prerelease label in the target version. Expected to be passed in as a -D
     * parameter from CLI.
     */
    @Parameter(property = "snapshot", defaultValue = "false", required = false)
    protected boolean labelSnapshot;

    /**
     * @param original from POM
     * @return result SemVer by incrementing one of the normal digits of the original version
     */
    @Override
    protected Version update(Version original) throws MojoFailureException {
        Version incremented = incrementNormalNumber(original);
        if (!labelSnapshot) {
            return incremented;
        }
        final String preReleaseLabel = incremented.getPreReleaseVersion();
        if (StringUtils.isBlank(preReleaseLabel)) {
            return incremented.setPreReleaseVersion(SNAPSHOT);
        }
        if (preReleaseLabel.endsWith(SNAPSHOT)) {
            return incremented;
        }
        return incremented.setPreReleaseVersion(preReleaseLabel + HYPHEN + SNAPSHOT);
    }

    /**
     * @param original Local POM version whose normal numbers is about to be incremented
     * @return target version to update POM
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    protected abstract Version incrementNormalNumber(Version original) throws MojoFailureException;

}
