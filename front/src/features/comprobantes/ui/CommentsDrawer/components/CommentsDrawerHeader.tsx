import { X } from 'lucide-react';

import s from './CommentsDrawerHeader.module.css';

type Props = {
    onClose: () => void;
};

export default function CommentsDrawerHeader({ onClose }: Props) {
    return (
        <header className={s.header}>
            <div className={s.headerLeft}>
                <div className={s.headerCopy}>
                    <h3 className={s.title}>Detalles del Movimiento</h3>
                    <span className={s.subtitle}>Comentarios del recibo seleccionado</span>
                </div>
            </div>

            <button
                className={s.closeBtn}
                onClick={onClose}
                aria-label="Cerrar"
                type="button"
            >
                <X size={18} />
            </button>
        </header>
    );
}