import { FilterX, MessageSquareText } from "lucide-react";
import s from "./ComentariosCardHeader.module.css";

type Props = {
    total: number;
    hasActiveFilters: boolean;
    onClear: () => void;
};

export default function ComentariosCardHeader({
    total,
    hasActiveFilters,
    onClear,
}: Props) {
    return (
        <header className={s.cardHeader}>
            <div className={s.cardTitleGroup}>
                <div className={s.cardIcon}>
                    <MessageSquareText size={18} className={s.titleIcon} />
                </div>

                <div>
                    <h2 className={s.title}>Registros encontrados</h2>
                    <p className={s.subtitle}>{total} registros en historial</p>
                </div>
            </div>

            <button
                className={s.clearButton}
                type="button"
                onClick={onClear}
                disabled={!hasActiveFilters}
            >
                <FilterX size={16} />
                <span>Limpiar filtros</span>
            </button>
        </header>
    );
}
