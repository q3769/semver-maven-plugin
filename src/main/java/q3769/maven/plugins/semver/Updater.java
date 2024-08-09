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
import lombok.NonNull;
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
   * Flag to append SNAPSHOT as the prerelease label in the target version. Expected to be passed in
   * as a -D parameter from CLI.
   */
  @Parameter(property = "snapshot", defaultValue = "false")
  protected boolean addingSnapshotLabel;
  /** */
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
    Version updatedVersion = update(requireValidSemVer(project.getVersion()));
    if (!addingSnapshotLabel) {
      return updatedVersion;
    }
    if (hasPreReleaseVersionOrBuildMetadata(updatedVersion)) {
      throw new MojoFailureException(String.format(
          "snapshot labeling requested for updated semver %s but not honored, because snapshot flag only supports normal version number increments with no labels",
          updatedVersion));
    }
    getLog().info(String.format("labeling version %s as a SNAPSHOT...", updatedVersion));
    return addSnapshotLabel(updatedVersion);
  }

  private static boolean hasPreReleaseVersionOrBuildMetadata(@NonNull Version version) {
    return version.preReleaseVersion().isPresent() || version.buildMetadata().isPresent();
  }

  private static Version addSnapshotLabel(@NonNull Version version) {
    return version.toBuilder().setPreReleaseVersion(SNAPSHOT).build();
  }

  /**
   * @param newVersion New version to be set in the POM file
   * @throws MojoExecutionException if unexpected error occurred while updating the POM file
   */
  private void updatePomFile(@NonNull String newVersion) throws MojoExecutionException {
    String originalVersion = project.getVersion();
    String executedGoal = mojo.getGoal();
    if (newVersion.equals(originalVersion)) {
      getLog()
          .info(String.format(
              "Original POM version: %s remains unchanged after executing goal: %s",
              originalVersion, executedGoal));
      return;
    }
    executeMojo(
        plugin(
            groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.17.1")),
        goal("set"),
        configuration(
            element(name("generateBackupPoms"), "false"), element(name("newVersion"), newVersion)),
        executionEnvironment(project, session, pluginManager));
    getLog()
        .info(String.format(
            "Updated original POM version: %s into: %s after executing goal: %s",
            originalVersion, newVersion, executedGoal));
  }
}
