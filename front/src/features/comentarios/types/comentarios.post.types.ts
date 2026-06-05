export type PostCommentsRequest = {
  neyemp: string;
};

export type PostCommentsHeader = {
  neyemp: string;
  nombreCompleto: string;
  curp?: string | null;
  rfc?: string | null;
  imss?: string | null;
};

export type PostCommentsCatalogo = {
  clave: string | null;
  descripcion: string | null;
};

export type PostCommentItem = {
  nombreTabla: string;
  id: number | null;
  qna: string;
  anio: string;
  movimiento: string;
  formaPago: string;
  numCuenta: string | null;
  importeInicial: number;
  importeFinal: number;
  diferencia: number;
  pensionado: string | null;
  capturado: string | null;
  comentario: string | null;
  fechaCaptura: string | null;
  adscripcion: PostCommentsCatalogo | null;
  puesto: PostCommentsCatalogo | null;
  lugarPago: PostCommentsCatalogo | null;
};

export type PostCommentsResponse = {
  cabesera: PostCommentsHeader;
  valores: PostCommentItem[];
};
