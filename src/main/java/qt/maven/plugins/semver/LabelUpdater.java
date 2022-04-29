/*
 * The MIT License
 * Copyright 2021 Qingtian Wang.
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
 * @author Qingtian Wang
 */
public abstract class LabelUpdater extends Updater {

    /**
     * If passed in, will be used to set as either one of the two SemVer labels.
     */
    @Parameter(property = "set", defaultValue = "", required = false)
    protected String set;

    @Override
    protected Version update(Version original) throws MojoFailureException {
        final String updatePreReleaseOrBuildMetadata = mojo.getGoal();
        if (StringUtils.isBlank(set)) {
            getLog().info("Incrementing label of version: " + original + " in goal: "
                    + updatePreReleaseOrBuildMetadata);
            try {
                return incrementLabel(original);
            } catch (Exception ex) {
                final String error = "Failed to increment label of original version: " + original
                        + ", the target label in goal: " + updatePreReleaseOrBuildMetadata
                        + " needs to exist and conform to SemVer format before increment";
                getLog().error(error, ex);
                throw new MojoFailureException(error, ex);
            }
        }
        getLog().info("Setting label of version: " + original + " in goal: " + updatePreReleaseOrBuildMetadata
                + " into: " + set);
        return setLabel(original, set);
    }

    protected abstract Version incrementLabel(Version version);

    protected abstract Version setLabel(Version version, String label);

}