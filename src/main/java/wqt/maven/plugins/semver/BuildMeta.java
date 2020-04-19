package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "buildmeta", defaultPhase = LifecyclePhase.NONE)
public class BuildMeta extends SemverMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        incrementBuildMeta();
    }

    private void incrementBuildMeta() throws MojoFailureException, MojoExecutionException {
        updatePomFileVersion(requireValidSemVer(project.getVersion()).incrementBuildMetadata().toString());
    }

}
