export type RequestDevsSolicitudes = {
    cveKdm1: number;
    neyemp: string;
    destinatarioCheque: string;
};

export type RawSolicitudPlano = {
    fecha: string;
    rfc: string;
    nombre: string;
    monto: number;
    plazo: number;
    fechaPago: string | null;
    comentario: string;
};

export type RawSolicitudFRD = RawSolicitudPlano;
export type RawSolicitudFRB = RawSolicitudPlano;

export type SolicitudConceptoItem = {
    descripcion: string;
    importe: number;
    importe2: number | null;
};

export type RawSolicitudPSE = {
    tipo: 'SOLICITUD';
    subtipo: 'PSE';
    folio: string;
    estatus: string;
    encabezado: {
        fecha: string;
        rfc: string;
        nombre: string;
    };
    resumenPago: {
        montoTotal: number;
        montoTotalTexto: string;
    };
    mensaje: {
        variant: string;
        textoCompleto: string;
    };
    conceptos: SolicitudConceptoItem[];
    movimientosComplementarios: SolicitudConceptoItem[];
};

export type RawSolicitudResponse =
    | RawSolicitudFRD
    | RawSolicitudFRB
    | RawSolicitudPSE;


export type DevsSolicitudResponse = {
    tipo: 'SOLICITUD';
    subtipo?: 'PSE' | 'FRD' | 'FRB';
    folio?: string;
    estatus?: string;
    encabezado: {
        fecha: string;
        rfc: string;
        nombre: string;
    };
    resumenPago: {
        montoTotal: number;
        montoTotalTexto: string;
    };
    mensaje: {
        variant: string;
        textoCompleto: string;
    };
    conceptos: SolicitudConceptoItem[];
    movimientosComplementarios: SolicitudConceptoItem[];
};

export type NormalizeSolicitudMeta = {
    subtipo?: 'PSE' | 'FRD' | 'FRB';
    folio?: string;
    estatus?: string;
};

const isRawSolicitudPSE = (raw: RawSolicitudResponse): raw is RawSolicitudPSE => {
    return (
        typeof raw === 'object' &&
        raw !== null &&
        'tipo' in raw &&
        'subtipo' in raw &&
        raw.tipo === 'SOLICITUD' &&
        raw.subtipo === 'PSE'
    );
}

export const normalizeSolicitudDetalle = (
    raw: RawSolicitudResponse,
    meta: NormalizeSolicitudMeta
): DevsSolicitudResponse => {
    if (isRawSolicitudPSE(raw)) {
        return {
            tipo: raw.tipo,
            subtipo: raw.subtipo,
            folio: raw.folio,
            estatus: raw.estatus,
            encabezado: {
                fecha: raw.encabezado.fecha,
                rfc: raw.encabezado.rfc,
                nombre: raw.encabezado.nombre,
            },
            resumenPago: {
                montoTotal: raw.resumenPago.montoTotal,
                montoTotalTexto: raw.resumenPago.montoTotalTexto,
            },
            mensaje: {
                variant: raw.mensaje.variant,
                textoCompleto: raw.mensaje.textoCompleto,
            },
            conceptos: raw.conceptos,
            movimientosComplementarios: raw.movimientosComplementarios,
        };
    }

    return {
        tipo: 'SOLICITUD',
        subtipo: meta.subtipo,
        folio: meta.folio ?? '',
        estatus: meta.estatus ?? 'ENTREGADO',
        encabezado: {
            fecha: raw.fecha,
            rfc: raw.rfc,
            nombre: raw.nombre,
        },
        resumenPago: {
            montoTotal: raw.monto,
            montoTotalTexto: '',
        },
        mensaje: {
            variant: 'paragraph',
            textoCompleto: raw.comentario,
        },
        conceptos: [],
        movimientosComplementarios: [],
    };
};