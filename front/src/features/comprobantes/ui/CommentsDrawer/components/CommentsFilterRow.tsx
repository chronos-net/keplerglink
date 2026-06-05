import type { CommentsDrawerFilter } from '../hooks/useCommentsDrawerView';

import styles from './CommentsFilterRow.module.css';

type CommentsFilterRowProps = {
    filter: CommentsDrawerFilter;
    totalItems: number;
    withCommentsCount: number;
    withoutCommentsCount: number;
    onFilterChange: (filter: CommentsDrawerFilter) => void;
};

export default function CommentsFilterRow({
    filter,
    totalItems,
    withCommentsCount,
    withoutCommentsCount,
    onFilterChange,
}: CommentsFilterRowProps) {
    return (
        <div className={styles.filterRow} aria-label="Filtrar registros">
            <button
                className={`${styles.filterChip} ${filter === 'all' ? styles.filterChipActive : ''
                    }`}
                type="button"
                onClick={() => onFilterChange('all')}
            >
                Todos
                <strong>{totalItems}</strong>
            </button>

            <button
                className={`${styles.filterChip} ${filter === 'with' ? styles.filterChipActiveOk : ''
                    }`}
                type="button"
                onClick={() => onFilterChange('with')}
            >
                Con comentario
                <strong>{withCommentsCount}</strong>
            </button>

            <button
                className={`${styles.filterChip} ${filter === 'without' ? styles.filterChipActiveMuted : ''
                    }`}
                type="button"
                onClick={() => onFilterChange('without')}
            >
                Sin comentario
                <strong>{withoutCommentsCount}</strong>
            </button>
        </div>
    );
}
