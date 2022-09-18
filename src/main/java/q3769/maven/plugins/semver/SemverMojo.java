/*
 * MIT License
 *
 * Copyright (c) 2022 Qingtian Wang
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
package q3769.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Updates the POM file with a new SemVer version
 *
 * @author Qingtian Wang
 */
public abstract class SemverMojo extends AbstractMojo {

    public static final String FALSE = "false";
    /**
     * Current Maven POM
     */
    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    /**
     * Default session
     */
    @Parameter(property = "session", defaultValue = "${session}", readonly = true, required = true)
    protected MavenSession session;

    @Parameter(defaultValue = "${mojoExecution}", readonly = true)
    protected MojoExecution mojo;

    @Parameter(property = "processModule", defaultValue = FALSE)
    protected String processModule;

    /**
     * @param version text that is supposed to be valid per SemVer spec
     * @return A valid SemVer
     */
    public static Version requireValidSemVer(String version) {
        try {
            return Version.valueOf(version);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error parsing '" + version + "' as a SemVer", ex);
        }
    }

    /**
     * @throws MojoExecutionException on execution error
     * @throws MojoFailureException   on build error
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String projectName = project.getName();
        String pomVersion = originalPomVersion();
        getLog().info("Goal '" + this.mojo.getGoal() + "' processing project '" + projectName + "' with POM version '"
                + pomVersion + "'...");
        if (project.hasParent()) {
            if (FALSE.equalsIgnoreCase(processModule)) {
                getLog().warn("Version of module '" + projectName
                        + "' will not be processed. By default, only parent project is processed; if otherwise desired, use the `-DprocessModule` CLI flag");
                return;
            }
            if (pomVersion == null) {
                getLog().warn("Version of module '" + projectName + "' is inherited to be the same as parent '"
                        + project.getParent().getName() + "', thus will not be processed independently");
                return;
            }
        }
        doExecute();
    }

    protected String originalPomVersion() {
        return project.getOriginalModel().getVersion();
    }

    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

}
