'use client';

import { useRef, useState } from "react";
import { toUpperFilter } from "@/features/comprobantes/utils/text";
import { PayrollService, type ServidorLite } from "@/features/comprobantes/services/payroll.service";
import { buscarPrestamosGlink } from "@/features/prestamos/services/prestamos.service";
import { allowNumericForSource } from "@/utils/autoCompleteSource.utils";
import { buscarPensionesGlink } from "@/features/pensiones/services/pensiones.service";
import { buscadorHistoricoGlink } from "@/features/historicos/services/historico.service";
import { buscadorHistoricoDavs } from "@/features/historico_davs/services/historico-davs.service";


const getErrorName = (err: unknown): string | null => {
  if (!err || typeof err !== 'object') return null;
  if ('name' in err && typeof (err as { name?: unknown }).name === 'string') {
    return (err as { name: string }).name;
  }
  return null;
}

export const useServidorAutocomplete = (params: {
  enabled: boolean;
  source?: "KEPLER" | "GLINK_PRESTAMOS" | "GLINK_PENSIONES" | "GLINK_HISTORICO" | "DAVS_HISTORICO";
  setText: (upper: string) => void;
  setResolvedNeyemp: (v: string | undefined) => void;

}) => {

  const { enabled, source, setText, setResolvedNeyemp } = params;
  const [suggestions, setSuggestions] = useState<ServidorLite[]>([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [searching, setSearching] = useState(false);
  const searchAbortRef = useRef<AbortController | null>(null);

  const handleTextChange = async (value: string) => {
    const upper = toUpperFilter(value);
    setText(upper);
    setResolvedNeyemp(undefined);

    if (!enabled) {
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }
    if (!source) {
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }

    const query = upper.trim();

    const isNumeric = /^\d+$/.test(query);
    const allowNumeric = allowNumericForSource(source);


    if (!query || query.length < 3 || (!allowNumeric && isNumeric)) {
      searchAbortRef.current?.abort();
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }

    searchAbortRef.current?.abort();
    const controller = new AbortController();
    searchAbortRef.current = controller;

    setSearching(true);
    try {
      let res: ServidorLite[] = [];

      switch (source) {
        case "KEPLER":
          res = await PayrollService.buscarServidoresPorNombre(query, {
            signal: controller.signal,
          });
          break;

        case "GLINK_PRESTAMOS":
          res = await buscarPrestamosGlink(query, {
            signal: controller.signal,
          });
          break;

        case "GLINK_PENSIONES":
          res = await buscarPensionesGlink(query, {
            signal: controller.signal,
          });
          break;

        case "GLINK_HISTORICO":
          res = await buscadorHistoricoGlink(query, {
            signal: controller.signal,
          });
          break;

        case "DAVS_HISTORICO":
          res = await buscadorHistoricoDavs(query, {
            signal: controller.signal,
          });
          break;

        default:
          res = [];
      }


      setSuggestions(res);
      setShowSuggestions(res.length > 0);
    } catch (err: unknown) {
      const name = getErrorName(err);
      if (name === "AbortError" || name === "CanceledError") return;
      setSuggestions([]);
      setShowSuggestions(false);
    } finally {
      setSearching(false);
    }
  };

  const closeSuggestions = () => {
    setShowSuggestions(false);
    setSuggestions([]);
  };

  return { suggestions, showSuggestions, searching, handleTextChange, closeSuggestions };
}