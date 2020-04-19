package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "major", defaultPhase = LifecyclePhase.NONE)
public class Major extends IncrementNormal {

    @Override
    protected void increment(Version original) throws MojoFailureException, MojoExecutionException {
        incrementMajor(original);
    }

    private void incrementMajor(Version original) throws MojoExecutionException {
        updatePomFileVersion((snapshot ? original.incrementMajorVersion(SNAPSHOT) : original.incrementMajorVersion()).toString());
    }

}
