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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import q3769.maven.plugins.semver.Updater;

/**
 * Compares this POM's version with another SemVer passed in as parameter, and pick the newer of the two versions as the
 * updated POM version.
 *
 * @author Qingtian Wang
 */
@Mojo(name = "pick-newer", defaultPhase = LifecyclePhase.NONE)
public class PickNewer extends Updater {

    /**
     * The other SemVer to be merged with current local POM version
     */
    @Parameter(property = "semver", defaultValue = "NOT_SET", required = true) protected String otherSemVer;

    @Override
    protected Version update(Version original) throws MojoFailureException {
        getLog().debug("Taking the newer between current version " + original + " and given version " + otherSemVer);
        final Version other = requireValidSemVer(otherSemVer);
        if (original.greaterThanOrEqualTo(other)) {
            getLog().debug("Current POM version " + original + " is newer and being picked");
            return original;
        }
        getLog().debug("CLI provided version " + otherSemVer + " is newer and being picked");
        return other;
    }
}
