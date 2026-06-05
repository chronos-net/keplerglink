import { request } from '@/lib/apis'
import type { DescPlazasRequest, DescPlazasResponseDto } from '../types/desc-plazas.dto'

export async function postDescPlazas(
  payload: DescPlazasRequest,
  opts?: { signal?: AbortSignal }
): Promise<DescPlazasResponseDto> {
  
  const res = await request<DescPlazasResponseDto>({
    url: '/api/descPlazas/validacionDescPlazas',
    method: 'POST',
    data: payload,
    signal: opts?.signal,
  })

  return res.data
}
