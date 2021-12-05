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
package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author Qingtian Wang
 *         Mojo to strip off all additional labels of the SemVer, leaving the normal numbers untouched for final
 *         version.
 */
@Mojo(name = "final", defaultPhase = LifecyclePhase.NONE)
public class Final extends Updater {

    /**
     * @param original to finalize
     * @return final SemVer version of the original, all meta info stripped
     * @throws MojoFailureException if the original SemVer is already without additional labels
     */
    @Override
    protected Version update(Version original) throws MojoFailureException {
        if (StringUtils.isBlank(original.getPreReleaseVersion()) && StringUtils.isBlank(original.getBuildMetadata())) {
            final String error = "Failed to strip additional labels from version: " + original;
            getLog().error(error);
            throw new MojoFailureException(error, new IllegalArgumentException("Original version: " + original
                    + " has no additional labels"));
        }
        return Version.forIntegers(original.getMajorVersion(), original.getMinorVersion(), original.getPatchVersion());
    }

}
