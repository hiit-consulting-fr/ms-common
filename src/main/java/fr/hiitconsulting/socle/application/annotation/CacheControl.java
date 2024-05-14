/*
 * MIT License
 *
 * Copyright (c) 2024 Hi!T Consulting
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

package fr.hiitconsulting.socle.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for managing the Cache-Control header
 *
 * <br><br>
 * The HTTP Cache-Control header contains directives (i.e., instructions), in requests and
 * responses, to control caching in browsers and shared caches (e.g., proxies, CDNs).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControl {

  /**
   * The max-age=N response directive indicates that the response remains fresh until N seconds
   * after the response is generated.
   * <br><br>
   * This indicates that caches can store this response and reuse it for subsequent requests as long
   * as it is fresh.
   * <br><br>
   * Note that max-age does not correspond to the time elapsed since the response was received, it
   * is the time elapsed since the response was generated on the original server. Thus, if other
   * caches located on the network route taken by the response store the response for 100 seconds
   * (indicating it with the Age response header), the browser cache will deduct 100 seconds from
   * the freshness duration.
   */
  long maxAge() default 30;

  /**
   * The no-cache response directive indicates that the response can be cached, but must be
   * validated with the original server before each reuse, even if the cache is disconnected from
   * the original server.
   * <br><br>
   * If you want caches to check their content at each update while reusing stored content, no-cache
   * is the directive to use.
   * <br><br>
   * Note that no-cache does not mean "do not cache". no-cache allows caches to store a response,
   * but imposes a revalidation before any reuse. If you actually want to not store data to have no
   * cache at all, you will need to use the no-store directive.
   */
  boolean noCache() default false;

  /**
   * The no-store response directive indicates that no cache (shared or private) should store the
   * response.
   */
  boolean noStore() default false;

  /**
   * The stale-while-revalidate response directive indicates that the cache can reuse a stale
   * response while it revalidates it in a cache.
   * <br><br>
   * ex:
   * <code>Cache-Control: max-age=604800, stale-while-revalidate=86400</code>
   * <br><br>
   * In the preceding example, the response is fresh for 7 days (604800s). After 7 days, it becomes
   * stale, but the cache can be reused for requests made the following day (86400s), as long as the
   * response revalidation takes place in the background.
   * <br><br>
   * The revalidation will refresh the cache again and the response will therefore appear as always
   * fresh to clients during this period, thus masking the latency induced by a revalidation.
   * <br><br>
   * If no request takes place during this intermediate period, the cache becomes stale and the next
   * request will revalidate the cache normally.
   */
  long staleWhileRevalidate() default 30;

  /**
   * The private response directive indicates that the response can only be stored in a private
   * cache (i.e., the local cache of browsers).
   * <br><br>
   * If private is forgotten for a response with custom content, this response can be stored in a
   * shared cache and end up being reused for several people, thus causing a leak of personal
   * information.
   */
  boolean privateCache() default true;

}