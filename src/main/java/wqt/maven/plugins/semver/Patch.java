package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 *
 * @author Qingtian Wang
 */
@Mojo(name = "patch", defaultPhase = LifecyclePhase.NONE)
public class Patch extends NormalDigitIncrementer {

    /**
     *
     * @param original semver whose patch digit is about to increment
     */
    @Override
    protected Version incrementNormalDigit(Version original) {
        return incrementPatch(original);
    }

    private Version incrementPatch(Version original) {
        return snapshot ? original.incrementPatchVersion(SNAPSHOT) : original.incrementPatchVersion();
    }

}
