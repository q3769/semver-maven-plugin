package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author Qingtian Wang
 *
 * Increment one of the three digits in the normal release portion of the SemVer text
 */
public abstract class NormalDigitIncrementer extends Incrementer {

    protected static final String SNAPSHOT = "SNAPSHOT";

    /**
     * Flag to append SNAPSHOT as the prerelease text in the target SemVer version
     */
    @Parameter(property = "snapshot", defaultValue = "false", required = false)
    protected boolean snapshot;

    @Override
    protected Version increment(Version original) {
        return incrementNormalDigit(original);
    }

    /**
     *
     * @param originalSemVer
     * @return target version to update POM
     */
    abstract protected Version incrementNormalDigit(Version originalSemVer);

}
