package wqt.maven.plugins.semver;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "set", defaultPhase = LifecyclePhase.NONE)
public class Set extends SemverMojo {

    @Parameter(property = "semver", defaultValue = "0.0.0-SNAPSHOT", required = true)
    protected String semver;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        updatePomFileVersion(requireValidSemVer(semver).toString());
    }

}
