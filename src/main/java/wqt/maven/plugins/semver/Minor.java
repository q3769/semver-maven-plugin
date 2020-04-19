package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "minor", defaultPhase = LifecyclePhase.NONE)
public class Minor extends IncrementNormal {

    @Override
    protected void increment(Version original) throws MojoExecutionException {
        incrementMinor(original);
    }

    private void incrementMinor(Version original) throws MojoExecutionException {
        updatePomFileVersion((snapshot ? original.incrementMinorVersion(SNAPSHOT) : original.incrementMinorVersion()).toString());
    }

}
