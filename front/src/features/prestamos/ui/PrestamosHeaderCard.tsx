'use client';

import s from './prestamos-header-card.module.css';

type CabeceraPrestamos = {
  nombre: string;
  neyemp?: string | null;
  rfc?: string | null;
  fechaInicio?: string | null;
};

type Props = {
  cabecera: CabeceraPrestamos;
  totalPrestamos: number;
};

export default function PrestamosHeaderCard({
  cabecera,
  totalPrestamos,
}: Props) {
  const inicial =
    (cabecera.nombre?.trim()[0] ??
      cabecera.neyemp?.trim()[0] ??
      '?'
    ).toUpperCase();

  return (
    <div className={s.card}>
      <div className="avatar">{inicial}</div>

      <div className={s.main}>
        <div className={s.name}>{cabecera.nombre || '—'}</div>

        <div className={s.meta}>
          <span>
            <strong>Clave S.P.:</strong> {cabecera.neyemp || '—'}
          </span>
          <span className={s.dot} />
          <span>
            <strong>RFC:</strong> {cabecera.rfc || '—'}
          </span>
          <span className={s.dot} />
          <span>
            <strong>Fecha ingreso:</strong> {cabecera.fechaInicio || '—'}
          </span>
        </div>
      </div>

      <div className={s.badgeRight}>
        {totalPrestamos} préstamo(s) encontrado(s)
      </div>
    </div>
  );
}
