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

import com.github.zafarkhaja.semver.Version;
import lombok.NonNull;
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
  private static final String FALSE = "false";

  /** */
  @Parameter(defaultValue = "${mojoExecution}", readonly = true)
  protected MojoExecution mojo;

  /** */
  @Parameter(property = "processModule", defaultValue = FALSE)
  protected String processModule;

  /** Current Maven POM */
  @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
  protected MavenProject project;

  /** Default session */
  @Parameter(property = "session", defaultValue = "${session}", readonly = true, required = true)
  protected MavenSession session;

  /**
   * @param version text that is supposed to be valid per SemVer spec
   * @return A valid SemVer
   */
  public static @NonNull Version requireValidSemVer(String version) {
    try {
      return Version.parse(version);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Error parsing '" + version + "' as a SemVer", ex);
    }
  }

  /**
   * @throws MojoExecutionException if an unexpected problem occurs. Throwing this exception causes
   *     a "BUILD ERROR" message to be displayed.
   * @throws MojoFailureException if an expected problem (such as a compilation failure) occurs.
   *     Throwing this exception causes a "BUILD FAILURE" message to be displayed.
   */
  protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

  /**
   * @throws MojoExecutionException on execution error
   * @throws MojoFailureException on build error
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    String projectName = project.getName();
    String pomVersion = originalPomVersion();
    logInfo(
        "Goal '%s' processing project '%s' with POM version '%s'...",
        this.mojo.getGoal(), projectName, pomVersion);
    if (project.hasParent()) {
      logInfo(
          "current project %s is a module of %s",
          projectName, project.getParent().getName());
      if (FALSE.equalsIgnoreCase(processModule)) {
        logWarn(
            "Version of module '%s' will not be processed. By default, only parent project is processed; if otherwise desired, use the `-DprocessModule` CLI flag",
            projectName);
        return;
      }
      if (pomVersion == null) {
        logWarn(
            "Version of module '%s' is inherited to be the same as parent '%s', thus will not be processed independently",
            projectName, project.getParent().getName());
        return;
      }
    }
    doExecute();
  }

  /** @return original version in pom.xml */
  protected String originalPomVersion() {
    return project.getModel().getVersion();
  }

  protected void logError(String message, Object... args) {
    getLog().error(String.format(message, args));
  }

  protected void logError(Throwable t, String message, Object... args) {
    getLog().error(String.format(message, args), t);
  }

  protected void logWarn(String message, Object... args) {
    getLog().warn(String.format(message, args));
  }

  protected void logInfo(String message, Object... args) {
    getLog().info(String.format(message, args));
  }

  protected void logDebug(String message, Object... args) {
    getLog().debug(String.format(message, args));
  }
}
