// src/features/comprobantes/types/comprobante.types.ts

export type ConceptRow = { clave: string; descripcion: string; importe: number };

export type ApiEmpleado = {
    neyemp: string;
    nombre: string;
    curp: string;
    rfc: string;
    iss: string;
};

export type ApiRecibo = {
    numRecibo: string;
    bancoFinal: string;
    banco: string;
    numCuenta: string;
    lugarPago: string | null;
};

export type ApiPlaza = {
    plazaId: string;
    adsc: string;
    puesto: string;
    leyendaPuesto: string;
    dependencia: string;
    centroTrabajo: string;
    lugarPago: string | null;
};

export type ApiResumen = {
    totalPercepciones: number;
    totalDeducciones: number;
    neto: number;
};

export type ApiConceptoImporteDesc = {
    codigo: string;
    descripcion: string;
    importe: number;
};

export type ApiMultiPlazaItem = {
    plaza: string;
    puesto: string;
    leyendaPuesto: string;
    adsc: string;
    neto: number;
};

export type RawComprobanteResponse = {
    empleado: ApiEmpleado;
    recibo: ApiRecibo;
    plaza: ApiPlaza;
    resumen: ApiResumen;
    percepciones?: ApiConceptoImporteDesc[];
    deducciones?: ApiConceptoImporteDesc[];
    multiplazas?: ApiMultiPlazaItem[];
};


export type ComprobanteItemUI = {
    codigo: string;       // normalizado (padStart(4))
    descripcion: string;
    importe: number;      // ya sumado si venía repetido
    ocurrencias: number;  // debug útil
};

export type ComprobanteResponse = {
    empleado: ApiEmpleado;
    recibo: Omit<ApiRecibo, "lugarPago"> & { lugarPago?: string };
    plaza: Omit<ApiPlaza, "lugarPago"> & { lugarPago?: string };
    resumen: ApiResumen;

    percepciones: ComprobanteItemUI[];
    deducciones: ComprobanteItemUI[];
    multiplazas: ApiMultiPlazaItem[];

    hasMultiPlaza: boolean;
};


/** UI Model (normalizado, seguro para UI) */

export const normalizeCode = (v: unknown) =>
    String(v ?? "").trim().padStart(4, "0");


function groupAndSum(items: ApiConceptoImporteDesc[] | undefined): ComprobanteItemUI[] {
    const map = new Map<string, ComprobanteItemUI>();

    for (const it of items ?? []) {
        const codigo = normalizeCode(it.codigo);
        const descripcion = String(it.descripcion ?? "").trim();
        const importe = Number(it.importe) || 0;

        // si llega el mismo código con distinta descripción, NO lo mezclamos
        const key = `${codigo}||${descripcion}`;

        const prev = map.get(key);
        if (!prev) {
            map.set(key, { codigo, descripcion, importe, ocurrencias: 1 });
        } else {
            prev.importe += importe;
            prev.ocurrencias += 1;
        }
    }

    return Array.from(map.values()).sort((a, b) => {
        if (a.codigo !== b.codigo) return a.codigo.localeCompare(b.codigo);
        return a.descripcion.localeCompare(b.descripcion);
    });
}

export const normalizeComprobante = (raw: RawComprobanteResponse): ComprobanteResponse => {
    return {
        empleado: raw.empleado,
        recibo: { ...raw.recibo, lugarPago: raw.recibo?.lugarPago ?? undefined },
        plaza: { ...raw.plaza, lugarPago: raw.plaza?.lugarPago ?? undefined },
        resumen: raw.resumen,

        percepciones: groupAndSum(raw.percepciones),
        deducciones: groupAndSum(raw.deducciones),
        multiplazas: Array.isArray(raw.multiplazas) ? raw.multiplazas : [],

        hasMultiPlaza: (raw.multiplazas?.length ?? 0) > 1,
    };
}