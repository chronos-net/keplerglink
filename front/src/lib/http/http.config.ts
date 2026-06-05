// src/lib/http/http.config.ts

export const API_BASE = process.env.NEXT_PUBLIC_API_BASE ?? 'http://localhost:8081';
// export const API_BASE = process.env.NEXT_PUBLIC_API_BASE ?? 'http://10.0.32.83:8081';

/** Auth endpoints */
export const AUTH_LOGIN_REGEX = /\/auth\/login$/i;
export const AUTH_ME_REGEX = /\/auth\/me$/i;
export const AUTH_LOGOUT_REGEX = /\/auth\/logout$/i;

/** Debug flags */
export const DEBUG_HTTP = process.env.NEXT_PUBLIC_DEBUG_HTTP === '1';

/** Debug: endpoints que queremos imprimir */
export const DEBUG_AUTH_REGEX = /\/auth\/(login|me|logout)$/i;

/** Debug específico que ya tenías */
export const DEBUG_CREATE_ANALYST_REGEX = /\/api\/users\/analysts\/create$/i;
