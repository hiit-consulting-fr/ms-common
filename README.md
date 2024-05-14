# ms-common

[![build](https://github.com/hiit-consulting-fr/ms-common/actions/workflows/build.yml/badge.svg)](https://github.com/hiit-consulting-fr/ms-common/actions/workflows/build.yml)
![GitHub](https://img.shields.io/github/license/hiit-consulting-fr/ms-common)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/hiit-consulting-fr/ms-common)

Common dependencies and code for microservices at Hi!t-Consulting

## Capabilities

### Request Logging

The `RequestLoggingFilter` logs outgoing responses. It logs the following information:

```
200 | 12.94 ms | GET /api/v1/user/me
(1)     (2)       (3)         
```

1. HTTP status code
2. Response time in milliseconds
3. HTTP method and path

### Cache Control header

The `CacheAnnotationInterceptor` can add the `Cache-Control` header to responses.

To use it, annotate a controller method with `@CacheControl` and specify the `maxAge` in seconds.

```java

@CacheControl(maxAge = 60)
@GetMapping
public String get() {
    return "Hello";
}
```
