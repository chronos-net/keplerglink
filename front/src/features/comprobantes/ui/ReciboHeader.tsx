'use client';

import { useEffect } from 'react';
import { MessageCircle, Landmark } from 'lucide-react';

import s from './recibo-header.module.css';

import { useComments } from '@/features/comentarios/hook/useComentarios';
import { usePensiones } from '@/features/pensiones/hook/usePensiones';

import CommentsModal from './CommentsDrawer/CommentsDrawerView';
import PensionesDrawer from './PensionesDrawer/PensionesDrawer';
import { ComprobanteResponse } from '../types/comprobantes.types';

export default function ReciboHeader({
  data,
  queryPeriodo,
  queryAnio,
}: {
  data: ComprobanteResponse;
  queryPeriodo: string;
  queryAnio: number | string;
}) {
  const p = data.empleado;
  const c = data.resumen;
  const r = data.recibo;
  const z = data.plaza;

  const centroTrabajo = z.centroTrabajo || z.plazaId || '—';
  const plazaCode = z.plazaId || z.adsc || '—';
  const lugarPago = z.lugarPago ?? '—';
  const categoria = z.puesto || '—';
  const adscripcion = z.adsc || '—';

  const money = (n?: number) =>
    (n ?? 0).toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });

  const {
    open: openComments,
    setOpen: setOpenComments,
    loading: loadingComments,
    error: errorComments,
    data: commentsData,
    hasComments,
    prime,
    run,
  } = useComments();

  const {
    open: openPensiones,
    setOpen: setOpenPensiones,
    loading: loadingPensiones,
    error: errorPensiones,
    data: pensionesData,
    hasPensiones,
    prime: primePensiones,
    run: runPensiones,
  } = usePensiones();

  const commentsHeader = commentsData?.cabecera ?? {
    neyemp: p.neyemp ?? '',
    nombreCompleto: p.nombre ?? '',
  };

  useEffect(() => {
    if (!p.neyemp) return;

    const anio = Number(queryAnio);
    const periodo = String(queryPeriodo);

    // comentarios
    prime({
      anio,
      periodo,
      neyemp: p.neyemp,
    });

    // pensiones
    primePensiones({
      anio,
      periodo,
      neyemp: p.neyemp,
    });
  }, [p.neyemp, queryPeriodo, queryAnio, prime, primePensiones]);




  return (
    <div className={s.canvas}>
      <div className={s.card}>
        <div className={s.headerGrid}>
          {/* PERSONA */}
          <div className={s.personCol}>
            <div className={s.personRow}>
              <div className="avatar">{(p.nombre?.[0] ?? '?').toUpperCase()}</div>

              <div className={s.personMeta}>
                <div className={s.personName}>{p.nombre ?? '—'}</div>

                <div className={s.personIds}>
                  <span><b>Curp:</b> {p.curp ?? '—'}</span>
                  <span className={s.dot} />
                  <span><b>RFC:</b> {p.rfc ?? '—'}</span>
                </div>
              </div>
            </div>
          </div>

          {/* DATOS LABORALES */}
          <div className={s.laborCol}>
            <div className={s.sectionTitle}>Datos laborales</div>
            <div className={s.laborGrid}>
              <div className={s.block}>
                <div className={s.label}>Centro de trabajo:</div>
                <div className={s.mono}>{centroTrabajo}</div>

                <div className={s.label}>Lugar de pago:</div>
                <div className={s.mono}>{lugarPago}</div>
              </div>

              <div className={s.block}>
                <div className={s.label}>ISSEMYM:</div>
                <div className={s.mono}>{p.iss ?? '—'}</div>

                <div className={s.label}>Plaza:</div>
                <div className={s.mono}>{plazaCode}</div>
              </div>
            </div>
          </div>

          {/* CATEGORÍA + BOTONES */}
          <div className={s.catCol}>
            <div className={s.catRow}>
              <div className={s.catBox}>
                <div className={s.label}>Categoría:</div>
                <div className={s.pill}>{categoria}</div>
              </div>

              <div className={s.catBox}>
                <div className={s.label}>Adscripción</div>
                <div className={s.pill}>{adscripcion}</div>
              </div>

              <div className={s.actionButtons}>
                <button
                  className={`${s.msgBtn} ${hasComments ? s.hasComments : s.noComments}`}
                  disabled={!p.neyemp || hasComments !== true}
                  onClick={async () => {
                    if (!p.neyemp) return;

                    await run({
                      anio: Number(queryAnio),
                      periodo: String(queryPeriodo),
                      neyemp: p.neyemp,
                    });

                    setOpenComments(true);
                  }}
                  title="Comentarios"
                >
                  <MessageCircle size={16} />
                  <span className={s.badge} />
                </button>

                <button
                  type="button"
                  className={`${s.msgBtn} ${hasPensiones ? s.hasComments : s.noComments}`}
                  disabled={!p.neyemp || hasPensiones !== true}
                  onClick={async () => {
                    if (!p.neyemp) return;
                    await runPensiones({ anio: Number(queryAnio), periodo: String(queryPeriodo), neyemp: p.neyemp });
                    setOpenPensiones(true);
                  }}
                  title="Pensiones registradas"
                >
                  <Landmark size={16} />
                  <span className={s.badge} />
                </button>
              </div>
            </div>
          </div>
        </div>

        {/* TOTALES BAR */}
        <div className={s.totalsBar}>
          <div className={s.totalItem}>
            <span className={s.totalLabel}>Total de Percepciones:</span>
            <span className={s.totalValue}>{money(c.totalPercepciones)}</span>
          </div>
          <div className={s.totalItem}>
            <span className={s.totalLabel}>Total de Deducciones:</span>
            <span className={s.totalValue}>{money(c.totalDeducciones)}</span>
          </div>
          <div className={`${s.totalItem} ${s.success}`}>
            <span className={s.totalLabel}>Neto:</span>
            <span className={s.totalValue}>{money(c.neto)}</span>
          </div>
          <div className={s.totalItem}>
            <span className={s.totalLabel}>Recibo:</span>
            <span className={s.totalValue}>{r.numRecibo ?? '—'}</span>
          </div>
          <div className={s.totalItem}>
            <span className={s.totalLabel}>N° de Cuenta:</span>
            <span className={s.totalValue}>{r.numCuenta ?? '—'}</span>
          </div>
        </div>
      </div>

      <CommentsModal
        open={openComments}
        onClose={() => setOpenComments(false)}
        loading={loadingComments}
        error={errorComments}
        items={commentsData?.valores ?? []}
        header={commentsHeader}
        curp={p.curp}
        periodo={String(queryPeriodo)}
        anio={String(queryAnio)}
      />


      <PensionesDrawer
        open={openPensiones}
        onClose={() => setOpenPensiones(false)}
        loading={loadingPensiones}
        error={errorPensiones}
        items={pensionesData?.pension ?? []}
        header={pensionesData?.pensionCabeceraSimp ?? null}
      />
    </div>
  );
}
