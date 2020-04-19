package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Qingtian Wang
 * 
 * Increment one of the three digits in the normal release portion of the SemVer text
 */
public abstract class IncrementNormal extends SemverMojo {

    /**
     * Flag to append SNAPSHOT as the prerelease text in the target SemVer version
     */
    @Parameter(property = "snapshot", defaultValue = "false", required = false)
    protected boolean snapshot;

    /**
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        increment(requireValidSemVer(project.getVersion()));
    }

    /**
     *
     * @param originalSemVer
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    abstract protected void increment(Version originalSemVer) throws MojoExecutionException, MojoFailureException;

}
