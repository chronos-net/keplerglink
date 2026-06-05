'use client';

import s from './PrestamosGlinkView.module.css';
import type {
  PrestamosGlinkResponse,
  PrestamoItem,
  CabeceraPrestamos,
} from '../types/prestamos-glink.types';

type PrestamosPayload = PrestamosGlinkResponse & {
  // typo del backend
  cabeseraPrestamos?: CabeceraPrestamos;
};

const money = (n: number | string | null | undefined) =>
  `$${Number(n ?? 0).toFixed(2)}`;

export default function PrestamosGlinkView({
  data,
}: {
  data: PrestamosGlinkResponse;
}) {
  const payload = data as PrestamosPayload;

  // ✅ soporta cabeceraPrestamos y cabeseraPrestamos
  const h = payload.cabeceraPrestamos ?? payload.cabeseraPrestamos;
  const items: PrestamoItem[] = payload.descPrestamos ?? [];

  if (!h) {
    return (
      <section className={s.panel}>
        <div className={s.empty}>
          Sin cabecera en la respuesta (revisa si viene como{' '}
          <code>cabeseraPrestamos</code>).
        </div>
      </section>
    );
  }

  return (
    <section className={s.panel}>
      <header className={s.head}>
        <div>
          <div className={s.title}>Resultado</div>
          <div className={s.sub}>
            {h.negnom} · {h.neyemp} · {h.rfc}
            {h.fecha_in ? ` · ${h.fecha_in}` : ''}
          </div>
        </div>

        <div className={s.badge}>{items.length} préstamo(s)</div>
      </header>

      <div className={s.tableWrap}>
        <div className={s.table}>
          <div className={s.th}>Cve</div>
          <div className={s.th}>Puesto</div>
          <div className={s.th}>Doc</div>
          <div className={s.thRight}>Imp. total</div>
          <div className={s.thRight}>Renta</div>
          <div className={s.thRight}>Saldo</div>
          <div className={s.thRight}>Plazos</div>
          <div className={s.thRight}>Qnas x pagar</div>

          {items.length === 0 ? (
            <div className={s.empty}>Sin préstamos para ese periodo.</div>
          ) : (
            items.map((it, idx) => (
              <div key={idx} className={s.row}>
                <div className={s.tdMono}>{it.cve_ded ?? '-'}</div>
                <div className={s.tdMono}>{it.puesto ?? '-'}</div>
                <div className={s.tdMono}>{it.doc_ref ?? '-'}</div>
                <div className={s.tdRight}>{money(it.imp_total)}</div>
                <div className={s.tdRight}>{money(it.imp_renta)}</div>
                <div className={s.tdRight}>{money(it.saldo)}</div>
                <div className={s.tdRight}>{Number(it.plazos ?? 0)}</div>
                <div className={s.tdRight}>{Number(it.qnas_x_pagar ?? 0)}</div>
              </div>
            ))
          )}
        </div>
      </div>
    </section>
  );
}
