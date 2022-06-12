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
package q3769.maven.plugins.semver.mojos;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import q3769.maven.plugins.semver.SemverMojo;

/**
 * Hard-sets to new SemVer, ignoring current version in POM
 *
 * @author Qingtian Wang
 */
@Mojo(name = "set-current", defaultPhase = LifecyclePhase.NONE) public class SetCurrent extends SemverMojo {

    /**
     * Expected to be passed in as a -D parameter in CLI. Needs to be in valid SemVer format.
     */
    @Parameter(property = "semver", required = true) protected String semver;

    /**
     * @return target SemVer per user CLI parameter
     * @throws MojoFailureException if input target version is malformed per SemVer spec
     */
    @Override protected Version getUpdatedVersion() throws MojoFailureException {
        return requireValidSemVer(semver);
    }

}
