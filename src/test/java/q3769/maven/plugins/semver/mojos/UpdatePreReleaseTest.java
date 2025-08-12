package q3769.maven.plugins.semver.mojos;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.github.zafarkhaja.semver.Version;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdatePreReleaseTest {

  private UpdatePreRelease updatePreRelease;

  @BeforeEach
  void setUp() {
    updatePreRelease = new UpdatePreRelease();
  }

  @Nested
  @DisplayName("incrementLabel")
  class IncrementLabel {

    @Test
    @DisplayName("throws MojoFailureException when incrementing invalid version")
    void throwsExceptionWhenIncrementingInvalidVersion() {
      Version version = mock(Version.class);
      given(version.nextPreReleaseVersion())
          .willThrow(new IllegalStateException("Invalid version"));

      MojoFailureException exception =
          assertThrows(MojoFailureException.class, () -> updatePreRelease.incrementLabel(version));

      assertTrue(exception.getMessage().contains("Failed to increment pre-release label"));
    }
  }

  @Nested
  @DisplayName("setLabel")
  class SetLabel {

    @Test
    @DisplayName("sets pre-release label successfully")
    void setsPreReleaseLabelSuccessfully() throws MojoFailureException {
      Version version = Version.parse("1.0.0-alpha.1");
      Version result = updatePreRelease.setLabel(version, "beta");
      assertEquals("1.0.0-beta", result.toString());
    }

    @Test
    void throwsExceptionWhenSettingLabelIsBlank() {
      Version version = Version.parse("1.0.0-alpha.1");

      var exception =
          assertThrows(MojoFailureException.class, () -> updatePreRelease.setLabel(version, " "));

      assertThat(exception.getMessage()).contains("Label to set cannot be blank");
    }

    @Test
    @DisplayName("throws MojoFailureException when setting invalid label")
    void throwsExceptionWhenSettingInvalidLabel() {
      Version version = mock(Version.class);
      given(version.nextPreReleaseVersion(anyString()))
          .willThrow(new IllegalStateException("Invalid label"));

      MojoFailureException exception = assertThrows(
          MojoFailureException.class, () -> updatePreRelease.setLabel(version, "invalid"));

      assertThat(exception.getCause())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Invalid label");
    }
  }
}
