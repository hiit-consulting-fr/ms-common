/*
 * MIT License
 *
 * Copyright (c) 2025 Hi!T Consulting
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
 *
 */

package fr.hiitconsulting.socle.domain.shared.validator;

import java.util.Collection;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@UtilityClass
public class DomainValidation {

  private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(?<!\\.)\\.[a-zA-Z]{2,}$";

  private static boolean isNotNull(@Nullable Object obj) {
    return obj != null;
  }

  private static boolean isNotBlank(@Nullable String str) {
    return isNotNull(str) && !str.trim().isEmpty();
  }

  public static <T> void notNull(String fieldName, @Nullable T value) {
    validate(value != null, "Le champ [" + fieldName + "='" + value + "'] ne doit pas être null.");
  }

  public static <T> T requireNotNull(String fieldName, @Nullable T value) {
    notNull(fieldName, value);
    assert value != null;
    return value;
  }

  public static void notBlank(String fieldName, @Nullable String value) {
    validate(isNotBlank(value),
        "Le champ [" + fieldName + "='" + value + "'] ne doit pas être null ou vide.");
  }

  public static String requireNotBlank(String fieldName, @Nullable String value) {
    notBlank(fieldName, value);
    assert value != null;
    return value;
  }

  public static <T> void notEmpty(String fieldName, @Nullable Collection<T> value) {
    validate(isNotNull(value) && !value.isEmpty(),
        "Le champ [" + fieldName + "='" + value + "'] ne doit pas être null ou une liste vide.");
  }

  public static <T> Collection<T> requireNotEmpty(String fieldName, @Nullable Collection<T> value) {
    notEmpty(fieldName, value);
    assert value != null;
    return value;
  }

  public static void validEmail(String fieldName, String value) {
    validate(
        isNotBlank(value) && Pattern.matches(EMAIL_REGEX, value),
        "Le champ [" + fieldName + "='" + value + "']  n'est pas une adresse email valide.");
  }

  private static void validate(boolean isValid, String errorMessage) {
    if (!isValid) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
