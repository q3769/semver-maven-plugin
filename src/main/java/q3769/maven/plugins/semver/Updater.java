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
package q3769.maven.plugins.semver;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Updates POM version, based on current value
 *
 * @author Qingtian Wang
 */
public abstract class Updater extends SemverMojo {
    private static final String SNAPSHOT = "SNAPSHOT";
    /**
     * Flag to append SNAPSHOT as the prerelease label in the target version. Expected to be passed in as a -D parameter
     * from CLI.
     */
    @Parameter(property = "snapshot", defaultValue = "false")
    protected boolean addingSnapshotLabel;
    /**
     *
     */
    @Component
    protected BuildPluginManager pluginManager;

    /**
     * @param original SemVer to be updated
     * @return the incremented result SemVer
     * @throws MojoFailureException on build error
     */
    protected abstract Version update(Version original) throws MojoFailureException;

    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        updatePomFile(getUpdatedVersion().toString());
    }

    /**
     * @return The incremented SemVer
     * @throws MojoFailureException if original version in POM is malformed
     */
    private Version getUpdatedVersion() throws MojoFailureException {
        Version updated = update(requireValidSemVer(project.getVersion()));
        if (!addingSnapshotLabel) {
            return updated;
        }
        if (updated.preReleaseVersion().isPresent() || updated.buildMetadata().isPresent()) {
            throw new MojoFailureException(
                    "snapshot labeling requested for updated semver " + updated
                            + " but not honored, because snapshot flag only supports normal version number increments with no labels");
        }
        getLog().info("labeling version " + updated + " as a SNAPSHOT...");
        return updated.toBuilder().setPreReleaseVersion(SNAPSHOT).build();
    }

    /**
     * @param version New version to be set in the POM file
     * @throws MojoExecutionException if unexpected error occurred while updating the POM file
     */
    private void updatePomFile(String version) throws MojoExecutionException {
        String original = project.getVersion();
        final String executedGoal = mojo.getGoal();
        if (version.equals(original)) {
            getLog().info("Original POM version: " + original + " remains unchanged after executing goal: "
                    + executedGoal);
            return;
        }
        executeMojo(
                plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.16.2")),
                goal("set"),
                configuration(element(name("generateBackupPoms"), "false"), element(name("newVersion"), version)),
                executionEnvironment(project, session, pluginManager));
        getLog().info("Updated original POM version: " + original + " into: " + version + " after executing goal: "
                + executedGoal);
    }
}
