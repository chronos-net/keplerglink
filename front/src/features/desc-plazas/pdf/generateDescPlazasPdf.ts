import type { DescPlazasResponseDto } from '../types/desc-plazas.dto'

export async function generateDescPlazasPdf(
  anio: number,
  quincena: string,
  data: DescPlazasResponseDto
) {
  const res = await fetch('/api/desc-plazas/pdf', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json; charset=utf-8' },
    body: JSON.stringify({ anio, quincena, data }),
  })

  if (!res.ok) {
    const text = await res.text().catch(() => '')
    throw new Error(text || 'Error al generar PDF')
  }

  const blob = await res.blob()
  const url = URL.createObjectURL(blob)

  const a = document.createElement('a')
  a.href = url
  a.download = `desglose_plazas_${anio}_${quincena}.pdf`
  a.click()

  URL.revokeObjectURL(url)
}
