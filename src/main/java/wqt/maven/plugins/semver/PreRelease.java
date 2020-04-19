package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 *
 * Mojo to increment prerelease portion of the SemVer text
 */
@Mojo(name = "pre", defaultPhase = LifecyclePhase.NONE)
public class PreRelease extends SemverMojo {

    /**
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        incrementPreRelease();
    }

    private void incrementPreRelease() throws MojoFailureException, MojoExecutionException {
        updatePomFile(requireValidSemVer(project.getVersion()).incrementPreReleaseVersion().toString());
    }

}
