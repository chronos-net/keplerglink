// src/features/auth/types/auth.types.ts

export type CatalogItem<T = string | number> = {
  id: T;
  desc: string;
};

export type Role = {
  id: 1 | 2;
  description: 'ROL_ADMINISTRADOR' | 'ROL_USUARIO'; // o el nombre que decidan para el 2
};

export type User = {
  userId: string;
  name: string;
  email: string;

  workUnit?: CatalogItem<string>;

  rfc?: string;
  curp?: string;
  phone?: string;

  // ✅ mantenemos roles por compatibilidad, pero siempre será [1 rol]
  roles: Role[];
};

export type LoginPayload = {
  username: string;
  password: string;
  captchaToken?: string;
};

/* ──────────────────────────────────────────────
  Normalización (backend flexible)
────────────────────────────────────────────── */
type ApiRecord = Record<string, unknown>;

function isRecord(v: unknown): v is ApiRecord {
  return typeof v === 'object' && v !== null;
}

function pickUserContainer(data: unknown): unknown {
  if (!isRecord(data)) return data;
  return (data.user ?? data.usuario ?? data) as unknown;
}

function toStringSafe(v: unknown): string | undefined {
  return typeof v === 'string' ? v : undefined;
}

function toNumberSafe(v: unknown): number | undefined {
  return typeof v === 'number' && Number.isFinite(v) ? v : undefined;
}

/**
 * ✅ Solo 1 y 2. Nada más.
 * - 1: admin
 * - 2: el otro (ajusta el nombre oficial si no es "ROL_USUARIO")
 */
function roleFromTipo(tipo: number, descTipoUsuario?: string): Role {
  if (tipo === 1) return { id: 1, description: 'ROL_ADMINISTRADOR' };

  // tipo === 2
  // Si el back trae descTipoUsuario real, lo puedes usar como fallback,
  // pero mantenemos un nombre estable para UI.
  const desc = descTipoUsuario?.trim();
  if (desc === 'ROL_ADMINISTRADOR') return { id: 1, description: 'ROL_ADMINISTRADOR' };

  return { id: 2, description: 'ROL_USUARIO' };
}

function normalizeCatalogItemString(raw: unknown): CatalogItem<string> | undefined {
  const v = typeof raw === 'string' ? raw.trim() : '';
  if (!v) return undefined;
  return { id: v, desc: v };
}

/**
 * Normaliza al modelo User (UI).
 * Si faltan campos mínimos, regresa null para evitar crashes.
 */
export function normalizeUser(data: unknown): User | null {
  const container = pickUserContainer(data);
  if (!isRecord(container)) return null;

  const userId =
    toStringSafe(container.userId) ??
    (typeof container.cveUsuario === 'number' ? String(container.cveUsuario) : undefined) ??
    toStringSafe(container.usuarioName) ??
    toStringSafe(container.username);

  const name =
    [toStringSafe(container.nombre), toStringSafe(container.apellidoPaterno), toStringSafe(container.apellidoMaterno)]
      .filter(Boolean)
      .join(' ')
      .trim() || toStringSafe(container.name) || '';

  const email = toStringSafe(container.email) ?? '';

  if (!userId || !name) return null;

  const tipo = toNumberSafe(container.cveTipoUsuario);
  if (tipo !== 1 && tipo !== 2) {
    // si llega algo raro, mejor no romper:
    // lo tratamos como 2 por seguridad.
  }

  const role = roleFromTipo((tipo === 1 || tipo === 2) ? tipo : 2, toStringSafe(container.descTipoUsuario));

  return {
    userId,
    name,
    email,

    workUnit: normalizeCatalogItemString(container.centroDeTrabajo),
    rfc: toStringSafe(container.rfc),
    curp: toStringSafe(container.curp),
    phone: toStringSafe(container.telefono),

    roles: [role], // ✅ siempre 1 rol
  };
}
