// src/features/comprobantes/types/payroll.types.ts
export type ReciboRequest = {
  anio: number;
  quincena: string;
  neyemp?: string;
  nombreSp?: string;
};

// ===== RAW =====
type RawDatosPersonales = {
  nombre?: string;
  curp?: string;
  rfc?: string;
  issemym?: string;
  neyemp?: string;
};

type RawPlaza =
  | {
      centro_trabajo?: string;
      plaza?: string | number;
      ads?: string | number;
      lugar_pago?: string | number;
      categoria?: string;
      leyenda_puesto?: string;
      leyenda_adscripcion?: string;
    }
  | {
      plaza?: string;
      ads?: string | number;
      lugar_pago?: string | number;
      categoria?: string;
      leyenda_puesto?: string;
      leyenda_adscripcion?: string;
    };

type RawPer =
  | { per?: string; descPer?: string; imp?: number }
  | { codigo?: string; descripcion?: string; importe?: number };

type RawPerGroup = {
  total?: number;
  percepciones?: RawPer[];
};

type RawDed =
  | { ded?: string; descDed?: string; imp?: number }
  | { codigo?: string; descripcion?: string; importe?: number };

type RawDedGroup = {
  total?: number;
  deducciones?: RawDed[];
};

export type ReciboRaw = {
  datosPersonales?: RawDatosPersonales;
  datosPlazas?: RawPlaza[];
  datosCantidades?: {
    lugPago?: string | null;
    numCuenta?: string;
    totalPercep?: number;
    totalDed?: number;
    banco?: string;
    cheque?: string;
    numRecibo?: string;
    neto?: number;
  };
  percepciones?: RawPerGroup[];
  datosDeducciones?: RawDedGroup[];
  datosPlazasDesc?: Array<{
    plaza: string | number;
    categoria: string;
    leyenda_puesto: string;
    ads: string | number;
    importe: number;
  }>;
};

// ===== NORMALIZADO (lo que renderizas) =====
export type Recibo = {
  datosPersonales: {
    nombre: string;
    curp: string;
    rfc: string;
    issemym?: string;
    neyemp?: string;
  };
  datosPlazas: Array<{
    centro_trabajo: string;
    plaza: string;
    lugar_pago?: string;
    categoria?: string;
    leyenda_puesto?: string;
    leyenda_adscripcion?: string;
  }>;
  datosCantidades: {
    lugPago?: string | null;
    numCuenta?: string;
    totalPercep?: number;
    totalDed?: number;
    banco?: string;
    cheque?: string;
    numRecibo?: string;
    neto?: number;
  };
  percepciones: Array<{
    clave: string;
    descripcion: string;
    importe: number;
  }>;
  deducciones: Array<{
    clave: string;
    descripcion: string;
    importe: number;
  }>;
  datosPlazasDesc?: Array<{
    plaza: string;
    categoria: string;
    leyenda_puesto: string;
    ads: string;
    importe: number;
  }>;
};