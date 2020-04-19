package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "pre", defaultPhase = LifecyclePhase.NONE)
public class PreRelease extends SemverMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        incrementPreRelease();
    }

    private void incrementPreRelease() throws MojoFailureException, MojoExecutionException {
        updatePomFileVersion(requireValidSemVer(project.getVersion()).incrementPreReleaseVersion().toString());
    }

}
