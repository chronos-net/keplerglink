// src/features/comprobantes/pdf/generateComprobantePdf.ts

import { ComprobanteResponse } from "../types/comprobantes.types";

export const generateComprobantePdf = async (
  anio: number,
  quincena: string,
  data: ComprobanteResponse
) => {
  const res = await fetch("/api/comprobantes/pdf", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ anio, quincena, data }),
  });

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || "Error al generar PDF");
  }

  const blob = await res.blob();
  const url = URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = `comprobante_${anio}_${quincena}.pdf`;
  a.click();

  URL.revokeObjectURL(url);
};