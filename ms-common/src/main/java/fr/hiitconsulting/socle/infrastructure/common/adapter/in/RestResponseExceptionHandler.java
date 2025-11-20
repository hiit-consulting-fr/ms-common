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

package fr.hiitconsulting.socle.infrastructure.common.adapter.in;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import fr.hiitconsulting.socle.domain.shared.exception.AbstractBadRequestException;
import fr.hiitconsulting.socle.domain.shared.exception.AbstractForbiddenException;
import fr.hiitconsulting.socle.domain.shared.exception.AbstractNotFoundException;
import fr.hiitconsulting.socle.infrastructure.common.adapter.in.dto.ErrorMessageQuery;
import fr.hiitconsulting.socle.infrastructure.common.adapter.in.dto.FieldErrorQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@NullMarked
public class RestResponseExceptionHandler {

  @ExceptionHandler(AbstractNotFoundException.class)
  public ResponseEntity<ErrorMessageQuery> handleNotFoundException(
      AbstractNotFoundException exception) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(new ErrorMessageQuery(exception.getMessage(), null));
  }

  @ExceptionHandler(AbstractForbiddenException.class)
  public ResponseEntity<ErrorMessageQuery> handleForbiddenException(
      AbstractForbiddenException exception) {
    return ResponseEntity
        .status(FORBIDDEN)
        .body(new ErrorMessageQuery(exception.getMessage(), null));
  }

  @ExceptionHandler(AbstractBadRequestException.class)
  public ResponseEntity<ErrorMessageQuery> handleBadRequestException(
      AbstractBadRequestException exception) {

    List<FieldErrorQuery> errors = Optional.ofNullable(exception.getErrors())
        .map(Map::entrySet).stream()
        .flatMap(Collection::stream)
        .map(entry -> new FieldErrorQuery(entry.getKey(), entry.getValue()))
        .toList();

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(new ErrorMessageQuery(exception.getMessage(), errors.isEmpty() ? null : errors));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessageQuery> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(new ErrorMessageQuery(exception.getMessage(), null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessageQuery> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<FieldErrorQuery> errors = ex.getBindingResult()
        .getFieldErrors().stream()
        .map(f -> new FieldErrorQuery(f.getField(), f.getDefaultMessage()))
        .toList();
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(new ErrorMessageQuery("Validation failed", errors));
  }

}
