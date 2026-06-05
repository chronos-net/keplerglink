'use client';

import { useState, FormEvent } from 'react';
import { Search, Calendar, Hash, Filter } from 'lucide-react';
import s from './anualizado-filters.module.css';

type Props = {
  busy?: boolean;
  onSubmit: (p: { anio: number; neyemp?: string; nombreSp?: string }) => void;
};

export default function AnualizadoFilters({ busy, onSubmit }: Props) {
  const [anio, setAnio] = useState<number>(new Date().getFullYear());
  const [neyemp, setNeyemp] = useState('');
  const [nombreSp, setNombreSp] = useState('');

  const handle = (e: FormEvent) => {
    e.preventDefault();
    onSubmit({
      anio,
      ...(neyemp.trim() ? { neyemp: neyemp.trim() } : {}),
      ...(nombreSp.trim() ? { nombreSp: nombreSp.trim() } : {}),
    });
  };

  return (
    <form className={s.wrapper} onSubmit={handle}>
      {/* Buscador grande */}
      <div className={`${s.group} ${s.wide}`}>
        <Search className={s.icon} aria-hidden />
        <input
          className={`${s.input} ${s.pill}`}
          placeholder="Nombre o clave"
          value={nombreSp}
          onChange={(e) => setNombreSp(e.target.value)}
          aria-label="Buscar por nombre o clave"
        />
      </div>

      {/* Año (pill corto) */}
      <div className={s.group}>
        <Calendar className={s.icon} aria-hidden />
        <input
          className={`${s.input} ${s.pill} ${s.sm}`}
          type="number"
          min={2000}
          max={2100}
          value={anio}
          onChange={(e) => setAnio(parseInt(e.target.value || '0', 10))}
          aria-label="Año"
        />
      </div>

      {/* Neyemp / Clave (pill corto) */}
      <div className={s.group}>
        <Hash className={s.icon} aria-hidden />
        <input
          className={`${s.input} ${s.pill} ${s.sm}`}
          placeholder="Clave"
          value={neyemp}
          onChange={(e) => setNeyemp(e.target.value)}
          aria-label="Clave (Neyemp)"
          inputMode="numeric"
        />
      </div>

      <button type="submit" className={s.btn} disabled={busy}>
        <Filter size={18} />
        <span>{busy ? 'Filtrando…' : 'Filtrar'}</span>
      </button>
    </form>
  );
}
