/*
 * MIT License
 *
 * Copyright (c) 2020 Qingtian Wang
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
import q3769.maven.plugins.semver.SemverCategory;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class CalendarSemverUtils {

    private static final DateTimeFormatter TO_UTC_DAY =
            DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);

    private CalendarSemverUtils() {
    }

    /**
     * Assumes version ints represent datetime in UTC zone
     *
     * @param original
     *         pom version
     * @param targetCategory
     *         to increment
     * @return new instance incremented
     * @throws MojoFailureException
     *         if the original version's target category version is newer than one hour from now
     */
    public static Version calendarIncrement(Version original, @Nonnull SemverCategory targetCategory)
            throws MojoFailureException {
        int target = targetCategory.getNormalInt(original);
        Instant now = Instant.now();
        int today = Integer.parseInt(TO_UTC_DAY.format(now));
        if (today > target) {
            return targetCategory.incrementTo(today, original);
        }
        throw new MojoFailureException(new UnsupportedOperationException(
                "Target " + targetCategory + " version " + target + " in original semver " + original
                        + " is not supported for calendar style increment - it has to be older than current date in UTC zone"));
    }
}
