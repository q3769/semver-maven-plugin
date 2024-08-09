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

package q3769.maven.plugins.semver;

import com.github.zafarkhaja.semver.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract class for updating semantic version labels.
 *
 * <p>This class provides methods to increment or set labels on a semantic version. Subclasses must
 * implement the {@link #incrementLabel(Version)} and {@link #setLabel(Version, String)} methods.
 *
 * @author Qingtian Wang
 */
public abstract class LabelUpdater extends Updater {

  /**
   * The label to set on the semantic version. If this parameter is provided, the label will be set
   * instead of incremented.
   */
  @Parameter(property = "set")
  protected String set;

  /**
   * Increments the label of the given semantic version.
   *
   * @param version the semantic version to increment
   * @return the incremented semantic version
   */
  protected abstract Version incrementLabel(Version version);

  /**
   * Sets the label of the given semantic version to the specified value.
   *
   * @param version the semantic version to be set with a new label
   * @param label the new label to set
   * @return the semantic version with the newly set label
   */
  protected abstract Version setLabel(Version version, String label);

  /**
   * Updates the semantic version by either incrementing or setting its label.
   *
   * <p>If the {@code set} parameter is blank, the label is incremented. Otherwise, the label is set
   * to the specified value.
   *
   * @param original the original semantic version
   * @return the updated semantic version
   */
  @Override
  protected Version update(Version original) {
    if (StringUtils.isBlank(set)) {
      logInfo("Incrementing label of version: %s", original);
      return incrementLabel(original);
    } else {
      logInfo("Setting label of version '%s' into '%s'...", original, set);
      return setLabel(original, set);
    }
  }

  private void logInfo(String message, Object... args) {
    getLog().info(String.format(message, args));
  }
}
