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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Qingtian Wang
 */
class CalendarMajorTest {

    private static final DateTimeFormatter TO_UTC_DAY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);
    private final CalendarMajor instance = new CalendarMajor();

    @Test
    void testShouldErrorOutIfOriginalMajorVersionDateIsHigher() {
        final int futureDate = Integer.MAX_VALUE;
        Version original = new Version.Builder(futureDate + ".2.3").build();

        Assertions.assertThrows(MojoFailureException.class, () -> instance.update(original));
    }

    @Test
    void testShouldErrorOutIfOriginalMajorVersionDateIsToday() {
        final int futureDate = Integer.parseInt(TO_UTC_DAY_FORMATTER.format(Instant.now()));
        Version original = new Version.Builder(futureDate + ".2.3").build();

        Assertions.assertThrows(MojoFailureException.class, () -> instance.update(original)).printStackTrace();
    }

    @Test
    void testShouldIncrementMajorToNowWithNoHours() throws MojoFailureException {
        String expectedMajor = TO_UTC_DAY_FORMATTER.format(Instant.now());
        final int someDayEarlier = Integer.parseInt(expectedMajor) - 10000;
        Version original = new Version.Builder(someDayEarlier + ".2.3").build();

        Version result = instance.update(original);

        assertEquals(new Version.Builder(expectedMajor + ".0.0").build(), result);
    }
}
