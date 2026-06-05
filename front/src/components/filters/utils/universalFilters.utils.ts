export type RenderFlags = {
  renderYear: boolean;
  renderQuincena: boolean;
};

export function getRenderFlags(params: {
  showYear?: boolean;
  showQuincena?: boolean;
  hideQuincena?: boolean;
}): RenderFlags {
  const renderYear = params.showYear !== false;
  const renderQuincena = params.showQuincena !== false && !params.hideQuincena;
  return { renderYear, renderQuincena };
}

export function computeCanSubmit(params: {
  text: string;
  anioStr: string;
  qStr: string;
  renderYear: boolean;
  renderQuincena: boolean;
  requireText?: boolean;
  requireYear?: boolean;
  requireQuincena?: boolean;
}): boolean {
  const hasText = params.text.trim().length > 0;
  const hasYearValue = params.anioStr.trim().length > 0;
  const hasQValue = params.qStr.trim().length > 0;

  // defaults: si se renderiza, se requiere (a menos que lo overridees)
  const requireText = params.requireText ?? false;
  const requireYear = params.requireYear ?? params.renderYear;
  const requireQuincena = params.requireQuincena ?? params.renderQuincena;

  const okText = !requireText || hasText;

  // Si no se renderiza, no se valida. Si se renderiza y es requerido, debe tener valor.
  const okYear = !params.renderYear || !requireYear || hasYearValue;
  const okQ = !params.renderQuincena || !requireQuincena || hasQValue;

  return okText && okYear && okQ;
}

export function clampQuincena(v?: string) {
  const n = Math.max(1, Math.min(24, Number((v ?? "").replace(/\D/g, "") || "0")));
  return String(n).padStart(2, "0");
}
