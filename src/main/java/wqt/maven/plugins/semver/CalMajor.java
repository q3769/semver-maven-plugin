/*
 * The MIT License
 * Copyright 2020 Qingtian Wang.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package wqt.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Increments major version to current calendar date in basic ISO format. i.e. Minor and patch versions are reset to
 * zeros.
 * 
 * @author Qingtian Wang
 */
@Mojo(name = "calMajor", defaultPhase = LifecyclePhase.NONE)
public class CalMajor extends NormalNumberIncrementer {

    /**
     * @param original POM project version whose major number is to be incremented
     * @return New semver version whose major number is incremented to current date in basic ISO format. Note that
     *         repeated calls during the same calendar day will silently succeed, resetting minor and patch versions to
     *         zeros.
     */
    @Override
    protected Version incrementNormalNumber(Version original) {
        Version newVersion = new Version.Builder(LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE) + ".0.0").build();
        final int newMajor = newVersion.getMajorVersion();
        final int originalMajor = original.getMajorVersion();
        if (newMajor < originalMajor) {
            throw new IllegalStateException("New major version : " + newMajor + " cannot be smaller than original : "
                    + originalMajor);
        }
        return newVersion;
    }

}
