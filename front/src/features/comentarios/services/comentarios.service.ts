import { request } from "@/lib/apis";
import { GetComentariosRequest, GetComentariosResponse } from "../types/comentarios.get.types";

import type { PostCommentsRequest, PostCommentsResponse } from '../types/comentarios.post.types';

export const getComments = async (
  params: GetComentariosRequest,
  opts?: { signal?: AbortSignal }
): Promise<GetComentariosResponse> => {
  const res = await request<GetComentariosResponse>({
    url: "/api/comentario/consultar",
    method: "GET",
    params: {
      anio: params.anio,
      periodo: params.periodo,
      neyemp: params.neyemp,
    },
    signal: opts?.signal,
  });

  return res.data;
};


export const postComments = async (
  params: PostCommentsRequest,
  opts?: { signal?: AbortSignal }
): Promise<PostCommentsResponse> => {
  const res = await request<PostCommentsResponse>({
    url: '/api/comentarios/ServidorPublicoComentarios',
    method: 'POST',
    data: {
      neyemp: params.neyemp,
    },
    signal: opts?.signal,
  });

  return res.data;
};