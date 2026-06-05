'use client';

import { X } from 'lucide-react';
import type { ReactNode } from 'react';
import s from './css/BaseModal.module.css';

type BaseModalWidth = 'md' | 'lg' | 'xl';

type Props = {
  open: boolean;
  title?: string;
  onClose: () => void;
  children: ReactNode;
  width?: BaseModalWidth;
};

export default function BaseModal({
  open,
  title,
  onClose,
  children,
  width
}: Props) {
  if (!open) return null;

  return (
    <div className={s.overlay}>
      <div
        className={`${s.modal} ${s['width_' + width]}`}
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-label={title ?? 'Modal'}
      >
        <div className={s.header}>
          <div className={s.title}>{title ?? ''}</div>

          <button
            type="button"
            className={s.closeBtn}
            onClick={onClose}
            aria-label="Cerrar"
            title="Cerrar"
          >
            <X size={18} />
          </button>
        </div>

        <div className={s.body}>{children}</div>
      </div>
    </div>
  );
}