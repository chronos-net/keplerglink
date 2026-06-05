'use client';

import { useCallback, useState } from "react";
import { clampQuincena, computeCanSubmit, getRenderFlags } from "../utils/universalFilters.utils";
import { FilterQuery, ModoEnvioTextoUniversal } from "../types/types";

export function useUniverSalFilters(params: {
    initialText: string;
    initialAnio?: number | null;
    initialQuincena?: string | null;

    showYear: boolean;
    showQuincena: boolean;
    hideQuincena: boolean;

    requireText?: boolean;
    requireYear?: boolean;
    requireQuincena?: boolean;

    modoEnvioTexto?: ModoEnvioTextoUniversal;
}) {
    const {
        initialText,
        initialAnio,
        initialQuincena,
        showYear,
        showQuincena,
        hideQuincena,
        requireText,
        requireYear,
        requireQuincena,
        modoEnvioTexto = 'libre',
    } = params;

    const [text, setText] = useState(initialText);
    const [anioStr, setAnioStr] = useState(initialAnio != null ? String(initialAnio) : "");
    const [qStr, setQStr] = useState(initialQuincena ?? "");
    const [resolvedNeyemp, setResolvedNeyemp] = useState<string | undefined>();

    const { renderYear, renderQuincena } = getRenderFlags({ showYear, showQuincena, hideQuincena });

    const baseCanSubmit = computeCanSubmit({
        text,
        anioStr,
        qStr,
        renderYear,
        renderQuincena,
        requireText,
        requireYear,
        requireQuincena,
    });

    const rawText = text.trim();
    const hasText = rawText.length > 0;
    const hasNumericKey = /^\d{6,}$/.test(rawText);
    const canSubmitText =
        !hasText ||
        modoEnvioTexto === 'libre' ||
        (modoEnvioTexto === 'resueltoONumerico' && (!!resolvedNeyemp || hasNumericKey)) ||
        (modoEnvioTexto === 'soloNumerico' && hasNumericKey);

    const canSubmit = baseCanSubmit && canSubmitText;

    const buildQuery = useCallback(
        (overrides?: { text?: string; resolved?: string }): FilterQuery => {
            const t = (overrides?.text ?? text).trim();
            const anio = renderYear && anioStr ? Number(anioStr) : undefined;
            const quincena = renderQuincena ? (qStr ? clampQuincena(qStr) : undefined) : undefined;

            return {
                text: t || undefined,
                anio,
                quincena,
                neyempResolved: overrides?.resolved ?? resolvedNeyemp,
            };
        },
        [text, anioStr, qStr, resolvedNeyemp, renderYear, renderQuincena]
    );

    const canSubmitWithText = (nextText: string) => {
        const rawTextNext = nextText.trim();
        const hasTextNext = !!rawTextNext;
        const hasYearNext = !renderYear || !requireYear || !!anioStr.trim();
        const hasQNext = !renderQuincena || !requireQuincena || !!qStr.trim();
        const hasNumericKeyNext = /^\d{6,}$/.test(rawTextNext);
        const canSubmitTextNext =
            !hasTextNext ||
            modoEnvioTexto === 'libre' ||
            modoEnvioTexto === 'resueltoONumerico' ||
            (modoEnvioTexto === 'soloNumerico' && hasNumericKeyNext);

        return hasYearNext && hasQNext && (!requireText || hasTextNext) && canSubmitTextNext;
    };

    return {
        text, setText,
        anioStr, setAnioStr,
        qStr, setQStr,
        resolvedNeyemp, setResolvedNeyemp,

        renderYear,
        renderQuincena,
        canSubmit,

        buildQuery,
        canSubmitWithText,
    };
}
