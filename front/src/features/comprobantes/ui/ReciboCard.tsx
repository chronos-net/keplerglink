// src/features/comprobantes/ui/ReciboCard.tsx
'use client';

import { ComprobanteResponse, ConceptRow } from '../types/comprobantes.types';
import ConceptTable from './ConceptTable';
import ReciboHeader from './ReciboHeader';
import s from './recibo-card.module.css';

export default function ReciboCard({
  data,
  periodo,
  anio,
}: {
  data: ComprobanteResponse;
  periodo: string;
  anio: number | string;
}) {

  const percepRows: ConceptRow[] = data.percepciones.map((x) => ({
    clave: x.codigo,
    descripcion: x.descripcion,
    importe: x.importe,
  }));

  const deducRows: ConceptRow[] = data.deducciones.map((x) => ({
    clave: x.codigo,
    descripcion: x.descripcion,
    importe: x.importe,
  }));

  const plazasView = (data.multiplazas?.length ? data.multiplazas : null)
    ? data.multiplazas.map((p) => ({
      plaza: p.plaza ?? '—',
      categoria: p.puesto ?? '—',
      puesto: p.leyendaPuesto ?? '—',
      adscripcion: p.adsc ?? '—',
    }))
    : [
      {
        plaza: data.plaza.plazaId ?? '—',
        categoria: data.plaza.puesto ?? '—',
        puesto: data.plaza.leyendaPuesto ?? '—',
        adscripcion: data.plaza.adsc ?? '—',
      },
    ];

  return (
    <div className={s['rb-wrap']}>
      <ReciboHeader
        data={data}
        queryPeriodo={periodo}
        queryAnio={anio}

      />

      <div className={s['rb-columns']}>
        <div className={s['rb-col']}>
          <ConceptTable title="Percepciones" rows={percepRows} />
        </div>
        <div className={s['rb-col']}>
          <ConceptTable title="Deducciones" rows={deducRows} />
        </div>
      </div>

      <div className={s['rb-neto']}>
        {plazasView.map((plaza, index) => (
          <div className={s.row} key={index}>
            <div className={s.item}>
              <div className={s.lbl}>Plaza</div>
              <strong className={s.mono}>{plaza.plaza ?? '—'}</strong>
            </div>

            <div className={s.item}>
              <div className={s.lbl}>Categoría</div>
              <strong className={s.mono}>{plaza.categoria ?? '—'}</strong>
            </div>

            <div className={s.item}>
              <div className={s.lbl}>Puesto</div>
              <strong className={s.mono}>{plaza.puesto ?? '—'}</strong>
            </div>

            <div className={s.item}>
              <div className={s.lbl}>Adscripción</div>
              <strong className={s.mono}>{plaza.adscripcion ?? '—'}</strong>
            </div>

            <div className={`${s.item} ${s.right}`}></div>
          </div>
        ))}

        <div className={`${s.row} ${s.netoRow}`}>
          <div className={s.item}></div>
          <div className={s.item}></div>
          <div className={s.item}></div>

          <div className={s.item}>
            <div className={s.lbl}>Importe Neto</div>
          </div>

          <div className={`${s.item} ${s.right}`}>
            <strong className={`${s.mono} ${s.netoValue}`}>
              {(data.resumen?.neto ?? 0).toLocaleString('es-MX', {
                style: 'currency',
                currency: 'MXN',
              })}
            </strong>
          </div>
        </div>
      </div>
    </div>
  );
}