package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

/**
 *
 * @author Qingtian Wang
 */
public abstract class SemverMojo extends AbstractMojo {

    protected static final String SNAPSHOT = "SNAPSHOT";

    /**
     *
     * @param semver text that is supposed to be valid in terms of SemVer spec
     * @return A valid SemVer
     * @throws MojoFailureException if the input text is invalid per SemVer spec
     */
    protected static Version requireValidSemVer(String semver) throws MojoFailureException {
        try {
            return Version.valueOf(semver);
        } catch (IllegalArgumentException | ParseException ex) {
            throw new MojoFailureException("Error parsing semver: " + semver, ex);
        }

    }

    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(property = "session", defaultValue = "${session}", readonly = true, required = true)
    protected MavenSession session;

    @Component
    private BuildPluginManager pluginManager;

    /**
     *
     * @param version New version to be set in the POM file
     * @throws MojoExecutionException if unexpected error occurred while updating the POM file
     */
    protected void updatePomFile(String version) throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("org.codehaus.mojo"),
                        artifactId("versions-maven-plugin"),
                        version("2.7")
                ),
                goal("set"),
                configuration(
                        element(name("generateBackupPoms"), "false"),
                        element(name("newVersion"), version)
                ),
                executionEnvironment(
                        project,
                        session,
                        pluginManager
                ));
    }

}
