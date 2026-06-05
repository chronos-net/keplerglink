import { request } from "@/lib/apis";
import { AnualizadoRequest, AnualizadoResponse, normalizeAnualizado, RawAnualizadoResponse } from "../types/anualizado.dto";

export async function postValidacionesAnualizado(
  payload: AnualizadoRequest,
  opts?: { signal?: AbortSignal }
): Promise<AnualizadoResponse> {
  const res = await request<RawAnualizadoResponse>({
    url: "/api/anualisado/validacionAnualisado",
    method: "POST",
    data: payload,
    signal: opts?.signal,
  });

  return normalizeAnualizado(res.data);
}