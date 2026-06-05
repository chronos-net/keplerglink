// src/app/(private)/dashboard/prestamos/page.tsx
'use client';

import { useCallback, useMemo, useState } from 'react';

import UniversalFilter from '@/components/filters/UniversalFilter';
import type { FilterQuery } from '@/components/filters/types/types';

import EmptyState from '@/features/comprobantes/ui/EmptyState';
import { usePrestamos } from '@/features/prestamos/hook/usePrestamos';
import type { PrestamosRequest } from '@/features/prestamos/types/prestamos.types';

import PrestamosHeaderCard from '@/features/prestamos/ui/PrestamosHeaderCard';
import PrestamosTable from '@/features/prestamos/ui/PrestamosTable';

import s from './prestamos-page.module.css'; 

function isNumericKey(v: string) {
  return /^\d{6,}$/.test(v);
}

function clampPeriodo(v?: string) {
  const n = Math.max(1, Math.min(24, Number((v ?? '').replace(/\D/g, '') || '1')));
  return String(n).padStart(2, '0');
}

export default function PrestamosPage() {
  const { data, loading, error, validar } = usePrestamos();
  const [touched, setTouched] = useState(false);

  const busy = loading;

  const errorMessage = useMemo(() => {
    if (!error) return '';
    if (typeof error === 'string') return error;
    return 'Ocurrió un error al consultar los préstamos. Intenta de nuevo.';
  }, [error]);

  const detalles = data?.detalle ?? [];
  const hasData = !!data && !error;
  const hasItems = hasData && detalles.length > 0;
  const isEmptyItems = hasData && detalles.length === 0;

  const totalPrestamos = detalles.length;

  const onFilter = useCallback(
    async (q: FilterQuery) => {
      setTouched(true);

      const anio = q.anio ?? new Date().getFullYear();
      const periodo = clampPeriodo(q.quincena) || '01';

      const raw = (q.text ?? '').trim();
      if (!raw) return;

      // Preferimos resolved; si no, aceptamos numérico
      const neyemp = q.neyempResolved || (isNumericKey(raw) ? raw : '');
      if (!neyemp) return;

      const payload: PrestamosRequest = { neyemp, periodo, anio };
      await validar(payload);
    },
    [validar]
  );

  return (
    <section className={s.page}>
      <h1 className={s.title}>Préstamos</h1>
      
      <UniversalFilter
        busy={busy}
        initialText=""
        textPlaceholder="Clave del servidor"
        onFilter={onFilter}
        autocompleteSource="GLINK_PRESTAMOS"
        textMode="numericKey9"
        showYear={false}
        showQuincena={false}
        requireText
      />


      {/* Loading */}
      {busy && <p className={s.helperText}>Cargando préstamos…</p>}

      {/* Error */}
      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar los préstamos"
          description={errorMessage}
        />
      )}

      {/* Contenido */}
      {!busy && !error && hasItems && (
        <>
          <PrestamosHeaderCard cabecera={data!.cabecera} totalPrestamos={totalPrestamos} />
          <PrestamosTable rows={detalles} />
        </>
      )}

      {/* ✅ Caso clave: data existe pero viene sin detalle */}
      {!busy && !error && touched && isEmptyItems && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin préstamos"
          description="No se encontraron préstamos para este servidor público."
        />
      )}

      {/* Estado inicial */}
      {!busy && !error && !touched && !data && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta los préstamos del servidor público"
          description="Ingresa la clave del servidor público para ver el detalle de sus préstamos."
        />
      )}

      {/* Ya buscó pero no hubo data */}
      {!busy && !error && touched && !data && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No encontramos información con ese filtro. Verifica la clave o selecciona un servidor en la lista."
        />
      )}
    </section>
  );
}