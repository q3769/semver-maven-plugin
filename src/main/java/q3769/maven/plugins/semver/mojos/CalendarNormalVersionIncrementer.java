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
import q3769.maven.plugins.semver.NormalVersion;

enum CalendarNormalVersionIncrementer {
  TO_YEAR("yyyy"),
  TO_MONTH("yyyyMM"),
  TO_DAY("yyyyMMdd"),
  TO_HOUR("yyyyMMddHH"),
  TO_MINUTE("yyyyMMddHHmm"),
  TO_SECOND("yyyyMMddHHmmss"),
  TO_MILLISECOND("yyyyMMddHHmmssSSS");

  private final DateTimeFormatter dateTimeFormatter;

  CalendarNormalVersionIncrementer(String pattern) {
    this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
  }

  /**
   * @param original pom version
   * @param selectedNormalVersion to increment
   * @return new instance incremented to current date in UTC zone
   */
  public static Version calendarIncrement(Version original, NormalVersion selectedNormalVersion) {
    long selectedNormalVersionNumber = selectedNormalVersion.getNumber(original);
    Instant now = Instant.now();
    for (CalendarNormalVersionIncrementer formatter : values()) {
      long updatedNormalVersionNumber = formatter.format(now);
      if (updatedNormalVersionNumber > selectedNormalVersionNumber) {
        return selectedNormalVersion.incrementTo(updatedNormalVersionNumber, original);
      }
    }
    throw new IllegalArgumentException(String.format(
        "%s version %s in POM semver %s is not supported for calendar style increment - it has to be older than current date in UTC zone",
        selectedNormalVersion, selectedNormalVersionNumber, original));
  }

  long format(Instant instant) {
    return Long.parseLong(dateTimeFormatter.format(instant.atZone(ZoneOffset.UTC)));
  }
}
