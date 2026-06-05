// src/features/comprobantes/services/comments.service.ts
import { request } from '@/lib/apis';

export type CommentItem = { comentario: string };

export type CommentsResponse = {
  comentarioCabecera: {
    periodo: string; // quincena
    anio: string;
    neyemp: string;
    adsc?: string | null;
    leyenda_adscripcion?: string | null;
    cheque?: string | null;
    puesto?: string | null;
    leyenda_puesto?: string | null;
    percep?: string | null;
    ded?: string | null;
    neto?: string | null;
    lug_pago?: string | null;
    num_cuenta?: string | null;
  };
  comentarios: CommentItem[];
};

export type FetchCommentsParams = {
  neyemp: string; // "850839867"
  periodo: string; // "15"
  anio: number | string; // 2007
};

export async function fetchComments(
  params: FetchCommentsParams,
  opts?: { signal?: AbortSignal }
): Promise<CommentsResponse> {
  const res = await request<CommentsResponse>({
    url: '/api/comentario/consultar',
    method: 'GET',
    params,
    signal: opts?.signal,
  });

  // por si el back se pone creativo y manda nulls
  return {
    comentarioCabecera: res.data?.comentarioCabecera ?? {
      periodo: String(params.periodo),
      anio: String(params.anio),
      neyemp: params.neyemp,
    },
    comentarios: res.data?.comentarios ?? [],
  };
}
