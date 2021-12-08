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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Merge this POM's version with another SemVer passed in as parameter. End result will the higher of the two per SemVer
 * spec.
 * 
 * @author Qingtian Wang
 */
@Mojo(name = "pick-newer", defaultPhase = LifecyclePhase.NONE)
public class PickNewer extends Updater {

    /**
     * The other SemVer to be merged with current local POM's version
     */
    @Parameter(property = "semver", defaultValue = "NOT_SET", required = true)
    protected String otherSemVer;

    @Override
    protected Version update(Version original) throws MojoFailureException {
        getLog().info("Taking the newer of current version: " + original + " and version: " + otherSemVer);
        final Version other = requireValidSemVer(otherSemVer);
        if (original.greaterThanOrEqualTo(other)) {
            getLog().info("Current version: " + original + " is picked");
            return original;
        }
        getLog().info("New version: " + otherSemVer + " is picked");
        return other;
    }

}
