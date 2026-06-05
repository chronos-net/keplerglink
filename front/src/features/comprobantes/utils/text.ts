// src/features/comprobantes/utils/text.ts

export const toUpperFilter = (value: string): string => {
  const v = (value ?? '');
  const cleaned = v
    .normalize('NFC')
    .replace(/[^\p{L}\p{M}\d\s.'-]/gu, '');
  return cleaned.toUpperCase();
};