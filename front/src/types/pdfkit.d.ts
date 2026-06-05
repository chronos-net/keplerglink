// src/types/pdfkit.d.ts
declare module 'pdfkit' {
  type PDFTextOptions = Record<string, unknown> & {
    width?: number;
    align?: 'left' | 'center' | 'right' | 'justify';
    continued?: boolean;
  };

  class PDFDocument {
    constructor(options?: Record<string, unknown>);

    on(event: 'data', listener: (chunk: Uint8Array) => void): this;
    on(event: 'end', listener: () => void): this;
    on(event: 'error', listener: (error: unknown) => void): this;

    pipe(destination: NodeJS.WritableStream): this;
    end(): void;

    font(src: string): this;
    fontSize(size: number): this;
    fillColor(color: string): this;
    strokeColor(color: string): this;
    lineWidth(width: number): this;
    opacity(value: number): this;

    text(text: string): this;
    text(text: string, options: PDFTextOptions): this;
    text(text: string, x: number, y: number): this;
    text(text: string, x: number, y: number, options: PDFTextOptions): this;

    image(
      src: string | Buffer,
      x?: number,
      y?: number,
      options?: Record<string, unknown>
    ): this;

    moveDown(lines?: number): this;
    addPage(options?: Record<string, unknown>): this;

    rect(x: number, y: number, width: number, height: number): this;
    roundedRect(x: number, y: number, width: number, height: number, radius: number): this;

    fill(color?: string): this;
    stroke(color?: string): this;
    fillAndStroke(fillColor: string, strokeColor: string): this;

    moveTo(x: number, y: number): this;
    lineTo(x: number, y: number): this;

    save(): this;
    restore(): this;

    widthOfString(text: string): number;

    x: number;
    y: number;
    page: {
      width: number;
      height: number;
      margins: {
        top: number;
        bottom: number;
        left: number;
        right: number;
      };
    };
  }

  export default PDFDocument;
}