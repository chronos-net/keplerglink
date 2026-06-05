'use client';

import { useCallback, useMemo, useState } from "react";

import { useAnualizado } from "@/features/anualizado/hooks/useAnualizado";
import { usePayrollCatalogos } from "@/features/comprobantes/hook/useReciboCatalogos";

import type { FilterQuery } from "@/components/filters/types/types";

import EmptyState from "@/features/comprobantes/ui/EmptyState";
import AnualizadoSkeleton from "@/features/anualizado/ui/AnualizadoSkeleton";
import AnualizadoCard from "@/features/anualizado/ui/AnualizadoCard";
import AnualizadoHeader from "@/features/anualizado/ui/AnualizadoHeader";

import styles from "./anualizado.module.css";

import { PayrollService, type ServidorLite } from "@/features/comprobantes/services/payroll.service";
import UniversalFilter from "@/components/filters/UniversalFilter";
import { generateAnualizadoPdf } from "@/features/anualizado/pdf/generateAnualizadoPdf";

const DEFAULT_YEAR = new Date().getFullYear();

function isNumericKey(s: string) {
  return /^\d{6,}$/.test(s);
}

export default function AnualizadoPage() {
  const { data, loading, error, run } = useAnualizado();

  const [touched, setTouched] = useState(false);
  const [currentYear, setCurrentYear] = useState<number | null>(null);
  const [initialText, setInitialText] = useState<string>("");

  const { anios, loading: loadingCatalogos } = usePayrollCatalogos();

  const yearOptions = useMemo(
    () => (anios ?? []).map((a) => ({ value: Number(a.descAnio), label: a.descAnio })),
    [anios]
  );

  const busy = loading || loadingCatalogos;

  const onFilter = useCallback(async (q: FilterQuery) => {
    setTouched(true);

    const anio = q.anio ?? currentYear ?? DEFAULT_YEAR;
    setCurrentYear(anio);
    setInitialText(q.text ?? "");

    const raw = (q.text ?? "").trim();
    if (!raw) return;

    if (q.neyempResolved) {
      await run({ anio, neyemp: q.neyempResolved });
      return;
    }

    if (isNumericKey(raw)) {
      await run({ anio, neyemp: raw });
      return;
    }

    try {
      const matches: ServidorLite[] = await PayrollService.buscarServidoresPorNombre(raw);

      if (matches?.length) {
        const upper = raw.toUpperCase();
        const exact =
          matches.find((m) => m.negnom.trim().toUpperCase() === upper) ?? matches[0];

        await run({ anio, neyemp: exact.neyemp });
        return;
      }
    } catch {
      // noop
    }

    await run({ anio, nombreSp: raw });
  }, [currentYear, run]);

  const canShowPdfButton = !!(touched && data && !busy && !error && currentYear != null);

  const handleGeneratePdf = useCallback(async () => {
    if (!data) return;
    if (currentYear == null) return; // ✅ ya no hay fallback innecesario
    await generateAnualizadoPdf(currentYear, data);
  }, [data, currentYear]);

  return (
    <section className={styles.page}>
      <h1 className={styles.h1}>Anualizado</h1>

      <div className={`${styles.filterRow} ${canShowPdfButton ? styles.filterRowHasPrint : ""}`}>
        <div className={styles.filterMain}>
          <UniversalFilter
            busy={busy}
            initialText={initialText}
            initialAnio={currentYear}
            showYear
            hideQuincena
            yearOptions={yearOptions}
            textPlaceholder="Nombre o Clave del servidor"
            requireText
            requireYear
            onFilter={onFilter}
            autocompleteSource="KEPLER"
            modoEnvioTexto="resueltoONumerico"
            
            showPdfButton={canShowPdfButton}
            onGeneratePdf={handleGeneratePdf}
            pdfBusy={false}
            pdfLabel="Generar PDF"
          />
        </div>
      </div>


      {busy && <AnualizadoSkeleton />}

      {/* Resultado OK */}

      {!busy && data && !error && (
        <>
          {data.empleado && <AnualizadoHeader datos={data.empleado} />}
          <AnualizadoCard data={data} />
        </>
      )}

      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar tu anualizado"
          description={
            error ||
            "Intenta nuevamente o verifica los datos capturados. Si el problema persiste, contacta a soporte."
          }
        />
      )}

      {!busy && !data && touched && !error && (
        <EmptyState
          imgSrc="/img/sin_anualizado.png"
          title="Sin información anualizada"
          description="No encontramos datos anualizados para ese nombre o clave en el año seleccionado. Verifica los datos o intenta con otro año."
        />
      )}

      {!busy && !data && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta tu anualizado"
          description="Ingresa nombre o clave y el año para ver el resultado."
        />
      )}
    </section>
  );
}