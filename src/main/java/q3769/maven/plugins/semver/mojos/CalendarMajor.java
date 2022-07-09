/*
 * MIT License
 *
 * Copyright (c) 2022 Qingtian Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package q3769.maven.plugins.semver.mojos;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import q3769.maven.plugins.semver.Updater;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Increments major version to current calendar date in basic ISO format. If the resulting version is newer than the
 * original POM version, up the POM version to the new one. Otherwise errors out.
 *
 * @author Qingtian Wang
 */
@Mojo(name = "calendar-major", defaultPhase = LifecyclePhase.NONE) public class CalendarMajor extends Updater {

    /**
     * @param original POM project version whose major number is to be incremented
     * @return New semver version whose major number is incremented to current date in basic ISO format. Error out
     */
    @Override protected Version update(Version original) throws MojoFailureException {
        Version newVersion =
                new Version.Builder(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".0.0").build();
        if (original.greaterThan(newVersion)) {
            throw new MojoFailureException(
                    "Original POM version: " + original + " is already newer than the intended update: " + newVersion);
        }
        return newVersion;
    }

}
