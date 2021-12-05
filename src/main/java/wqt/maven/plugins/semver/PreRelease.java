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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author Qingtian Wang
 *         Mojo to increment prerelease portion of the SemVer text
 */
@Mojo(name = "pr", defaultPhase = LifecyclePhase.NONE)
public class PreRelease extends Updater {

    /**
     * @param original to be incremented on pre-release label
     * @return target SemVer by incrementing the pre-release label of the original
     * @throws MojoFailureException on build error
     */
    @Override
    protected Version update(Version original) throws MojoFailureException {
        try {
            return original.incrementPreReleaseVersion();
        } catch (Exception ex) {
            final String error = "Failed to increment pre-release info of original version: " + original
                    + " - pre-release portion needs to exist and conform to SemVer format before increment";
            getLog().error(error, ex);
            throw new MojoFailureException(error, ex);
        }
    }
}
