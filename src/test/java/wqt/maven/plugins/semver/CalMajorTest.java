/*
 * The MIT License
 *
 * Copyright 2021 Qingtian Wang.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
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
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

/**
 *
 * @author Qingtian Wang
 */
public class CalMajorTest extends TestCase {

    private static final String TODAY = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    private static final Version EXPECTED_RESULT = new Version.Builder(TODAY + ".0.0").build();
    private final CalMajor instance = new CalMajor();

    public void testShouldIncrementMojorToToday() {
        final int someDayEarlier = Integer.valueOf(TODAY) - 10000;
        Version original = new Version.Builder(someDayEarlier + ".2.3").build();
        Version result = instance.incrementNormalNumber(original);
        assertEquals(EXPECTED_RESULT, result);
    }

    public void testShouldResetMinorAndPatchOnRepeatedSameDayCall() {
        Version original = new Version.Builder(Integer.valueOf(TODAY) + ".2.3").build();
        Version result = instance.incrementNormalNumber(original);
        assertEquals(EXPECTED_RESULT, result);
    }

    public void testShouldErrorOutIfOriginalMajorVersionIsHigher() {
        final int someLaterDay = Integer.valueOf(TODAY) + 10000;
        Version original = new Version.Builder(someLaterDay + ".2.3").build();
        try {
            instance.incrementNormalNumber(original);
            fail("Should have errored out");
        } catch (IllegalStateException ex) {
            System.out.println("Error as expected : " + ex.getMessage());
        }
    }

}
