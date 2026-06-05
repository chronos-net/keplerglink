import styles from './historico-davs-view.module.css';
import type { HistoricoDavsResponseNormalized, HistoricoDavsTipo, HistoricoDavsItem } from '../types/historicoDavs.types';

type Props = {
  data: HistoricoDavsResponseNormalized;
};

const SECCIONES: Array<{ key: HistoricoDavsTipo; title: string; subtitle: string }> = [
  { key: 'asesoria', title: 'Asesoría', subtitle: 'Movimientos de asesoría' },
  { key: 'tramite', title: 'Trámite', subtitle: 'Movimientos de trámite' },
  { key: 'solicitud', title: 'Solicitud', subtitle: 'Movimientos de solicitud' },
  { key: 'entrega', title: 'Entrega', subtitle: 'Movimientos de entrega' },
];

function formatFecha(iso: string): string {
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return iso;
  return d.toLocaleDateString('es-MX', { year: 'numeric', month: '2-digit', day: '2-digit' });
}

function Row({ item }: { item: HistoricoDavsItem }) {
  return (
    <div className={styles.row}>
      <div className={styles.left}>
        <div className={styles.folio}>
          <span className={styles.tag}>Folio</span> {item.folioDocumento}
        </div>
        <div className={styles.meta}>
          <span>cveKdm1: <b>{item.cveKdm1}</b></span>
          <span>•</span>
          <span>Fecha: <b>{formatFecha(item.fecha)}</b></span>
        </div>

        {item.destinatarioCheque ? (
          <div className={styles.desc}>{item.destinatarioCheque}</div>
        ) : (
          <div className={styles.descMuted}>Sin destinatario/descripcion.</div>
        )}
      </div>

      <div className={styles.right}>
        <div className={styles.kv}>
          <span className={styles.k}>Neyemp</span>
          <span className={styles.v}>{item.neyemp}</span>
        </div>
      </div>
    </div>
  );
}

export default function HistoricoDavsView({ data }: Props) {
  return (
    <div className={styles.wrap}>
      {SECCIONES.map((s) => {
        const items = data[s.key];
        return (
          <section key={s.key} className={styles.card}>
            <header className={styles.cardHeader}>
              <div>
                <h2 className={styles.h2}>{s.title}</h2>
                <p className={styles.sub}>{s.subtitle}</p>
              </div>
              <div className={styles.count}>{items.length}</div>
            </header>

            {items.length === 0 ? (
              <div className={styles.emptyMini}>Sin movimientos.</div>
            ) : (
              <div className={styles.list}>
                {items.map((it) => (
                  <Row key={`${s.key}-${it.cveKdm1}-${it.folioDocumento}`} item={it} />
                ))}
              </div>
            )}
          </section>
        );
      })}
    </div>
  );
}
