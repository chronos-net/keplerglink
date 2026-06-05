'use client';

import { useCallback, useState } from 'react';

import UniversalFilter from '@/components/filters/UniversalFilter';
import type { FilterQuery } from '@/components/filters/types/types';

import EmptyState from '@/features/comprobantes/ui/EmptyState';

import s from './comentarios.module.css';
import { usePostComentarios } from '@/features/comentarios/hook/usePostComentarios';
import ComentariosSkeleton from '@/features/comentarios/ui/ComentariosSkeleton';
import ComentariosCard from '@/features/comentarios/ui/ComentariosCard/ComentariosCard';
import ComentariosHeader from '@/features/comentarios/ui/ComentariosHeader';

function isNumericKey(value: string) {
  return /^\d{6,}$/.test(value);
}

export default function ComentariosPage() {
  const { data, loading, error, run } = usePostComentarios();
  const [touched, setTouched] = useState(false);

  const busy = loading;

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

  const cabesera = data?.cabesera ?? null;
  const valores = data?.valores ?? [];

  const hasData = !!data;
  const hasItems = valores.length > 0;
  const isEmptyItems = hasData && !error && !hasItems;

  return (
    <section className={s.page}>
      <h1 className={s.h1}>Comentarios</h1>

      <UniversalFilter
        busy={busy}
        initialText=""
        textPlaceholder="Nombre o Clave del servidor"
        requireText
        showYear={false}
        showQuincena={false}
        onFilter={onFilter}
        autocompleteSource="KEPLER"
      />

      {busy && <ComentariosSkeleton />}

      {!busy && cabesera && !error && hasItems && (
        <>
          <ComentariosHeader datos={cabesera} />

          <ComentariosCard
            cabecera={cabesera}
            items={valores}
          />
        </>
      )}

      {!busy && error && (
        <EmptyState
          imgSrc="/img/error.png"
          title="No pudimos consultar los comentarios"
          description={
            error ||
            'Intenta nuevamente o verifica los datos capturados. Si el problema persiste, contacta a soporte.'
          }
        />
      )}

      {!busy && !error && touched && isEmptyItems && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin registros"
          description="No se encontraron comentarios para este servidor público."
        />
      )}

      {!busy && !data && !touched && !error && (
        <EmptyState
          imgSrc="/img/buscar_recibo.png"
          title="Consulta comentarios"
          description="Ingresa el nombre o la clave del servidor público para consultar los comentarios."
        />
      )}

      {!busy && !error && touched && !data && (
        <EmptyState
          imgSrc="/img/error.png"
          title="Sin resultados"
          description="No encontramos comentarios para ese filtro. Verifica la clave o selecciona un servidor en la lista."
        />
      )}
    </section>
  );
}