// src/features/historicos/utils/format.ts
export function formatCurrencyMXN(value?: number): string {
  const n = typeof value === 'number' ? value : 0;
  return n.toLocaleString('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  });
}


export function capitalizeFirstLetter(value?: string | null): string {
  const text = (value ?? '').trim();

  if (!text) return '';

  return text.charAt(0).toLocaleUpperCase('es-MX') + text.slice(1);
}
