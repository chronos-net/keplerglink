// src/app/(private)/dashboard/comprobantes/page.tsx
'use client';

import { useState, useCallback, useMemo } from 'react';

import ReciboCard from '@/features/comprobantes/ui/ReciboCard';
import EmptyState from '@/features/comprobantes/ui/EmptyState';
import ReciboSkeleton from '@/features/comprobantes/ui/ReciboSkeleton';

import type { ReciboRequest } from '@/features/comprobantes/types/payroll.types';
import { usePayrollCatalogos } from '@/features/comprobantes/hook/useReciboCatalogos';


import s from './comprobantes.module.css';
import { FilterQuery } from '@/components/filters/types/types';
import { useRecibo } from '@/features/comprobantes/hook/useRecibo';
import UniversalFilter from '@/components/filters/UniversalFilter';
import { generateComprobantePdf } from '@/features/comprobantes/pdf/generateComprobantePdf';



// ================================
// HELPERS
// ================================
function splitNombreOClave(s: string): { neyemp?: string; nombreSp?: string } {
  const t = (s ?? '').trim();
  if (!t) return {};
  return /^\d{6,}$/.test(t) ? { neyemp: t } : { nombreSp: t };
}

function clampQuincena(v?: string) {
  const n = Math.max(
    1,
    Math.min(24, Number((v ?? '').replace(/\D/g, '') || '0')),
  );
  return String(n).padStart(2, '0');
}

export default function ComprobantesPage() {

  const { generar, data, loading, error } = useRecibo();


  const [touched, setTouched] = useState(false);

  const { anios, periodos, loading: loadingCatalogos } =
    usePayrollCatalogos();

  const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
  const [currentPeriodo, setCurrentPeriodo] = useState<string>('');

  const yearOptions = useMemo(
    () => anios.map((a) => ({ value: Number(a.descAnio), label: a.descAnio })),
    [anios]
  );

  const quincenaOptions = useMemo(
    () => periodos.map((p) => ({ value: p.descPeriodo, label: p.descPeriodo })),
    [periodos]
  );

  const busy = loading || loadingCatalogos;

  const onFilter = useCallback(
    async (q: FilterQuery) => {
      setTouched(true);

      const anio = q.anio ?? new Date().getFullYear();
      const quincena = clampQuincena(q.quincena ?? '01');

      setCurrentYear(anio);
      setCurrentPeriodo(quincena);

      const basePayload: ReciboRequest = { anio, quincena };

      const payload: ReciboRequest = q.neyempResolved
        ? { ...basePayload, neyemp: q.neyempResolved }
        : { ...basePayload, ...splitNombreOClave(q.text ?? '') };

      await generar(payload);
    },
    [generar]
  );

  const canShowPdfButton = !!(touched && data && !busy && !error);

  const handleGeneratePdf = useCallback(async () => {
    if (!data) return;
    await generateComprobantePdf(currentYear, currentPeriodo || '01', data);
  }, [data, currentYear, currentPeriodo]);
  return (
    <section className={s.page}>
      <h1 className={s.h1}>Recibos</h1>

      <UniversalFilter
        busy={busy}
        initialText=""
        initialAnio={null}
        initialQuincena=""
        yearOptions={yearOptions}
        quincenaOptions={quincenaOptions}
        textPlaceholder="Nombre o Clave del servidor"
        requireText
        requireYear
        requireQuincena

        onFilter={onFilter}
        autocompleteSource="KEPLER"
        modoEnvioTexto="resueltoONumerico"
        autoSubmitOnSelect={true}

        // De momento apagamos PDF en el esqueleto
        showPdfButton={canShowPdfButton}
        onGeneratePdf={handleGeneratePdf}
        pdfBusy={false}
        pdfLabel="Generar PDF"
      />

      {busy && <ReciboSkeleton />}

      {!busy && data && !error && (
        <ReciboCard data={data} periodo={currentPeriodo} anio={currentYear} />
      )}

      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos generar tu recibo"
          description={
            error || 'Intenta nuevamente. Si el problema persiste, contacta a soporte.'
          }
        />
      )}

      {!busy && !data && touched && !error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No encontramos información con los filtros ingresados. Verifica los datos o intenta con otros criterios de búsqueda."
        />
      )}

      {!busy && !data && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta tu recibo"
          description="Ingresa el nombre o la clave del servidor público, el año y la quincena para consultar el recibo completo."
        />
      )}
    </section>
  );
}
