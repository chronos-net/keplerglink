"use client";

import { useComentariosCard } from "../../hook/useComentariosCard";
import type { PostCommentsHeader, PostCommentItem } from "../../types/comentarios.post.types";
import s from "./ComentariosCard.module.css";

import ComentariosCardHeader from "./ComentariosCardHeader";
import ComentariosCardToolbar from "./ComentariosCardToolbar";
import ComentariosCardContent from "./ComentariosCardContent";
import ComentariosDetailModal from "./ComentariosDetailModal";

type ComentariosCardProps = {
  cabecera: PostCommentsHeader;
  items: PostCommentItem[];
};



export default function ComentariosCard({ cabecera, items }: ComentariosCardProps) {
  
  const {
    query,
    year,
    qna,
    selected,
    page,
    years,
    qnas,
    filteredItems,
    paginatedItems,
    totalPages,
    hasActiveFilters,
    startItem,
    endItem,
    setSelected,
    setPage,
    updateQuery,
    updateYear,
    updateQna,
    previousPage,
    nextPage,
    clearFilters,
  } = useComentariosCard(items);


  return (
    <section className={s.wrap}>
      <ComentariosCardHeader
        total={filteredItems.length}
        hasActiveFilters={hasActiveFilters}
        onClear={clearFilters}
      />

      <ComentariosCardToolbar
        query={query}
        year={year}
        qna={qna}
        years={years}
        qnas={qnas}
        onQueryChange={updateQuery}
        onYearChange={updateYear}
        onQnaChange={updateQna}
      />


      <ComentariosCardContent
        filteredItems={filteredItems}
        paginatedItems={paginatedItems}
        hasActiveFilters={hasActiveFilters}
        startItem={startItem}
        endItem={endItem}
        page={page}
        totalPages={totalPages}
        onClear={clearFilters}
        onSelectItem={setSelected}
        onPreviousPage={previousPage}
        onNextPage={nextPage}
        onPageChange={setPage}
      />

      <ComentariosDetailModal
        item={selected}
        cabecera={cabecera}
        onClose={() => setSelected(null)}
      />

    </section>
  );
}

