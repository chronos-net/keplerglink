import { AutocompleteSource } from "@/components/filters/types/types";


const SOURCES_ALLOW_NUMERIC = new Set<AutocompleteSource>([
  "GLINK_PRESTAMOS",
  "GLINK_PENSIONES",
  "GLINK_HISTORICO",
  "DAVS_HISTORICO",
]);

export const allowNumericForSource = (source?: AutocompleteSource) =>
  !!source && SOURCES_ALLOW_NUMERIC.has(source);

export const shouldShowKeyForSource = (source?: AutocompleteSource) =>
  allowNumericForSource(source); // hoy es la misma regla