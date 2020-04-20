package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "major", defaultPhase = LifecyclePhase.NONE)
public class Major extends NormalDigitIncrementer {

    /**
     *
     * @param original version whose major digit is to be incremented
     */
    @Override
    protected Version incrementNormalDigit(Version original) {
        return incrementMajor(original);
    }

    private Version incrementMajor(Version original) {
        return snapshot ? original.incrementMajorVersion(SNAPSHOT) : original.incrementMajorVersion();
    }

}
