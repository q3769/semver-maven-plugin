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

/**
 * @author Qingtian Wang
 *         Updates POM version, based on current value
 */
public abstract class Updater extends SemverMojo {

    /**
     * @return The incremented SemVer
     * @throws MojoFailureException if original version in POM is malformed
     */
    @Override
    protected Version targetVersion() throws MojoFailureException {
        return update(requireValidSemVer(project.getVersion()));
    }

    /**
     * @param original SemVer to be updated
     * @return the incremented result SemVer
     * @throws MojoFailureException on build error
     */
    abstract protected Version update(Version original) throws MojoFailureException;

}
