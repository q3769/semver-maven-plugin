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
 * Mojo to increment build meta info portion of the SemVer text. If, however, the <code>set</code> parameter is passed
 * in, its value will be used to set as the build metadata label.
 * 
 * @author Qingtian Wang
 */
@Mojo(name = "build-metadata", defaultPhase = LifecyclePhase.NONE)
public class BuildMetadata extends Updater {

    /**
     * If passed in, will be used to set as the build metadata label.
     */
    @Parameter(property = "set", defaultValue = "", required = false)
    protected String set;

    /**
     * @param original from POM
     * @return updated to set in POM
     * @throws MojoFailureException on build error
     */
    @Override
    protected Version update(Version original) throws MojoFailureException {
        if (StringUtils.isBlank(set)) {
            getLog().info("Incrementing build metadata label of version: " + original);
            try {
                return original.incrementBuildMetadata();
            } catch (Exception ex) {
                final String error = "Failed to increment build meta info of original version: " + original
                        + " - build meta portion needs to exist and conform to SemVer format before increment";
                getLog().error(error, ex);
                throw new MojoFailureException(error, ex);
            }
        }
        getLog().info("Setting build metadata label of version: " + original + " into: " + set);
        return original.setBuildMetadata(set);
    }

}
