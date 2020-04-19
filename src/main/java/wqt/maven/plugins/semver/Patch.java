package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "patch", defaultPhase = LifecyclePhase.NONE)
public class Patch extends IncrementNormal {

    /**
     *
     * @param original
     * @throws MojoExecutionException
     */
    @Override
    protected void increment(Version original) throws MojoExecutionException {
        incrementPatch(original);
    }

    private void incrementPatch(Version original) throws MojoExecutionException {
        updatePomFile((snapshot ? original.incrementPatchVersion(SNAPSHOT) : original.incrementPatchVersion()).toString());
    }

}
