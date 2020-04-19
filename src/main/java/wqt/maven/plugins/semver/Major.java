package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "major", defaultPhase = LifecyclePhase.NONE)
public class Major extends IncrementNormal {

    /**
     *
     * @param original version whose major digit is to be incremented
     * @throws MojoFailureException
     * @throws MojoExecutionException
     */
    @Override
    protected void increment(Version original) throws MojoFailureException, MojoExecutionException {
        incrementMajor(original);
    }

    private void incrementMajor(Version original) throws MojoExecutionException {
        updatePomFile((snapshot ? original.incrementMajorVersion(SNAPSHOT) : original.incrementMajorVersion()).toString());
    }

}
