// src/lib/http/http.interceptors.ts
import type { AxiosError, AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import {
  AUTH_LOGIN_REGEX,
  AUTH_ME_REGEX,
  DEBUG_AUTH_REGEX,
  DEBUG_CREATE_ANALYST_REGEX,
  DEBUG_HTTP,
} from './http.config';

function safeJson(v: unknown) {
  try {
    if (typeof v === 'string') return JSON.parse(v);
    return v;
  } catch {
    return v;
  }
}

function maskSensitive(data: unknown) {
  const obj = safeJson(data);
  if (!obj || typeof obj !== 'object') return obj;

  // evita imprimir password
  const clone = { ...(obj as Record<string, unknown>) };
  if (typeof clone.password === 'string') clone.password = '***';
  return clone;
}

function fullUrl(api: AxiosInstance, cfg: InternalAxiosRequestConfig) {
  const base = String(api.defaults.baseURL ?? '');
  const url = String(cfg.url ?? '');
  return `${base}${url}`;
}

export function attachRequestInterceptor(api: AxiosInstance) {
  api.interceptors.request.use((config) => {
    const url = String(config.url ?? '');
    const method = String(config.method ?? 'GET').toUpperCase();

    const wantsDebug =
      DEBUG_HTTP ||
      DEBUG_AUTH_REGEX.test(url) ||
      DEBUG_CREATE_ANALYST_REGEX.test(url);

    if (typeof window !== 'undefined' && wantsDebug) {
      const info = {
        method,
        url,
        fullUrl: fullUrl(api, config as InternalAxiosRequestConfig),
        baseURL: api.defaults.baseURL,
        withCredentials: Boolean(config.withCredentials),
        timeout: config.timeout,
      };

      console.groupCollapsed(
        `%c[HTTP →] ${method} ${url}`,
        'color:#9F2141;font-weight:700'
      );
      console.log(info);

      // payload (enmascarado)
      if (config.data != null) {
        console.log('%cpayload', 'color:#777', maskSensitive(config.data));
      }

      console.groupEnd();
    }

    return config;
  });
}

export function attachResponseInterceptor(api: AxiosInstance) {
  api.interceptors.response.use(
    (res) => {
      const url = String(res.config?.url ?? '');
      const method = String(res.config?.method ?? 'GET').toUpperCase();

      const wantsDebug = DEBUG_HTTP || DEBUG_AUTH_REGEX.test(url);

      if (typeof window !== 'undefined' && wantsDebug) {
        console.groupCollapsed(
          `%c[HTTP ←] ${res.status} ${method} ${url}`,
          'color:#2e7d32;font-weight:700'
        );
        console.log({
          status: res.status,
          url,
          fullUrl: fullUrl(api, res.config as InternalAxiosRequestConfig),
          // Nota: en browser normalmente NO verás set-cookie aquí
          headers: res.headers,
        });
        console.log('%cdata', 'color:#777', res.data);
        console.groupEnd();
      }

      return res;
    },
    (error: AxiosError<unknown>) => {
      const status = error.response?.status;
      const url = String(error.config?.url ?? '');
      const method = String(error.config?.method ?? 'GET').toUpperCase();

      // Caso esperado: bootstrap /auth/me sin sesión (no spamear)
      const isExpectedBootstrap =
        typeof window !== 'undefined' &&
        AUTH_ME_REGEX.test(url) &&
        (status === 401 || status === 419 || status === 440);

      const wantsDebug = DEBUG_HTTP || DEBUG_AUTH_REGEX.test(url);

      if (typeof window !== 'undefined' && wantsDebug && !isExpectedBootstrap) {
        console.groupCollapsed(
          `%c[HTTP ✖] ${status ?? 'ERR'} ${method} ${url}`,
          'color:#d32f2f;font-weight:700'
        );
        console.log({
          status,
          url,
          fullUrl: error.config ? fullUrl(api, error.config as InternalAxiosRequestConfig) : '',
          baseURL: api.defaults.baseURL,
          withCredentials: Boolean(error.config?.withCredentials),
          responseData: error.response?.data,
          responseHeaders: error.response?.headers,
        });
        console.groupEnd();
      }

      // 🔔 Señal global de expiración
      if (typeof window !== 'undefined') {
        // 419/440 = expirado
        if (status === 419 || status === 440) {
          window.dispatchEvent(new CustomEvent('auth:expired'));
        } else if (status === 401 && !AUTH_LOGIN_REGEX.test(url)) {
          // 401 en /auth/me significa: “no hay cookie válida”
          window.dispatchEvent(new CustomEvent('auth:expired'));
        }
      }

      return Promise.reject(error);
    }
  );
}
