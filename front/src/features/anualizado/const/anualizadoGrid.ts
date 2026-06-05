import { AnualizadoResponse } from "../types/anualizado.dto";

export function money(n?: number) {
  return (n ?? 0).toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export const headerRows: Array<{
  key: string;
  label: string;
  get: (z: AnualizadoResponse['plazas'][number] | undefined) => string | null | undefined;
}> = [
  { key: 'plaza', label: 'Plaza', get: (z) => z?.plaza },
  { key: 'ads', label: 'Adscripción', get: (z) => z?.ads },
  { key: 'cheque', label: 'Cheque', get: (z) => z?.cheque },
  { key: 'categoria', label: 'Categoría', get: (z) => z?.categoria },
  { key: 'centro', label: 'Centro de Trabajo', get: (z) => z?.centroTrabajo },
  { key: 'lugar', label: 'Lugar de Pago', get: (z) => z?.lugarPago },
];