import type {
    ComentariosCatalogo,
    GetComentariosValor,
} from '@/features/comentarios/types/comentarios.get.types';

const empty = '-';
const notReported = 'No informado';

export const pick = (value?: string | null) => {
    const text = String(value ?? '').trim();
    return text.length ? text : empty;
};

export const pickCatalog = (value?: ComentariosCatalogo | null) => {
    const clave = String(value?.clave ?? '').trim();
    const descripcion = String(value?.descripcion ?? '').trim();

    if (clave && descripcion) return `${clave} - ${descripcion}`;
    if (clave) return clave;
    if (descripcion) return descripcion;

    return empty;
};

export const isZeroAccount = (value?: string | null) => {
    const text = String(value ?? '').replace(/\D/g, '');
    return text.length > 0 && /^0+$/.test(text);
};

export const pickAccount = (value?: string | null) => {
    const text = String(value ?? '').trim();
    if (!text || isZeroAccount(text)) return notReported;
    return text;
};

export const money = (value?: number | string | null, fallbackZero = false): string => {
    if (value === null || value === undefined || value === '') {
        if (!fallbackZero) return empty;

        return (0).toLocaleString('es-MX', {
            style: 'currency',
            currency: 'MXN',
        });
    }

    const amount = typeof value === 'number' ? value : Number(value);
    if (Number.isNaN(amount)) return String(value);

    return amount.toLocaleString('es-MX', {
        style: 'currency',
        currency: 'MXN',
    });
};

export const sumMoney = (
    items: GetComentariosValor[],
    key: 'importeInicial' | 'importeFinal' | 'diferencia'
) => {
    return items.reduce((total, item) => {
        const value = item[key];
        return total + (typeof value === 'number' && !Number.isNaN(value) ? value : 0);
    }, 0);
};

export const dateFromComment = (text: string) => {
    const match = text.match(/\b(\d{2}\/\d{2}\/\d{4})\b/);
    return match ? match[1] : null;
};

export const formatDate = (value?: string | null) => {
    const raw = value?.trim();
    if (!raw) return null;

    const [datePart] = raw.split(' ');
    const [year, month, day] = datePart.split('-');
    if (!year || !month || !day) return raw;

    return `${day}/${month}/${year}`;
};

export const itemDate = (item: GetComentariosValor) => {
    return dateFromComment(item.comentario ?? '') ?? formatDate(item.fechaCaptura) ?? empty;
};

export const hasCommentText = (item: GetComentariosValor) => {
    return String(item.comentario ?? '').trim().length > 0;
};

export const formatDateShort = (value?: string | null) => {
    const raw = String(value ?? '').trim();
    if (!raw) return '-';

    const [datePart] = raw.split(' ');
    return datePart || raw;
};

