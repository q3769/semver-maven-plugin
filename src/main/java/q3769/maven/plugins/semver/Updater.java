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
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Updates POM version, based on current value
 *
 * @author Qingtian Wang
 */
public abstract class Updater extends SemverMojo {

    protected static final String HYPHEN = "-";
    protected static final String SNAPSHOT_SUFFIX = "SNAPSHOT";

    /**
     * Flag to append SNAPSHOT as the prerelease label in the target version. Expected to be passed in as a -D parameter
     * from CLI.
     */
    @Parameter(property = "snapshot", defaultValue = "false") protected boolean snapshot;
    @Component protected BuildPluginManager pluginManager;

    @Override protected void doExecute() throws MojoExecutionException, MojoFailureException {
        updatePomFile(getUpdatedVersion().toString());
    }

    /**
     * @return The incremented SemVer
     * @throws MojoFailureException if original version in POM is malformed
     */
    private Version getUpdatedVersion() throws MojoFailureException {
        Version updated = update(requireValidSemVer(project.getVersion()));
        if (!snapshot) {
            return updated;
        }
        final String preReleaseLabel = updated.getPreReleaseVersion();
        final String buildMetadataLabel = updated.getBuildMetadata();
        if (StringUtils.isBlank(preReleaseLabel)) {
            updated = updated.setPreReleaseVersion(SNAPSHOT_SUFFIX);
        } else if (!preReleaseLabel.contains(SNAPSHOT_SUFFIX)) {
            updated = updated.setPreReleaseVersion(preReleaseLabel + HYPHEN + SNAPSHOT_SUFFIX);
        }
        if (StringUtils.isBlank(buildMetadataLabel)) {
            return updated;
        }
        return updated.setBuildMetadata(buildMetadataLabel);
    }

    /**
     * @param original SemVer to be updated
     * @return the incremented result SemVer
     * @throws MojoFailureException on build error
     */
    protected abstract Version update(Version original) throws MojoFailureException;

    /**
     * @param version New version to be set in the POM file
     * @throws MojoExecutionException if unexpected error occurred while updating the POM file
     */
    private void updatePomFile(String version) throws MojoExecutionException {
        String original = project.getVersion();
        final String executedGoal = mojo.getGoal();
        if (version.equals(original)) {
            getLog().info(
                    "Original POM version: " + original + " remains unchanged after executing goal: " + executedGoal);
            return;
        }
        executeMojo(plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.10.0")),
                goal("set"),
                configuration(element(name("generateBackupPoms"), "false"), element(name("newVersion"), version)),
                executionEnvironment(project, session, pluginManager));
        getLog().info("Updated original POM version: " + original + " into: " + version + " after executing goal: "
                + executedGoal);
    }
}