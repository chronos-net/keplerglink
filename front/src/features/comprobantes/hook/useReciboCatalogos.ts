'use client';

import { useEffect, useState } from 'react';
import { CatalogosService } from '../services/catalogos.service';

export type AnioItem = {
  cveAnio: number;
  descAnio: string;
};

export type PeriodoItem = {
  cvePeriodo: number;
  descPeriodo: string;
};

type State = {
  anios: AnioItem[];
  periodos: PeriodoItem[];
  loading: boolean;
  error: Error | null;
};

export function usePayrollCatalogos(): State {

  const [anios, setAnios] = useState<AnioItem[]>([]);
  const [periodos, setPeriodos] = useState<PeriodoItem[]>([]);
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const controller = new AbortController();

    (async () => {
      try {
        setLoading(true);
        setError(null);

        const [a, p] = await Promise.all([
          CatalogosService.getAnios({ signal: controller.signal }),
          CatalogosService.getPeriodos({ signal: controller.signal }),
        ]);

        setAnios(a);
        setPeriodos(p);
      } catch (err: unknown) {
        if (err instanceof DOMException && err.name === 'AbortError') return;
        setError(err instanceof Error ? err : new Error('Error al cargar catálogos'));
      } finally {
        setLoading(false);
      }
    })();

    return () => controller.abort();
  }, []);

  return { anios, periodos, loading, error };
}