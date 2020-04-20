package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "minor", defaultPhase = LifecyclePhase.NONE)
public class Minor extends NormalDigitIncrementer {

    /**
     *
     * @param original to be incremented on the minor digit
     */
    @Override
    protected Version incrementNormalDigit(Version original) {
        return incrementMinor(original);
    }

    private Version incrementMinor(Version original) {
        return snapshot ? original.incrementMinorVersion(SNAPSHOT) : original.incrementMinorVersion();
    }

}
