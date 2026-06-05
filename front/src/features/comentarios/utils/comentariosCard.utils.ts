const moneyFormatter = new Intl.NumberFormat("es-MX", {
  style: "currency",
  currency: "MXN",
});

export const textOrDash = (value?: string | number | null) => {
  const raw = String(value ?? "").trim();
  return raw || "-";
};

export const formatMoney = (value?: number | null) => {
  if (typeof value !== "number" || Number.isNaN(value)) return "-";
  return moneyFormatter.format(value);
};

export const formatTitle = (value?: string | null) => {
  const raw = textOrDash(value);

  if (raw === "-") return raw;

  return raw
    .toLocaleLowerCase("es-MX")
    .split(" ")
    .map((word) => word.charAt(0).toLocaleUpperCase("es-MX") + word.slice(1))
    .join(" ");
};

export const getComentarioTone = (value?: string | null) => {
  const raw = textOrDash(value).toUpperCase();

  if (raw.includes("CANCEL")) return "cancel";
  if (raw.includes("NO COBRADO")) return "warning";
  if (raw.includes("COMENTARIO")) return "comment";

  return "default";
};

export const getComentarioKey = (
  item: {
    anio?: string | null;
    periodo?: string | null;
    cheque?: string | null;
    ads?: string | null;
    tipoRegistro?: string | null;
  },
  index: number
) => {
  return [
    item.anio ?? "NA",
    item.periodo ?? "NA",
    item.cheque ?? "NA",
    item.ads ?? "NA",
    item.tipoRegistro ?? "NA",
    index,
  ].join("-");
};

export const formatShortDate = (value?: string | null) => {
  const raw = String(value ?? "").trim();
  if (!raw) return "-";

  const date = new Date(raw);
  if (Number.isNaN(date.getTime())) return raw;

  return new Intl.DateTimeFormat("es-MX", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(date);
};

export const formatPeriodo = (item: { qna?: string | null; anio?: string | null }) => {
  return `QNA ${textOrDash(item.qna)}/${textOrDash(item.anio)}`;
};

export const truncateComment = (value?: string | null, max = 42) => {
  const text = String(value ?? "").trim();
  if (!text) return "-";
  if (text.length <= max) return text;
  return `${text.slice(0, max).trim()}...`;
};

