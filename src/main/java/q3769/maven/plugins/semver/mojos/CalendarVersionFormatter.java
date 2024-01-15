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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import lombok.NonNull;
import org.apache.maven.plugin.MojoFailureException;
import q3769.maven.plugins.semver.SemverNormalVersion;

enum CalendarVersionFormatter {
    TO_YEAR("yyyy"),
    TO_MONTH("yyyyMM"),
    TO_DAY("yyyyMMdd"),
    TO_HOUR("yyyyMMddHH"),
    TO_MINUTE("yyyyMMddHHmm"),
    TO_SECOND("yyyyMMddHHmmss"),
    TO_MILLISECOND("yyyyMMddHHmmssSSS");

    private final String pattern;
    private transient DateTimeFormatter dateTimeFormatter;

    CalendarVersionFormatter(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @param original pom version
     * @param targetNormalVersion to increment
     * @return new instance incremented
     * @throws MojoFailureException if the original version's target category version is newer than one hour from now
     */
    public static Version calendarIncrement(Version original, @Nonnull SemverNormalVersion targetNormalVersion)
            throws MojoFailureException {
        long target = targetNormalVersion.getNormalVersionNumber(original);
        Instant now = Instant.now();
        for (CalendarVersionFormatter formatter : EnumSet.allOf(CalendarVersionFormatter.class)) {
            long updatedNormalVersion = formatter.format(now);
            if (updatedNormalVersion > target) {
                return targetNormalVersion.incrementTo(updatedNormalVersion, original);
            }
        }
        throw new MojoFailureException(
                new UnsupportedOperationException(
                        "Target " + targetNormalVersion + " version " + target + " in original semver " + original
                                + " is not supported for calendar style increment - it has to be older than current date in UTC zone"));
    }

    private DateTimeFormatter getDateTimeFormatter() {
        if (this.dateTimeFormatter == null) {
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.pattern);
        }
        return this.dateTimeFormatter;
    }

    long format(@NonNull Instant instant) {
        return Long.parseLong(getDateTimeFormatter().format(instant.atZone(ZoneOffset.UTC)));
    }
}
