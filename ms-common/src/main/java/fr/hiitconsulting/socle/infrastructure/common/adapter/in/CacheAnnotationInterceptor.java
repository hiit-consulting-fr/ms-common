/*
 * MIT License
 *
 * Copyright (c) 2024-2025 Hi!T Consulting
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

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

import fr.hiitconsulting.socle.infrastructure.common.adapter.annotation.CacheControl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@NullMarked
public class CacheAnnotationInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    if (handler instanceof HandlerMethod hm) {

      handleCacheControlAnnotation(response, hm.getMethodAnnotation(CacheControl.class));

    }

    return true;
  }

  private void handleCacheControlAnnotation(HttpServletResponse response,
      @Nullable CacheControl annotation) {
    if (annotation == null) {
      return;
    }

    List<String> headerValues = new ArrayList<>();
    if (annotation.noCache()) {
      headerValues.add("no-cache");

      if (annotation.noStore()) {
        headerValues.add("no-store");
      }
    } else {
      if (annotation.privateCache()) {
        headerValues.add("private");
      }

      if (annotation.maxAge() > 0) {
        headerValues.add("max-age=" + annotation.maxAge());
      }

      if (annotation.staleWhileRevalidate() > 0) {
        headerValues.add("stale-while-revalidate=" + annotation.staleWhileRevalidate());
      }
    }

    response.setHeader(CACHE_CONTROL, String.join(", ", headerValues));
  }

}




