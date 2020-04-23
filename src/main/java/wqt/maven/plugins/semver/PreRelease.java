package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 *
 * Mojo to increment prerelease portion of the SemVer text
 */
@Mojo(name = "p", defaultPhase = LifecyclePhase.NONE)
public class PreRelease extends Incrementer {

    @Override
    protected Version increment(Version original) throws MojoFailureException {
        try {
            return original.incrementPreReleaseVersion();
        } catch (Exception ex) {
            final String error = "Failed to increment pre-release info of original version: " + original + " - pre-release portion needs to exist and conform to SemVer format before increment";
            getLog().error(error, ex);
            throw new MojoFailureException(error, ex);
        }
    }
}
