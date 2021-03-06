/* 
 * The MIT License
 *
 * Copyright 2020 Qingtian Wang.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
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
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Qingtian Wang
 *
 * Increment one of the three numbers in the normal release portion of the SemVer text
 */
public abstract class NormalNumberIncrementer extends Updater {

    private static final String SNAPSHOT = "SNAPSHOT";

    /**
     * Flag to append SNAPSHOT as the prerelease label in the target version. Expected to be passed in by user as a CLI
     * -D parameter.
     */
    @Parameter(property = "snapshot", defaultValue = "false", required = false)
    protected boolean snapshot;

    /**
     *
     * @param original from POM
     * @return result SemVer by incrementing one of the normal digits of the original version
     */
    @Override
    protected Version update(Version original) {
        Version incremented = incrementNormalNumber(original);
        if (!snapshot) {
            return incremented;
        }
        return incremented.setPreReleaseVersion(SNAPSHOT);
    }

    /**
     *
     * @param original the SemVer one of whose normal numbers is about to be incremented
     * @return target version to update POM
     */
    abstract protected Version incrementNormalNumber(Version original);

}
