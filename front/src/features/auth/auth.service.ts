// src/features/auth/auth.service.ts
import api from '@/lib/apis';
import type { LoginPayload, User } from './types/auth.types';
import { normalizeUser } from './types/auth.types';

function assertUser(u: User | null, fallbackMsg: string) {
  if (!u) throw new Error(fallbackMsg);
  return u;
}

export async function login(payload: LoginPayload): Promise<User> {
  const { data } = await api.post('/auth/login', payload);
  const u = normalizeUser(data);
  return assertUser(u, 'Login OK, pero la API no devolvió un usuario válido.');
}

export async function me(): Promise<User> {
  const { data } = await api.get('/auth/me');
  const u = normalizeUser(data);
  return assertUser(u, 'No hay sesión activa.');
}

export async function logout(): Promise<void> {
  await api.post('/auth/logout');
}
