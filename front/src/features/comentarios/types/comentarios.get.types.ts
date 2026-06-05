export type GetComentariosRequest = {
  neyemp: string;
  periodo: string;
  anio: number;
};

export type GetComentariosCabecera = {
  neyemp: string;
  nombreCompleto: string;
};

export type ComentariosCatalogo = {
  clave: string;
  descripcion: string;
};

export type GetComentariosValor = {
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
  adscripcion: ComentariosCatalogo;
  puesto: ComentariosCatalogo;
  lugarPago: ComentariosCatalogo;
};

export type GetComentariosResponse = {
  cabecera: GetComentariosCabecera;
  valores: GetComentariosValor[];
};
