/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Hi!T Consulting
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

package fr.hiitconsulting.socle.application.logger;

import com.google.common.base.Stopwatch;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

  @Override
  protected boolean shouldLog(HttpServletRequest request) {
    return logger.isInfoEnabled() && !request.getRequestURI().contains("actuator");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    boolean isFirstRequest = !isAsyncDispatch(request);
    HttpServletRequest requestToUse = request;

    if (isIncludePayload() && isFirstRequest
        && !(request instanceof ContentCachingRequestWrapper)) {
      requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
    }

    Stopwatch stopwatch = Stopwatch.createStarted();
    try {
      filterChain.doFilter(requestToUse, response);
    } finally {
      if (shouldLog(requestToUse) && !isAsyncStarted(requestToUse)) {
        logger.info(
            createMessage(requestToUse, getMessagePrefix(response.getStatus(), stopwatch), ""));
      }
    }
  }

  private String getMessagePrefix(int status, Stopwatch stopwatch) {
    AnsiElement e;
    if (status >= 400 && status <= 499) {
      e = AnsiColor.YELLOW;
    } else if (status >= 500 && status <= 599) {
      e = AnsiColor.RED;
    } else {
      e = AnsiColor.GREEN;
    }
    return AnsiOutput.toString(e, status) + " | " + AnsiOutput.toString(AnsiStyle.BOLD,
        stopwatch.toString()) +
        " | ";
  }

  @Override
  protected void beforeRequest(HttpServletRequest request, String message) {
  }

  @Override
  protected void afterRequest(HttpServletRequest request, String message) {
  }

}