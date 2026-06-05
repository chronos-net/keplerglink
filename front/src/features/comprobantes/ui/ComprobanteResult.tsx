'use client';

import type { Recibo } from '../types/payroll.types';

const money = (n?: number) =>
  (n ?? 0).toLocaleString('es-MX', { style: 'currency', currency: 'MXN' });

export default function ComprobanteResult({ data }: { data?: Recibo | null }) {
  if (!data) return null;

  const p = data.datosPersonales;
  const c = data.datosCantidades;

  // Si prefieres totales calculados, descomenta:
  // const percepTotal = data.percepciones.reduce((acc, x) => acc + (x.importe ?? 0), 0);
  // const deducTotal  = data.deducciones.reduce((acc, x) => acc + (x.importe ?? 0), 0);

  return (
    <div style={{ display: 'grid', gap: 16 }}>
      {/* Datos personales */}
      <section style={{ border: '1px solid #1f1f22', borderRadius: 12, padding: 12 }}>
        <h3 style={{ marginTop: 0 }}>Datos personales</h3>
        {p ? (
          <div>
            <div><strong>Nombre:</strong> {p.nombre}</div>
            <div><strong>CURP:</strong> {p.curp}</div>
            <div><strong>RFC:</strong> {p.rfc}</div>
          </div>
        ) : <em>Sin datos</em>}
      </section>

      {/* Plazas / Dependencias */}
      <section style={{ border: '1px solid #1f1f22', borderRadius: 12, padding: 12 }}>
        <h3 style={{ marginTop: 0 }}>Puestos / Dependencias</h3>
        {data.datosPlazas.length ? (
          <ul>
            {data.datosPlazas.map((d, i) => (
              <li key={i}>
                {d.centro_trabajo} • Plaza {d.plaza}
                {d.categoria ? ` • ${d.categoria}` : ''}
                {d.leyenda_puesto ? ` — ${d.leyenda_puesto}` : ''}
                {d.leyenda_adscripcion ? ` (${d.leyenda_adscripcion})` : ''}
                {d.lugar_pago ? ` • Adscripción: ${d.lugar_pago}` : ''}
              </li>
            ))}
          </ul>
        ) : <em>Sin registros</em>}
      </section>

      {/* Cantidades */}
      <section style={{ border: '1px solid #1f1f22', borderRadius: 12, padding: 12 }}>
        <h3 style={{ marginTop: 0 }}>Cantidades</h3>
        {c ? (
          <ul>
            <li><strong>Total percepciones:</strong> {money(c.totalPercep)}</li>
            <li><strong>Total deducciones:</strong> {money(c.totalDed)}</li>
            <li><strong>Neto:</strong> {money(c.neto)}</li>
          </ul>
        ) : <em>Sin datos</em>}
      </section>

      {/* Percepciones */}
      <section style={{ border: '1px solid #1f1f22', borderRadius: 12, padding: 12 }}>
        <h3 style={{ marginTop: 0 }}>Percepciones</h3>
        <div><strong>Total:</strong> {money(c?.totalPercep)}</div>
        {data.percepciones.length ? (
          <ul>
            {data.percepciones.map((x, i) => (
              <li key={i}>
                {x.clave} — {x.descripcion}: {money(x.importe)}
              </li>
            ))}
          </ul>
        ) : <em>Sin percepciones</em>}
      </section>

      {/* Deducciones */}
      <section style={{ border: '1px solid #1f1f22', borderRadius: 12, padding: 12 }}>
        <h3 style={{ marginTop: 0 }}>Deducciones</h3>
        <div><strong>Total:</strong> {money(c?.totalDed)}</div>
        {data.deducciones.length ? (
          <ul>
            {data.deducciones.map((x, i) => (
              <li key={i}>
                {x.clave} — {x.descripcion}: {money(x.importe)}
              </li>
            ))}
          </ul>
        ) : <em>Sin deducciones</em>}
      </section>
    </div>
  );
}
