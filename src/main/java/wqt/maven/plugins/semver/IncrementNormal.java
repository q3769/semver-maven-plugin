package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class IncrementNormal extends SemverMojo {

    @Parameter(property = "snapshot", defaultValue = "false", required = false)
    protected boolean snapshot;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        increment(requireValidSemVer(project.getVersion()));
    }

    abstract protected void increment(Version originalSemVer) throws MojoExecutionException, MojoFailureException;

}
