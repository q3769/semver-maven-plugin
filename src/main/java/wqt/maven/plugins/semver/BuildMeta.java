package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 *
 * Mojo to increment build meta info portion of the SemVer text
 */
@Mojo(name = "buildmeta", defaultPhase = LifecyclePhase.NONE)
public class BuildMeta extends SemverMojo {

    /**
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        incrementBuildMeta();
    }

    private void incrementBuildMeta() throws MojoFailureException, MojoExecutionException {
        updatePomFile(requireValidSemVer(project.getVersion()).incrementBuildMetadata().toString());
    }

}
