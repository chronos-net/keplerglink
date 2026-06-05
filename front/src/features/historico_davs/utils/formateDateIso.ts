import { HistoricoDavsTipo, Tabfilter } from "../types/historicoDavs.types";

export const formatDateISO = (dateStr: string): string => {
    const [y, m, d] = dateStr.split('-');
    if (!y || !m || !d) return dateStr;
    return `${d}/${m}/${y}`;
}

export const tipoLabel = (tipo: HistoricoDavsTipo): string => {
    switch (tipo) {
        case 'asesoria':
            return 'Asesoría';
        case 'tramite':
            return 'Trámite';
        case 'solicitud':
            return 'Solicitud';
        case 'entrega':
            return 'Entregado';
        default:
            return tipo;
    }
}

export const TAB_FILTERS: Tabfilter[] = [
    { value: 'todos', label: 'Todos' },
    { value: 'asesoria', label: 'Asesorías' },
    { value: 'tramite', label: 'Trámites' },
    { value: 'solicitud', label: 'Solicitudes' },
    { value: 'entrega', label: 'Entregados' },
];