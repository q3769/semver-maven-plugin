package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "set", defaultPhase = LifecyclePhase.NONE)
public class Set extends SemverMojo {

    /**
     * Expected to be passed in as a -D parameter in CLI. Needs to be in valid SemVer format.
     */
    @Parameter(property = "semver", defaultValue = "0.0.0-SNAPSHOT", required = true)
    protected String semver;

    /**
     *
     * @throws MojoExecutionException if error on updating POM
     * @throws MojoFailureException if target SemVer input is invalid
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        updatePomFile(requireValidSemVer(semver).toString());
    }

}
