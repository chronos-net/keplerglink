'use client';

import { useCallback, useMemo, useState } from 'react';

import UniversalFilter from '@/components/filters/UniversalFilter';
import type { FilterQuery } from '@/components/filters/types/types';

import EmptyState from '@/features/comprobantes/ui/EmptyState';
import { useConsultarPensiones } from '@/features/pensiones/hook/useConsultarPensiones';

import PensionesSkeleton from '@/features/pensiones/ui/PensionesSkeleton';
import PensionesHeader from '@/features/pensiones/ui/PensionesHeader';
import PensionesStats from '@/features/pensiones/ui/PensionesStats';
import PensionesTable from '@/features/pensiones/ui/PensionesTable';

import styles from './pensiones.module.css';

function isNumericKey(s: string) {
  return /^\d{6,}$/.test(s);
}

export default function PensionesPage() {
  const { data, loading, error, run } = useConsultarPensiones();

  const [touched, setTouched] = useState(false);
  const busy = loading;

  const errorMessage = useMemo(() => {
    if (!error) return '';
    if (typeof error === 'string') return error;
    return 'Ocurrió un error al consultar tus pensiones. Intenta de nuevo.';
  }, [error]);

  const onFilter = useCallback(
    async (q: FilterQuery) => {
      setTouched(true);

      const raw = (q.text ?? '').trim();
      if (!raw) return;

      const neyemp = q.neyempResolved || (isNumericKey(raw) ? raw : '');
      if (!neyemp) return;

      await run({ neyemp });
    },
    [run]
  );

  return (
    <section className={styles.page}>
      <h1 className={styles.title}>Pensiones Alimenticias</h1>

      <div className={styles.filterRow}>
        <UniversalFilter
          busy={busy}
          initialText=""
          textPlaceholder="Clave del servidor"
          requireText
          showYear={false}
          showQuincena={false}
          onFilter={onFilter}
          autocompleteSource="GLINK_PENSIONES"
          textMode="numericKey9"
        />
      </div>


      {/* Loading */}
      {busy && <PensionesSkeleton />}

      {/* Dejarlo en uno solo */}
      {!busy && data && !error && (


        <div className={styles.content}>
          {/* Header */}
          <PensionesHeader servidor={data.servidor} />
          <PensionesStats stats={data.stats} />
          <PensionesTable items={data.pensiones} />
        </div>
      )}

      {/* Error */}
      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar tus pensiones"
          description={errorMessage}
        />
      )}

      {/* Sin datos */}
      {!busy && !data && touched && !error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No encontramos pensiones para ese filtro. Verifica la clave o selecciona un servidor en la lista."
        />
      )}

      {/* Inicial */}
      {!busy && !data && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta pensiones"
          description="Ingresa la clave del servidor público para consultar pensiones."
        />
      )}
    </section>
  );
}