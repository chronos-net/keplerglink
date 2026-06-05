'use client';

import { useCallback, useState } from 'react';
import s from './historico-glink.module.css';

import UniversalFilter from '@/components/filters/UniversalFilter';
import type { FilterQuery } from '@/components/filters/types/types';

import EmptyState from '@/features/comprobantes/ui/EmptyState';
import { useHistoricoGlink } from '@/features/historicos/hooks/useHistoricoGlink';

import HistoricoSkeleton from '@/features/historicos/ui/HistoricoSkeleton';
import HistoricoHeaderCard from '@/features/historicos/ui/HistoricoHeaderCard';
import HistoricoTable from '@/features/historicos/ui/HistoricoTable';

function isNumericKey(v: string) {
  return /^\d{6,}$/.test(v);
}

export default function HistoricoGlinkPage() {
  const { data, loading, error, validar } = useHistoricoGlink();
  const [touched, setTouched] = useState(false);

  const header = data?.header ?? null;
  const totalPercep = data?.percepciones?.total ?? 0;
  const detalles = data?.detalles ?? [];
  const hasHeader = !!header;
  const hasDetalles = detalles.length > 0;

  const busy = loading;

  const onFilter = useCallback(
    async (q: FilterQuery) => {
      setTouched(true);

      const raw = (q.text ?? '').trim();
      if (!raw) return;

      const neyemp = q.neyempResolved || (isNumericKey(raw) ? raw : '');
      if (!neyemp) return;

      await validar({ neyemp });
    },
    [validar]
  );

  return (
    <section className={s.page}>
      <h1 className={s.title}>Histórico Laboral</h1>

      <div className={s.filterRow}>
        <UniversalFilter
          busy={busy}
          initialText=""
          textPlaceholder="Clave del servidor"
          requireText
          showYear={false}
          showQuincena={false}
          onFilter={onFilter}
          autocompleteSource="GLINK_HISTORICO"
          textMode="numericKey9"
        />
      </div>

      {busy && <HistoricoSkeleton />}



      {!busy && hasHeader && !error && hasDetalles && (
        <>
          <HistoricoHeaderCard header={header} totalPercepciones={totalPercep} />
          <HistoricoTable rows={detalles} />
        </>
      )}

      {!busy && hasHeader && !error && !hasDetalles && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin registros"
          description="No encontramos movimientos en el histórico laboral para esta clave."
        />
      )}

      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar el histórico laboral"
          description={
            error ||
            'Intenta nuevamente o verifica los datos capturados. Si el problema persiste, contacta a soporte.'
          }
        />
      )}

      {!busy && !hasHeader && touched && !error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No encontramos histórico para ese filtro. Verifica la clave o selecciona un servidor en la lista."
        />
      )}

      {!busy && !hasHeader && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta el histórico laboral"
          description="Ingresa la clave del servidor para visualizar el histórico completo."
        />
      )}
    </section>
  );
}