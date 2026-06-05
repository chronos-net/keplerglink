"use client";

import { useMemo, useState } from "react";

import type { PostCommentItem } from "../types/comentarios.post.types";

type UseComentariosCardOptions = {
  pageSize?: number;
};

export const useComentariosCard = (
  items: PostCommentItem[],
  options?: UseComentariosCardOptions
) => {
  const pageSize = options?.pageSize ?? 8;

  const [query, setQuery] = useState("");
  const [year, setYear] = useState("all");
  const [qna, setQna] = useState("all");
  const [selected, setSelected] = useState<PostCommentItem | null>(null);
  const [page, setPage] = useState(1);

  const years = useMemo(() => {
    return Array.from(
      new Set(
        items
          .map((item) => (item.anio ?? "").trim())
          .filter((value) => value.length > 0)
      )
    ).sort((a, b) => Number(b) - Number(a));
  }, [items]);

  const qnas = useMemo(() => {
    return Array.from(
      new Set(
        items
          .map((item) => (item.qna ?? "").trim())
          .filter((value) => value.length > 0)
      )
    ).sort((a, b) => Number(a) - Number(b));
  }, [items]);

  const filteredItems = useMemo(() => {
    const normalizedQuery = query.trim().toLocaleLowerCase("es-MX");

    return items.filter((item) => {
      const matchesYear = year === "all" || item.anio === year;
      const matchesQna = qna === "all" || item.qna === qna;

      if (!matchesYear || !matchesQna) return false;
      if (!normalizedQuery) return true;

      const periodo = `${item.qna}/${item.anio}`;
      const periodoConTexto = `QNA ${item.qna}/${item.anio}`;
      
      const haystack = [
        periodo,
        periodoConTexto,
        item.formaPago,
        item.comentario,
      ]
        .map((value) => String(value ?? "").toLocaleLowerCase("es-MX"))
        .join(" ");

      return haystack.includes(normalizedQuery);
    });
  }, [items, qna, query, year]);

  const totalPages = Math.max(1, Math.ceil(filteredItems.length / pageSize));

  const paginatedItems = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filteredItems.slice(start, start + pageSize);
  }, [filteredItems, page, pageSize]);

  const hasActiveFilters =
    query.trim().length > 0 || year !== "all" || qna !== "all";

  const startItem = filteredItems.length === 0 ? 0 : (page - 1) * pageSize + 1;
  const endItem = Math.min(page * pageSize, filteredItems.length);

  const clearFilters = () => {
    setQuery("");
    setYear("all");
    setQna("all");
    setPage(1);
  };

  const updateQuery = (value: string) => {
    setQuery(value);
    setPage(1);
  };

  const updateYear = (value: string) => {
    setYear(value);
    setPage(1);
  };

  const updateQna = (value: string) => {
    setQna(value);
    setPage(1);
  };

  const previousPage = () => {
    setPage((current) => Math.max(1, current - 1));
  };

  const nextPage = () => {
    setPage((current) => Math.min(totalPages, current + 1));
  };

  return {
    query,
    year,
    qna,
    selected,
    page,
    pageSize,
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
  };
};
