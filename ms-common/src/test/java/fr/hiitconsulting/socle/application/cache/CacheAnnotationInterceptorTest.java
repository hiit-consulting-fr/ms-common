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

package fr.hiitconsulting.socle.application.cache;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

import fr.hiitconsulting.socle.infrastructure.common.adapter.annotation.CacheControl;
import fr.hiitconsulting.socle.infrastructure.common.adapter.in.CacheAnnotationInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.method.HandlerMethod;

@ExtendWith(MockitoExtension.class)
public class CacheAnnotationInterceptorTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private HandlerMethod handlerMethod;

  @InjectMocks
  private CacheAnnotationInterceptor interceptor;

  @Test
  public void shouldSetNoCacheAndNoStoreWhenAnnotationSpecifies() {
    when(handlerMethod.getMethodAnnotation(CacheControl.class)).thenReturn(new CacheControl() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return null;
      }

      @Override
      public long maxAge() {
        return 0;
      }

      @Override
      public boolean noCache() {
        return true;
      }

      @Override
      public boolean noStore() {
        return true;
      }

      @Override
      public long staleWhileRevalidate() {
        return 0;
      }

      @Override
      public boolean privateCache() {
        return false;
      }

    });

    interceptor.preHandle(request, response, handlerMethod);

    verify(response).setHeader(CACHE_CONTROL, "no-cache, no-store");
  }

  @Test
  public void shouldSetPrivateAndMaxAgeWhenAnnotationSpecifies() {
    when(handlerMethod.getMethodAnnotation(CacheControl.class)).thenReturn(new CacheControl() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return null;
      }

      @Override
      public boolean privateCache() {
        return true;
      }

      @Override
      public long maxAge() {
        return 3600;
      }

      @Override
      public boolean noCache() {
        return false;
      }

      @Override
      public boolean noStore() {
        return false;
      }

      @Override
      public long staleWhileRevalidate() {
        return 0;
      }

    });

    interceptor.preHandle(request, response, handlerMethod);

    verify(response).setHeader(CACHE_CONTROL, "private, max-age=3600");
  }

  @Test
  public void shouldNotSetHeaderWhenAnnotationIsNotPresent() {
    when(handlerMethod.getMethodAnnotation(CacheControl.class)).thenReturn(null);

    interceptor.preHandle(request, response, handlerMethod);

    verify(response, never()).setHeader(eq(CACHE_CONTROL), anyString());
  }
}