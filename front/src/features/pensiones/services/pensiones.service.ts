import { request } from "@/lib/apis"
import { DataPensiones, normalizePension, normalizePensionesGlink, PensionesGlinkRequest, PensionesLite, PensionesRequest, PensionesResponse, RawPensionResponse, ResponsePostPension } from "../types/pensiones.types"

export const getPensiones = async (
    params: PensionesRequest,
    opts?: { signal?: AbortSignal }
): Promise<PensionesResponse> => {

    const res = await request<RawPensionResponse>({
        url: "/api/pension/consultar",
        method: "GET",
        params: {
            anio: params.anio,
            periodo: params.periodo,
            neyemp: params.neyemp
        },
        signal: opts?.signal,
    });

    return normalizePension(res.data, params);
}

export const buscarPensionesGlink = async (
    q: string,
    opts?: { signal?: AbortSignal }
): Promise<PensionesLite[]> => {

    const term = (q ?? '').trim();
    if (!term) return [];

    const res = await request<PensionesLite[]>({
        url: '/api/buscadorPensionesGlink',
        method: 'GET',
        params: { q: term },
        signal: opts?.signal,
    });

    return Array.isArray(res.data) ? res.data : [];
}


export const consultarPensionesGlink = async (
    params: PensionesGlinkRequest,
    opts?: { signal?: AbortSignal }
    
): Promise<DataPensiones> => {

    const res = await request<ResponsePostPension>({
        url: "/api/pensionesGLINK/consultar",
        method: "POST",
        data: { neyemp: params.neyemp },
        signal: opts?.signal,
    });

    return normalizePensionesGlink(res.data);
};