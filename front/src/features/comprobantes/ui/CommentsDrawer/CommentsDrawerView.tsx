'use client'

import s from './comments-drawer-v2.module.css';

import type { GetComentariosCabecera, GetComentariosValor } from '@/features/comentarios/types/comentarios.get.types';
import { useCommentsDrawerView } from './hooks/useCommentsDrawerView';

import CommentsDrawerHeader from './components/CommentsDrawerHeader';
import CommentsLoadingState from './components/CommentsLoadingState';
import CommentsErrorState from './components/CommentsErrorState';
import CommentsSummaryHero from './components/CommentsSummaryHero';
import CommentsAmountsSummary from './components/CommentsAmountsSummary';
import CommentsFilterRow from './components/CommentsFilterRow';
import CommentsEmptyState from './components/CommentsEmptyState';
import CommentRecordCard from './components/CommentRecordCard';



type Props = {
  open: boolean;
  onClose: () => void;
  loading: boolean;
  error: string | null;
  items: GetComentariosValor[];
  header?: GetComentariosCabecera | null;
  curp?: string | null;
  periodo?: string;
  anio?: string;
};

export default function CommentsDrawerView({
  open,
  onClose,
  loading,
  error,
  items,
  header,
  curp,
  periodo,
  anio,
}: Props) {


  const {
    filter,
    setFilter,
    toggleComment,
    hasRows,
    filteredItems,
    records,
    totalInicial,
    totalFinal,
    totalDiferencia,
    withCommentsCount,
    withoutCommentsCount,
  } = useCommentsDrawerView({ items, header, loading, error });

  const hasFilteredRecords = records.length > 0;

  if (!open) return null;

  return (
    <div className={s.backdrop} role="dialog" aria-modal="true">
      <aside className={s.sheet}>

        <CommentsDrawerHeader onClose={onClose} />

        <div className={s.body}>
          {loading && (
            <CommentsLoadingState />
          )}

          {!loading && error && (
            <CommentsErrorState error={error} />
          )}

          {!loading && !error && (
            <>
              <CommentsSummaryHero
                header={header}
                firstItem={items[0] ?? null}
                totalItems={items.length}
                curp={curp}
                periodo={periodo}
                anio={anio}
              />

              <CommentsAmountsSummary
                totalInicial={totalInicial}
                totalFinal={totalFinal}
                totalDiferencia={totalDiferencia}
              />

              <section className={s.recordsSection}>
                <div className={s.sectionHeader}>
                  <p>Registro de cada Periodo</p>
                  <span>
                    {filteredItems.length} de {items.length}
                  </span>
                </div>

                {hasRows && (
                  <CommentsFilterRow
                    filter={filter}
                    totalItems={items.length}
                    withCommentsCount={withCommentsCount}
                    withoutCommentsCount={withoutCommentsCount}
                    onFilterChange={setFilter}
                  />
                )}

                {/* Cuando no hay comentarios por si falla la API */}
                {!hasRows && (
                  <CommentsEmptyState />
                )}

                {hasRows && hasFilteredRecords && (
                  <ul className={s.recordList}>
                    {records.map(({ item, recordKey, hasComment, commentOpen }) => (
                      <CommentRecordCard
                        key={recordKey}
                        item={item}
                        header={header}
                        recordKey={recordKey}
                        hasComment={hasComment}
                        commentOpen={commentOpen}
                        onToggleComment={toggleComment}
                      />
                    ))}
                  </ul>
                )}

                {hasRows && !hasFilteredRecords && (
                  <CommentsEmptyState
                    title={
                      filter === 'with'
                        ? 'No hay registros con comentario'
                        : filter === 'without'
                          ? 'No hay registros sin comentario'
                          : 'No hay registros para mostrar'
                    }
                    description="Cambia el filtro para consultar los registros disponibles."
                  />
                )}
              </section>
            </>
          )}
        </div>
      </aside>
    </div>
  );
}
