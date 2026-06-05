// src/app/api/comprobantes/pdf/route.ts
import { NextResponse } from "next/server";
import PDFDocument from "pdfkit";
import { Buffer } from "node:buffer";
import path from "node:path";
import { ComprobanteItemUI, ComprobanteResponse } from "@/features/comprobantes/types/comprobantes.types";



export const runtime = "nodejs";

function money(n?: number) {
  return (n ?? 0).toLocaleString("es-MX", {
    style: "currency",
    currency: "MXN",
    minimumFractionDigits: 2,
  });
}

type PdfBody = {
  anio: number;
  quincena: string; // "01".."24"
  data: ComprobanteResponse;
};

type LegacyPlazaFields = {
  centro_trabajo?: string;
  lugar_pago?: string;
  plaza?: string | number;
  leyenda_adscripcion?: string;
  categoria?: string;
};

export async function POST(req: Request) {
  try {
    const body = (await req.json()) as PdfBody;

    const anio = body?.anio ?? new Date().getFullYear();
    const periodo = body?.quincena ?? "—";
    const data = body?.data;

    if (!data) {
      return new NextResponse("Falta 'data' para generar PDF", { status: 400 });
    }

    // ================================
    // MAPEOS (ANTES: personales/plazas/cantidades)
    // ================================
    const personales = data.empleado ?? ({} as ComprobanteResponse["empleado"]);
    const plaza0 = data.plaza ?? ({} as ComprobanteResponse["plaza"]);
    const plazaLegacy = plaza0 as ComprobanteResponse["plaza"] & LegacyPlazaFields;
    const recibo = data.recibo ?? ({} as ComprobanteResponse["recibo"]);
    const resumen = data.resumen ?? ({} as ComprobanteResponse["resumen"]);

    // Antes venían como {clave, descripcion, importe}
    // Ahora vienen como {codigo, descripcion, importe}
    const percepciones = (data.percepciones ?? []).map((p: ComprobanteItemUI) => ({
      clave: p.codigo,
      descripcion: p.descripcion,
      importe: p.importe,
    }));

    const deducciones = (data.deducciones ?? []).map((d: ComprobanteItemUI) => ({
      clave: d.codigo,
      descripcion: d.descripcion,
      importe: d.importe,
    }));

    // Campos que tu layout usa con nombres viejos
    const centroTrabajo =
      plazaLegacy.centro_trabajo ??
      plaza0.centroTrabajo ??
      plaza0.plazaId ??
      "—";

    const lugarPago =
      plazaLegacy.lugar_pago ??
      plaza0.lugarPago ??
      recibo.lugarPago ??
      "—";

    const plazaCode = String(plazaLegacy.plaza ?? plaza0.plazaId ?? "—");

    const adscripcion =
      plazaLegacy.leyenda_adscripcion ??
      plaza0.adsc ??
      plaza0.dependencia ??
      "—";

    // Antes: plaza0.categoria
    // Con tus types no existe "categoria" como tal; usamos puesto/leyendaPuesto como fallback.
    const categoria =
      plazaLegacy.categoria ??
      plaza0.leyendaPuesto ??
      plaza0.puesto ??
      "—";

    // Totales / recibo / cuenta
    const cantidades = {
      totalPercep: resumen.totalPercepciones,
      totalDed: resumen.totalDeducciones,
      neto: resumen.neto,
      numRecibo: recibo.numRecibo,
      numCuenta: recibo.numCuenta,
    };

    // ================================
    // PDF
    // ================================
    const doc = new PDFDocument({
      size: "LETTER",
      margins: { top: 40, bottom: 40, left: 40, right: 40 },
    });

    const chunks: Uint8Array[] = [];
    doc.on("data", (chunk: Uint8Array) => {
      chunks.push(chunk);
    });


    const bufferPromise = new Promise<Buffer>((resolve, reject) => {
      doc.on("end", () => resolve(Buffer.concat(chunks.map((c) => Buffer.from(c)))));
      doc.on("error", (err: unknown) => reject(err));
    });

    const colors = {
      primary: "#000000",
      text: "#000000",
      muted: "#000000",
      border: "#D1D5DB",
      headerBg: "#F3F4F6",
      tableHeaderBg: "#E5E7EB",
      rowStripe: "#F9FAFB",
    };

    doc.lineWidth(0.5).strokeColor(colors.border);

    const pageWidth =
      doc.page.width - doc.page.margins.left - doc.page.margins.right;
    const leftX = doc.page.margins.left;

    /* ========================================
       FONDO (MARCA DE AGUA CENTRADA)
    ======================================== */
    try {
      doc.save();
      doc.opacity(0.08);

      doc.opacity(1);
      doc.restore();
    } catch {
      // no rompe
    }

    /* ========================================
       ENCABEZADO + LOGOS
    ======================================== */
    const leftLogoPath = path.join(process.cwd(), "public", "img/escudo_logo.png");
    const rightLogoPath = path.join(process.cwd(), "public", "img/logo.png");

    const headerTopY = doc.y;
    const logoWidth = 68;

    try {
      doc.image(leftLogoPath, leftX, headerTopY, { width: logoWidth });
    } catch { }
    try {
      doc.image(rightLogoPath, leftX + pageWidth - logoWidth, headerTopY, {
        width: logoWidth,
      });
    } catch { }

    const headerTextY = headerTopY + 12;

    doc
      .font("Helvetica-Bold")
      .fontSize(12)
      .fillColor(colors.primary)
      .text("DIRECCION GENERAL DEL PERSONAL", leftX, headerTextY, {
        width: pageWidth,
        align: "center",
      });

    doc
      .font("Helvetica")
      .fontSize(7)
      .fillColor("#4B5563")
      .text(
        "PERCEPCIONES Y DEDUCCIONES DEL RECIBO DEL/DE LA SERVIDOR(A) PÚBLICO(A)",
        leftX,
        headerTextY + 12,
        { width: pageWidth, align: "center" }
      );

    // PERIODO label + value
    const periodoLabel = "PERIODO ";
    const periodoValue = `${periodo} - ${anio}`;
    const periodoFontSize = 7;

    doc.font("Helvetica").fontSize(periodoFontSize);
    const labelWidth = doc.widthOfString(periodoLabel);

    doc.font("Helvetica-Bold").fontSize(periodoFontSize);
    const valueWidth = doc.widthOfString(periodoValue);

    const totalWidth = labelWidth + valueWidth;
    const periodoX = leftX + (pageWidth - totalWidth) / 2;
    const periodoY = headerTextY + 19;

    doc
      .font("Helvetica")
      .fontSize(periodoFontSize)
      .fillColor("#4B5563")
      .text(periodoLabel, periodoX, periodoY, { continued: true });

    doc
      .font("Helvetica-Bold")
      .fontSize(periodoFontSize)
      .fillColor(colors.primary)
      .text(periodoValue);

    doc.moveDown();
    doc.y = headerTextY + 55;

    /* ========================================
       DATOS PERSONALES + LABORALES (CAJA)
    ======================================== */
    const tableX = leftX;
    const tableY = doc.y;
    const tableW = pageWidth;
    const rowH = 12;
    const halfW = tableW / 2;

    doc.rect(tableX, tableY, tableW, rowH * 4).stroke();

    doc
      .rect(tableX, tableY, halfW, rowH)
      .fillAndStroke(colors.headerBg, colors.border);
    doc
      .rect(tableX + halfW, tableY, halfW, rowH)
      .fillAndStroke(colors.headerBg, colors.border);

    doc
      .font("Helvetica-Bold")
      .fontSize(6)
      .fillColor(colors.primary)
      .text("DATOS PERSONALES", tableX, tableY + 5, {
        width: halfW,
        align: "center",
      });

    doc
      .font("Helvetica-Bold")
      .fontSize(6)
      .fillColor(colors.primary)
      .text("DATOS LABORALES", tableX + halfW, tableY + 5, {
        width: halfW,
        align: "center",
      });

    doc
      .moveTo(tableX + halfW, tableY)
      .lineTo(tableX + halfW, tableY + rowH * 2)
      .stroke();

    const labColX = tableX + tableW * 0.75;
    doc
      .moveTo(labColX, tableY + rowH)
      .lineTo(labColX, tableY + rowH * 5)
      .stroke();

    const personalRows: [string, string][] = [
      ["NOMBRE", personales.nombre ?? "—"],
      ["CURP", personales.curp ?? "—"],
      ["RFC", personales.rfc ?? "—"],
    ];

    let yP = tableY + rowH;
    const labelWPersonal = 50;

    personalRows.forEach(([label, value]) => {
      doc
        .rect(tableX, yP, labelWPersonal, rowH)
        .fillAndStroke(colors.headerBg, colors.border);

      doc.rect(tableX + labelWPersonal, yP, halfW - labelWPersonal, rowH).stroke();

      doc
        .font("Helvetica-Bold")
        .fontSize(6)
        .fillColor(colors.muted)
        .text(label, tableX + 6, yP + 4, { width: labelWPersonal - 1 });

      doc
        .font("Helvetica")
        .fontSize(6)
        .fillColor(colors.text)
        .text(value, tableX + labelWPersonal + 6, yP + 4, {
          width: halfW - labelWPersonal - 16,
          align: "right",
        });

      yP += rowH;
    });

    const lab1: [string, string][] = [
      ["CENTRO DE TRABAJO", centroTrabajo],
      ["PLAZA", plazaCode],
      ["LUGAR DE PAGO", lugarPago],
    ];

    let yL = tableY + rowH;
    const col1X = tableX + halfW;
    const col1W = tableW / 4;
    const labelWLab = 76;

    lab1.forEach(([label, value]) => {
      const labelWidth = Math.min(labelWLab, col1W - 40);

      doc
        .rect(col1X, yL, labelWidth, rowH)
        .fillAndStroke(colors.headerBg, colors.border);

      doc.rect(col1X + labelWidth, yL, col1W - labelWidth, rowH).stroke();

      doc
        .font("Helvetica-Bold")
        .fontSize(6)
        .fillColor(colors.muted)
        .text(label, col1X + 6, yL + 4, { width: labelWidth - 6 });

      doc
        .font("Helvetica")
        .fontSize(6)
        .fillColor(colors.text)
        .text(value, col1X + labelWidth + 6, yL + 4, {
          width: col1W - labelWidth - 10,
          align: "right",
        });

      yL += rowH;
    });

    const lab2: [string, string][] = [
      ["ISSEMYM", personales.iss ?? "—"],
      ["CATEGORIA", categoria],
      ["ADSCRIPCIÓN", adscripcion],
    ];

    let yR = tableY + rowH;
    const col2X = labColX;
    const col2W = tableW / 4;

    lab2.forEach(([label, value]) => {
      const labelWidth = Math.min(labelWLab, col2W - 10);

      doc
        .rect(col2X, yR, labelWidth, rowH)
        .fillAndStroke(colors.headerBg, colors.border);

      doc.rect(col2X + labelWidth, yR, col2W - labelWidth, rowH).stroke();

      doc
        .font("Helvetica-Bold")
        .fontSize(6)
        .fillColor(colors.muted)
        .text(label, col2X + 3, yR + 2, { width: labelWidth - 6 });

      doc
        .font("Helvetica")
        .fontSize(5)
        .fillColor(colors.text)
        .text(value, col2X + labelWidth + 3, yR + 2, {
          width: col2W - labelWidth - 4,
          align: "right",
        });

      yR += rowH;
    });

    doc.y = tableY + rowH * 4;

    /* ========================================
       TOTALES
    ======================================== */
    const totals: [string, string][] = [
      ["TOTAL DE PERCEPCIONES", money(cantidades.totalPercep)],
      ["TOTAL DE DEDUCCIONES", money(cantidades.totalDed)],
      ["NETO", money(cantidades.neto)],
      ["RECIBO", cantidades.numRecibo ?? "—"],
      ["N DE CUENTA", cantidades.numCuenta ?? "—"],
    ];

    const totalsY = doc.y;
    const totalsH = 20;
    const totalsColW = tableW / totals.length;
    let tX = tableX;

    totals.forEach(([label, value]) => {
      const isNeto = label === "NETO";

      const bgColor = isNeto ? "#E5E7EB" : colors.rowStripe;

      doc.rect(tX, totalsY, totalsColW, totalsH).fillAndStroke(bgColor, colors.border);

      doc
        .font("Helvetica")
        .fontSize(6)
        .fillColor("#6B7280")
        .text(label, tX + 6, totalsY + 4, { width: totalsColW - 12 });

      doc
        .font(isNeto ? "Helvetica-Bold" : "Helvetica")
        .fontSize(7)
        .fillColor(colors.text)
        .text(value, tX + 6, totalsY + 11, {
          width: totalsColW - 12,
          align: "right",
        });

      tX += totalsColW;
    });

    doc.y = totalsY + totalsH + 18;

    /* ========================================
       PERCEPCIONES Y DEDUCCIONES (DOS TABLAS)
    ======================================== */
    const tableGap = 5;
    const tableWidth2 = (pageWidth - tableGap) / 2;
    const xLeft2 = leftX;
    const xRight2 = leftX + tableWidth2 + tableGap;

    const tableTitleH = 18;
    const columnHeaderH = 10;
    const rowHeight2 = 10;

    const tablesTopY2 = doc.y;

    doc.strokeColor(colors.border).lineWidth(0.3);

    const drawTableTitle = (x: number, title: string) => {
      doc.rect(x, tablesTopY2, tableWidth2, tableTitleH).fillAndStroke(
        colors.headerBg,
        colors.border
      );

      doc
        .font("Helvetica-Bold")
        .fontSize(8)
        .fillColor(colors.primary)
        .text(title.toUpperCase(), x + 6, tablesTopY2 + 6, {
          width: tableWidth2 - 52,
          align: "left",
        });
    };

    const drawColumnHeader = (x: number) => {
      const y = tablesTopY2 + tableTitleH;

      doc.rect(x, y, tableWidth2, columnHeaderH).fillAndStroke(
        colors.tableHeaderBg,
        colors.border
      );

      doc
        .font("Helvetica-Bold")
        .fontSize(7)
        .fillColor("#4B5563")
        .text("CLAVE", x + 6, y + 2);

      doc.text("DESCRIPCIÓN", x + 46, y + 2);

      doc.text("IMPORTE", x + tableWidth2 - 56, y + 2, {
        width: 50,
        align: "right",
      });
    };

    drawTableTitle(xLeft2, "Percepciones");
    drawTableTitle(xRight2, "Deducciones");
    drawColumnHeader(xLeft2);
    drawColumnHeader(xRight2);

    const rowsStartY2 = tablesTopY2 + tableTitleH + columnHeaderH;
    let yLeft2 = rowsStartY2;
    let yRight2 = rowsStartY2;

    percepciones.forEach((p) => {
      doc.rect(xLeft2, yLeft2, tableWidth2, rowHeight2).stroke();

      doc.font("Helvetica").fontSize(6).fillColor(colors.text).text(
        p.clave ?? "",
        xLeft2 + 6,
        yLeft2 + 3
      );

      doc.text(p.descripcion ?? "", xLeft2 + 46, yLeft2 + 3, {
        width: tableWidth2 - 46 - 56,
      });

      doc.text(money(p.importe), xLeft2 + tableWidth2 - 56, yLeft2 + 3, {
        width: 50,
        align: "right",
      });

      yLeft2 += rowHeight2;
    });

    deducciones.forEach((d) => {
      doc.rect(xRight2, yRight2, tableWidth2, rowHeight2).stroke();

      doc.font("Helvetica").fontSize(6).fillColor(colors.text).text(
        d.clave ?? "",
        xRight2 + 6,
        yRight2 + 3
      );

      doc.text(d.descripcion ?? "", xRight2 + 46, yRight2 + 3, {
        width: tableWidth2 - 46 - 56,
      });

      doc.text(money(d.importe), xRight2 + tableWidth2 - 56, yRight2 + 3, {
        width: 50,
        align: "right",
      });

      yRight2 += rowHeight2;
    });

    const tablesBottomY2 = Math.max(yLeft2, yRight2);

    doc.rect(xLeft2, tablesTopY2, tableWidth2, tablesBottomY2 - tablesTopY2).stroke();
    doc
      .rect(xRight2, tablesTopY2, tableWidth2, tablesBottomY2 - tablesTopY2)
      .stroke();

    const colClaveOffset = 46;
    const colImporteOffset = tableWidth2 - 56;

    doc
      .moveTo(xLeft2 + colClaveOffset, tablesTopY2 + tableTitleH)
      .lineTo(xLeft2 + colClaveOffset, tablesBottomY2)
      .stroke();

    doc
      .moveTo(xLeft2 + colImporteOffset, tablesTopY2 + tableTitleH)
      .lineTo(xLeft2 + colImporteOffset, tablesBottomY2)
      .stroke();

    doc
      .moveTo(xRight2 + colClaveOffset, tablesTopY2 + tableTitleH)
      .lineTo(xRight2 + colClaveOffset, tablesBottomY2)
      .stroke();

    doc
      .moveTo(xRight2 + colImporteOffset, tablesTopY2 + tableTitleH)
      .lineTo(xRight2 + colImporteOffset, tablesBottomY2)
      .stroke();

    doc.y = tablesBottomY2 + 16;

    /* ========================================
       LEYENDA INFERIOR
    ======================================== */
    const now = new Date();
    const fechaHora = now.toLocaleString("es-MX", {
      dateStyle: "long",
      timeStyle: "short",
    });

    const leyenda =
      "Este documento se genera únicamente para consulta a través del Sistema de Consulta de Recibos de Percepciones y Deducciones; " +
      "no constituye comprobante oficial de pago ni tiene validez jurídica. " +
      `Fue elaborado automáticamente el ${fechaHora} en la Dirección General del Personal, ubicada en Miguel Hidalgo 216, Portal Madero, Edificio Monroy, cuarto piso. ` +
      "Para cualquier aclaración, trámite formal o seguimiento deberá acudir a los canales oficiales de atención de la Dirección General del Personal.";

    doc
      .font("Helvetica-Oblique")
      .fontSize(7)
      .fillColor("#6B7280")
      .text(leyenda, leftX, doc.y, {
        width: pageWidth,
        align: "center",
      });

    doc.end();
    const buffer = await bufferPromise;

    return new NextResponse(new Uint8Array(buffer), {
      status: 200,
      headers: {
        "Content-Type": "application/pdf",
        "Content-Disposition": `attachment; filename="comprobante_${anio}_${periodo}.pdf"`,
        "Cache-Control": "no-store",
      },
    });
  } catch (e) {
    console.error("PDF ERROR:", e);
    return new NextResponse("Error generando PDF", { status: 500 });
  }
}