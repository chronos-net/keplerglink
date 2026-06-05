'use client';
import { useMemo, useState } from 'react';
import type { ReciboRequest } from '../types/payroll.types';
import s from './comprobante-form.module.css';

type Props = { onSubmit: (p: ReciboRequest) => void; busy?: boolean };

export default function ComprobanteForm({ onSubmit, busy }: Props) {
  const thisYear = new Date().getFullYear();
  const years = useMemo(() => Array.from({ length: 8 }, (_, i) => thisYear - i), [thisYear]);

  const [quincena, setQuincena] = useState('01');
  const [anio, setAnio] = useState<number | ''>(thisYear);
  const [nombreSp, setNombreSp] = useState('');
  const [neyemp, setneyemp] = useState('');
  const [msg, setMsg] = useState<string | null>(null);

  function handle(e: React.FormEvent) {
    e.preventDefault();
    const nombre = nombreSp.trim();
    const clave = neyemp.trim();

    if (!clave && !nombre) {
      setMsg('Ingresa Clave SP o Nombre (uno de los dos).');
      return;
    }
    setMsg(null);
    onSubmit({
      quincena,
      anio: Number(anio),
      ...(clave ? { neyemp: clave } : {}),
      ...(nombre ? { nombreSp: nombre } : {}),
    });
  }

  return (
    <form className={s.form} onSubmit={handle} noValidate>
      <div className={s.row}>
        <label className={s.field}>
          <span className={s.label}>Nombre o clave</span>
          <div className={s.dual}>
            <input
              className={s.input}
              value={neyemp}
              onChange={(e) => setneyemp(e.target.value)}
              placeholder="Clave SP (opcional)"
              inputMode="numeric"
              aria-label="Clave del servidor público"
            />
            <span className={s.or}>o</span>
            <input
              className={s.input}
              value={nombreSp}
              onChange={(e) => setNombreSp(e.target.value)}
              placeholder="Nombre del Servidor Público (opcional)"
              aria-label="Nombre del Servidor Público"
            />
          </div>
        </label>

        <label className={s.field}>
          <span className={s.label}>Año</span>
          <select
            className={s.select}
            value={anio}
            onChange={(e) => setAnio(Number(e.target.value))}
            aria-label="Año del recibo"
          >
            {years.map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </label>

        <label className={s.field}>
          <span className={s.label}>Quincena</span>
          <select
            className={s.select}
            value={quincena}
            onChange={(e) => setQuincena(e.target.value)}
            aria-label="Quincena"
          >
            {Array.from({ length: 24 }).map((_, i) => {
              const q = String(i + 1).padStart(2, '0');
              return (
                <option key={q} value={q}>
                  {q}
                </option>
              );
            })}
          </select>
        </label>

        <div className={s.actions}>
          <button className={s.btn} disabled={busy} type="submit" aria-busy={busy || undefined}>
            {busy && <span className={s.spinner} aria-hidden />}
            {busy ? 'Generando…' : 'Generar recibo'}
          </button>
          {msg && <div role="alert" className={s.error}>{msg}</div>}
        </div>
      </div>
    </form>
  );
}
