// src/features/anualizado/pdf/generateAnualizadoPdf.ts

import { AnualizadoResponse } from "../types/anualizado.dto";


export const generateAnualizadoPdf = async (anio: number, data: AnualizadoResponse) => {
    const res = await fetch("/api/anualizado/pdf", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ anio, data }),
    });

    if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(text || "Error al generar PDF");
    }

    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `anualizado_${anio}.pdf`;
    a.click();
    URL.revokeObjectURL(url);
};