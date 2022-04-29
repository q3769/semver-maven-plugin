/*
 * The MIT License
 * Copyright 2021 Qingtian Wang.
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
package qt.maven.plugins.semver.mojos;

import com.github.zafarkhaja.semver.Version;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Qingtian Wang
 */
public class CalMajorTest {

    private static final Logger LOG = Logger.getLogger(CalMajorTest.class.getName());

    private static final String TODAY = LocalDate.now()
            .format(DateTimeFormatter.BASIC_ISO_DATE);
    private static final Version EXPECTED_RESULT = new Version.Builder(TODAY + ".0.0").build();
    private final CalendarMajor instance = new CalendarMajor();

    @Test
    public void testShouldIncrementMojorToToday() throws MojoFailureException {
        final int someDayEarlier = Integer.valueOf(TODAY) - 10000;
        Version original = new Version.Builder(someDayEarlier + ".2.3").build();
        Version result = instance.update(original);
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    public void testShouldErrorOutIfOriginalMajorVersionIsHigher() {
        final int someLaterDay = Integer.valueOf(TODAY) + 10000;
        Version original = new Version.Builder(someLaterDay + ".2.3").build();
        MojoFailureException assertThrows = Assertions.assertThrows(MojoFailureException.class, () -> {
            instance.update(original);
        });
        LOG.log(Level.INFO, "Expected message: {0}", assertThrows.getMessage());
    }

}