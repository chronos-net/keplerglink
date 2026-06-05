// src/lib/apis.ts
import axios from 'axios';
import type { AxiosInstance, AxiosRequestConfig } from 'axios';

import { API_BASE } from './http/http.config';
import { attachRequestInterceptor, attachResponseInterceptor } from './http/http.interceptors';

function createApi(baseURL: string): AxiosInstance {
  const client = axios.create({
    baseURL,
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    timeout: 20_000,
  });

  attachRequestInterceptor(client);
  attachResponseInterceptor(client);
  return client;
}

export const api = createApi(API_BASE);

export function request<T = unknown>(config: AxiosRequestConfig) {
  return api.request<T>(config);
}

export default api;
