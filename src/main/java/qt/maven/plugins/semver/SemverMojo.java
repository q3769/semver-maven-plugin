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
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Updates the POM file with a new SemVer version
 *
 * @author Qingtian Wang
 */
public abstract class SemverMojo extends AbstractMojo {

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

    @Parameter(defaultValue = "${mojoExecution}", readonly = true) protected MojoExecution mojo;

    @Parameter(property = "processAllModules", defaultValue = "false") protected boolean processAllModules;

    @Component private BuildPluginManager pluginManager;

    /**
     * @throws MojoExecutionException on execution error
     * @throws MojoFailureException   on build error
     */
    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        updatePomFile(getUpdatedVersion().toString());
    }

    /**
     * @return new target version to be set in the POM file
     * @throws MojoFailureException on build error
     */
    protected abstract Version getUpdatedVersion() throws MojoFailureException;

    /**
     * @param version New version to be set in the POM file
     * @throws MojoExecutionException if unexpected error occurred while updating the POM file
     */
    protected void updatePomFile(String version) throws MojoExecutionException {
        String original = project.getVersion();
        final String executedGoal = mojo.getGoal();
        if (version.equals(original)) {
            getLog().info(
                    "Original POM version: " + original + " remains unchanged after executing goal: " + executedGoal);
            return;
        }
        if (project.hasParent() && !processAllModules) {
            getLog().info(
                    "No modules will be processed; by default, only the parent project will be processed. If otherwise desired, use `-DprocessAllModules`");
            return;
        }
        executeMojo(plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.10.0")),
                goal("set"),
                configuration(element(name("generateBackupPoms"), "false"), element(name("newVersion"), version)),
                executionEnvironment(project, session, pluginManager));
        getLog().info("Updated original POM version: " + original + " into: " + version + " after executing goal: "
                + executedGoal);
    }

    /**
     * @param version text that is supposed to be valid in terms of SemVer spec
     * @return A valid SemVer
     * @throws MojoFailureException if input version text is malformed per SemVer spec
     */
    protected Version requireValidSemVer(String version) throws MojoFailureException {
        try {
            return Version.valueOf(version);
        } catch (Exception ex) {
            final String error = "Error forming SemVer from version: " + version;
            getLog().error(error, ex);
            throw new MojoFailureException(error,
                    new IllegalArgumentException("Not a valid SemVer text: " + version, ex));
        }
    }

}
