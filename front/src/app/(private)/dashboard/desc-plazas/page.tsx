'use client';

import { useMemo, useCallback, useState } from 'react';
import styles from './desc-plazas.module.css';

import { usePayrollCatalogos } from '@/features/comprobantes/hook/useReciboCatalogos';
import { useDescPlazas } from '@/features/desc-plazas/hooks/useDescPlazas';
import DescPlazasView from '@/features/desc-plazas/ui/DescPlazasView';

import EmptyState from '@/features/comprobantes/ui/EmptyState';
import { FilterQuery } from '@/components/filters/types/types';
import UniversalFilter from '@/components/filters/UniversalFilter';

import { clampQuincena } from '@/components/filters/utils/universalFilters.utils';
import DescPlazasSkeleton from '@/features/desc-plazas/ui/DescPlazasSkeleton';
import { generateDescPlazasPdf } from '@/features/desc-plazas/pdf/generateDescPlazasPdf';


export default function DescPlazasPage() {
  const { data, loading, error, consultar } = useDescPlazas();
  const [touched, setTouched] = useState(false);
  const [currentYear, setCurrentYear] = useState<number | null>(null);
  const [currentQuincena, setCurrentQuincena] = useState('');
  const [pdfBusy, setPdfBusy] = useState(false);

  const {
    anios,
    periodos,
    loading: loadingCatalogos
  } = usePayrollCatalogos();

  const yearOptions = useMemo(
    () => anios.map((a) => ({ value: Number(a.descAnio), label: a.descAnio })),
    [anios]
  );

  const quincenaOptions = useMemo(
    () => periodos.map((p) => ({ value: p.descPeriodo, label: p.descPeriodo })),
    [periodos]
  );

  const busy = loading || loadingCatalogos;

  const onFilter = useCallback(async (q: FilterQuery) => {
    setTouched(true);

    const anio = q.anio;
    const quincenaRaw = q.quincena;

    const raw = (q.text ?? '').trim();
    if (!raw || !anio || !quincenaRaw) return;

    const quincena = clampQuincena(quincenaRaw);

    const neyemp = q.neyempResolved || (/^\d{6,}$/.test(raw) ? raw : '');
    if (!neyemp) return;

    setCurrentYear(anio);
    setCurrentQuincena(quincena);

    await consultar({ anio, quincena, neyemp, nombreSp: raw });
  }, [consultar]);

  const handleGeneratePdf = useCallback(async () => {
    if (!data || currentYear == null || !currentQuincena) return;
    setPdfBusy(true);
    try {
      await generateDescPlazasPdf(currentYear, currentQuincena, data);
    } finally {
      setPdfBusy(false);
    }
  }, [data, currentYear, currentQuincena]);

  return (
    <section className={styles.page}>
      <h1 className={styles.h1}>Desgloses de plazas</h1>



      <div className={styles.filterMain}>

        {/*Este es un componente globalizado*/}
        <UniversalFilter
          busy={busy}
          
          yearOptions={yearOptions}
          quincenaOptions={quincenaOptions}
          initialText=""

          initialAnio={null}
          initialQuincena=""
          textPlaceholder="Nombre o Clave del servidor"

          onFilter={onFilter}
          autocompleteSource="KEPLER"
          // autoSubmitOnSelect={false}
          modoEnvioTexto="resueltoONumerico"

          requireText
          requireYear
          requireQuincena
        />
      </div>


      {/* Loading */}
      {busy && (
        <DescPlazasSkeleton/>
      )}

      {/* Resultado OK */}
      {!busy && data && !error && currentYear != null && currentQuincena && (
        <DescPlazasView
          data={data}
          anio={currentYear}
          quincena={currentQuincena}
          onGeneratePdf={handleGeneratePdf}
          pdfBusy={pdfBusy}
        />
      )}

      {/* Error*/}
      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar los desgloses de plazas"
          description={
            error || 'Intenta nuevamente. Si el problema perciste, contacta a soporte'
          }
        />
      )}

      {/* Sin datos */}
      {!busy && !data && touched && !error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No se encontró información para ese servidor público con el año y la quincena seleccionados. Verifica los datos o intenta con otro filtro."
        />
      )}

      {/* Inicial */}
      {!busy && !data && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta desgloses de plazas"
          description="Ingresa nombre o clave, selecciona año y quincena para ver el resultado."
        />
      )}
    </section>
  );
}
