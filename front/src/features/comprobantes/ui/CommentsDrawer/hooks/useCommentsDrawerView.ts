import { useCallback, useMemo, useState } from 'react';

import type {
    GetComentariosCabecera,
    GetComentariosValor,
} from '@/features/comentarios/types/comentarios.get.types';

import { hasCommentText, sumMoney } from '@/features/comprobantes/utils/commentsDrawer.utils';

export type CommentsDrawerFilter = 'all' | 'with' | 'without';

type Params = {
    items: GetComentariosValor[];
    header?: GetComentariosCabecera | null;
    loading: boolean;
    error: string | null;
};

const buildRecordKey = (
    item: GetComentariosValor,
    index: number,
) =>
    `${item.id ?? item.qna ?? index}-${item.numCuenta ?? item.formaPago ?? index}-${index}`;

export const useCommentsDrawerView = ({ items, loading, error }: Params) => {
    const [filter, setFilter] = useState<CommentsDrawerFilter>('all');
    const [openComments, setOpenComments] = useState<Record<string, boolean>>({});

    const totals = useMemo(
        () => ({
            totalInicial: sumMoney(items, 'importeInicial'),
            totalFinal: sumMoney(items, 'importeFinal'),
            totalDiferencia: sumMoney(items, 'diferencia'),
        }),
        [items]
    );

    const commentCounts = useMemo(() => {
        const withCommentsCount = items.filter(hasCommentText).length;

        return {
            withCommentsCount,
            withoutCommentsCount: Math.max(items.length - withCommentsCount, 0),
        };
    }, [items]);

    const filteredItems = useMemo(() => {
        if (filter === 'with') return items.filter(hasCommentText);
        if (filter === 'without') return items.filter((item) => !hasCommentText(item));
        return items;
    }, [items, filter]);

    const records = useMemo(() => {
        return filteredItems.map((item, index) => {
            const recordKey = buildRecordKey(item, index);

            return {
                item,
                recordKey,
                hasComment: hasCommentText(item),
                commentOpen: openComments[recordKey] ?? false,
            };
        });
    }, [filteredItems, openComments]);

    const toggleComment = useCallback((recordKey: string) => {
        setOpenComments((current) => ({
            ...current,
            [recordKey]: !current[recordKey],
        }));
    }, []);

    return {
        filter,
        setFilter,
        toggleComment,
        hasRows: !loading && !error && items.length > 0,
        filteredItems,
        records,
        ...totals,
        ...commentCounts,
    };
};