package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "set", defaultPhase = LifecyclePhase.NONE)
public class Setter extends SemverMojo {

    /**
     * Expected to be passed in as a -D parameter in CLI. Needs to be in valid SemVer format.
     */
    @Parameter(property = "semver", defaultValue = "0.0.0-SNAPSHOT", required = true)
    protected String semver;

    @Override
    protected Version targetVersion() throws MojoFailureException {
        try {
            return requireValidSemVer(semver);
        } catch (Exception ex) {
            final String error = "Invalid version: " + semver + " - target version needs to conform to SemVer format";
            getLog().error(error, ex);
            throw new MojoFailureException(error, ex);
        }
    }

}
