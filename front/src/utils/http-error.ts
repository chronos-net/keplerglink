export type HttpErrorLike = {
  name?: string;
  code?: string;
  message?: string;
  response?: {
    status?: number;
    data?: {
      message?: string;
      error?: string;
    };
  };
};

export function isCanceledError(error: unknown): boolean {
  if (typeof error !== 'object' || error === null) return false;

  const e = error as HttpErrorLike;

  return (
    e.name === 'AbortError' ||
    e.name === 'CanceledError' ||
    e.code === 'ERR_CANCELED' ||
    (e.message ?? '').toLowerCase().includes('canceled')
  );
}

export function getHttpErrorMessage(
  error: unknown,
  fallback = 'Ocurrió un error inesperado.'
): string {
  if (typeof error !== 'object' || error === null) return fallback;

  const e = error as HttpErrorLike;

  return (
    e.response?.data?.message ||
    e.response?.data?.error ||
    e.message ||
    fallback
  );
}

export function getHttpStatus(error: unknown): number | null {
  if (typeof error !== 'object' || error === null) return null;

  const e = error as HttpErrorLike;
  return typeof e.response?.status === 'number' ? e.response.status : null;
}