// src/features/anualizado/ui/AnualizadoGridPro/useAnualizadoGridModel.ts
import { useMemo } from 'react';
import { AnualizadoResponse } from '../types/anualizado.dto';

type Band = { idx: number; size: number; label: string };

function addToMap(m: Map<string, number>, key: string, value: number) {
  m.set(key, (m.get(key) ?? 0) + value);
}

function sortCodes(codes: string[]) {
  // Si son numéricas tipo "0102" las ordena por número; si no, alfabético.
  const allNumeric = codes.every((c) => /^\d+$/.test(c));
  return allNumeric ? codes.sort((a, b) => Number(a) - Number(b)) : codes.sort();
}

/**
 * useAnualizadoGridModel
 * ---------------------
 * Prepara un “view-model” optimizado para pintar la tabla del anualizado.
 *
 * ¿Por qué existe?
 * - Evita recalcular cosas pesadas en cada render del grid (indexación O(n)).
 * - Convierte arrays en Maps para accesos O(1) por periodo/código.
 * - Agrupa importes por código (importante porque el backend puede repetir claves).
 * - Genera bandas visuales (Q1, Q2, ...) para el header con colSpan.
 *
 * ¿Qué entrega?
 * - periods: lista de periodos en orden
 * - plazaByPeriod: Map(periodo -> plaza)
 * - percByPeriodo / dedByPeriodo: Map(periodo -> Map(codigo -> importe_total))
 * - percKeys / dedKeys: catálogo real de claves presentes (sin inventar)
 * - resumenByPeriodo: Map(periodo -> resumen)
 * - bands: agrupación de 6 periodos para el encabezado
 */
export function useAnualizadoGridModel(data: AnualizadoResponse) {
  return useMemo(() => {
    const { periodos, plazas, catalogos } = data;

    const periods = periodos.map((p) => p.periodo);

    // periodo -> plaza
    const plazaByPeriod = new Map<string, (typeof plazas)[number]>();
    for (const z of plazas) plazaByPeriod.set(z.periodo, z);

    // periodo -> (codigo -> importe acumulado)
    const percByPeriodo = new Map<string, Map<string, number>>();
    const dedByPeriodo = new Map<string, Map<string, number>>();

    const percSet = new Set<string>();
    const dedSet = new Set<string>();

    // periodo -> resumen
    const resumenByPeriodo = new Map<string, (typeof periodos)[number]['resumen']>();

    for (const p of periodos) {
      resumenByPeriodo.set(p.periodo, p.resumen);

      const percInner = new Map<string, number>();
      for (const x of p.percepciones) {
        addToMap(percInner, x.codigo, x.importe);
        percSet.add(x.codigo);
      }
      percByPeriodo.set(p.periodo, percInner);

      const dedInner = new Map<string, number>();
      for (const x of p.deducciones) {
        addToMap(dedInner, x.codigo, x.importe);
        dedSet.add(x.codigo);
      }
      dedByPeriodo.set(p.periodo, dedInner);
    }

    const percKeys = sortCodes([...percSet]);
    const dedKeys = sortCodes([...dedSet]);

    // bandas visuales (6 columnas por banda)
    const bands: Band[] = [];
    const BAND = 6;
    for (let i = 0; i < Math.ceil(periods.length / BAND); i++) {
      const from = i * BAND;
      const size = Math.min(BAND, periods.length - from);
      const a = periods[from];
      const b = periods[from + size - 1];
      bands.push({ idx: i, size, label: `Q${i + 1} ${a}–${b}` });
    }

    return {
      periods,
      plazas,
      catalogos,
      bands,
      plazaByPeriod,
      percKeys,
      dedKeys,
      percByPeriodo,
      dedByPeriodo,
      resumenByPeriodo,
    };
  }, [data]);
}