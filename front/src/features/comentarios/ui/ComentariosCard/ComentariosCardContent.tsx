import { Eye } from "lucide-react";

import type { PostCommentItem } from "../../types/comentarios.post.types";

import s from "./ComentariosCardContent.module.css";

import {
  formatMoney,
  formatPeriodo,
  textOrDash,
  truncateComment,
} from "../../utils/comentariosCard.utils";

type Props = {
  filteredItems: PostCommentItem[];
  paginatedItems: PostCommentItem[];
  hasActiveFilters: boolean;
  startItem: number;
  endItem: number;
  page: number;
  totalPages: number;
  onClear: () => void;
  onSelectItem: (item: PostCommentItem) => void;
  onPreviousPage: () => void;
  onNextPage: () => void;
  onPageChange: (page: number) => void;
};

export default function ComentariosCardContent({
  filteredItems,
  paginatedItems,
  hasActiveFilters,
  startItem,
  endItem,
  page,
  totalPages,
  onClear,
  onSelectItem,
  onPreviousPage,
  onNextPage,
  onPageChange,
}: Props) {
  return (
    <>
      {filteredItems.length === 0 ? (
        <div className={s.empty}>
          <p className={s.emptyTitle}>No se encontraron registros</p>
          <p className={s.emptyDescription}>
            Intenta ajustar los filtros de búsqueda o verifica que existan comentarios
            registrados.
          </p>

          {hasActiveFilters && (
            <button className={s.emptyAction} type="button" onClick={onClear}>
              Limpiar filtros
            </button>
          )}
        </div>
      ) : (
        <>
          <div className={s.tableShell}>
            <table className={s.table}>
              <thead>
                <tr>
                  <th>Periodo</th>
                  <th>Imp. inicial</th>
                  <th>Imp. final</th>
                  <th>Diferencia</th>
                  <th>Tipo de pago</th>
                  <th>Comentario</th>
                  <th>Ver</th>
                </tr>
              </thead>

              <tbody>
                {paginatedItems.map((item, index) => (
                  <tr
                    key={`${item.nombreTabla}-${item.id}-${item.anio}-${item.qna}-${index}`}
                    className={s.clickableRow}
                    onClick={() => onSelectItem(item)}
                  >
                    <td data-label="Periodo">
                      <strong className={s.period}>{formatPeriodo(item)}</strong>
                    </td>

                    <td data-label="Imp. inicial">
                      <strong className={s.money}>{formatMoney(item.importeInicial)}</strong>
                    </td>

                    <td data-label="Imp. final">
                      <strong className={s.money}>{formatMoney(item.importeFinal)}</strong>
                    </td>

                    <td data-label="Diferencia">
                      <strong className={s.money}>{formatMoney(item.diferencia)}</strong>
                    </td>

                    <td data-label="Tipo de pago">{textOrDash(item.formaPago)}</td>

                    <td data-label="Comentario">{truncateComment(item.comentario)}</td>

                    <td className={s.actions}>
                      <button
                        className={s.iconButton}
                        type="button"
                        aria-label={`Ver detalle del periodo ${formatPeriodo(item)}`}
                        onClick={(event) => {
                          event.stopPropagation();
                          onSelectItem(item);
                        }}
                      >
                        <Eye size={16} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <footer className={s.footer}>
            <p className={s.footerText}>
              Mostrando {startItem}-{endItem} de {filteredItems.length} registros
            </p>

            <div className={s.pagination}>
              <button
                className={s.pageButton}
                type="button"
                onClick={onPreviousPage}
                disabled={page === 1}
              >
                {"<"}
              </button>

              {Array.from({ length: totalPages }, (_, index) => index + 1).map(
                (pageNumber) => (
                  <button
                    key={pageNumber}
                    className={`${s.pageButton} ${
                      pageNumber === page ? s.pageButtonActive : ""
                    }`}
                    type="button"
                    onClick={() => onPageChange(pageNumber)}
                  >
                    {pageNumber}
                  </button>
                )
              )}

              <button
                className={s.pageButton}
                type="button"
                onClick={onNextPage}
                disabled={page === totalPages}
              >
                {">"}
              </button>
            </div>
          </footer>
        </>
      )}
    </>
  );
}