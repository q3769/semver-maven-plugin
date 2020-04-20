package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 *
 * Mojo to increment build meta info portion of the SemVer text
 */
@Mojo(name = "buildmeta", defaultPhase = LifecyclePhase.NONE)
public class BuildMeta extends Incrementer {

    @Override
    protected Version increment(Version original) throws MojoFailureException {
        try {
            return original.incrementBuildMetadata();
        } catch (Exception ex) {
            final String error = "Failed to increment build meta info of original version: " + original + " - build meta portion needs to exist and conform to SemVer format before increment";
            getLog().error(error, ex);
            throw new MojoFailureException(error, ex);
        }
    }

}
