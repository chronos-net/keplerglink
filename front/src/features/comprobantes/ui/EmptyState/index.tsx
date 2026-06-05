'use client';

import Image from 'next/image';
import s from './EmptyState.module.css';

type Variant = 'default' | 'anualizadoError';

type Props = {
  title?: string;
  description?: string;
  imgSrc: string;   // ej. '/img/empty-bee.png'
  alt?: string;
  variant?: Variant;
};

export default function EmptyState({
  title = 'Sin resultados por ahora',
  description = 'Usa el filtro de arriba para generar tu recibo de nómina.',
  imgSrc,
  alt = 'Estado vacío',
  variant = 'default',
}: Props) {
  const isError = variant === 'anualizadoError';

  return (
    <div className={`${s.wrap} ${isError ? s.wrapError : ''}`}>
      <div className={`${s.card} ${isError ? s.cardError : ''}`}>
        <div className={`${s.pic} ${isError ? s.picError : ''}`}>
          <Image src={imgSrc} alt={alt} fill priority sizes="220px" />
        </div>
        <h3 className={s.title}>{title}</h3>
        <p className={s.desc}>{description}</p>
      </div>
    </div>
  );
}
