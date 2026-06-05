// src/app/api/anualizado/pdf/route.ts
import { NextResponse } from "next/server";
import PDFDocument from "pdfkit";
import { Buffer } from "node:buffer";
import path from "node:path";
import { AnualizadoResponse } from "@/features/anualizado/types/anualizado.dto";

export const runtime = "nodejs";

type Body = { anio: number; data: AnualizadoResponse };

function money(n?: number) {
  return (n ?? 0).toLocaleString("es-MX", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

function chunk<T>(arr: T[], size: number): T[][] {
  const out: T[][] = [];
  for (let i = 0; i < arr.length; i += size) out.push(arr.slice(i, i + size));
  return out.length ? out : [[]];
}

function normalizeCode(v: unknown) {
  return String(v ?? "").trim().padStart(4, "0");
}

type PlazaRowGetter = (
  z?: AnualizadoResponse["plazas"][number]
) => string | null | undefined;

export async function POST(req: Request) {
  try {
    const body = (await req.json()) as Body;

    if (!body?.anio || !body?.data) {
      return new NextResponse("Faltan parámetros (anio, data)", { status: 400 });
    }

    const { anio, data } = body;

    // ===== periodos (ordenados) =====
    const periodsAll = (data.periodos ?? [])
      .map((p) => String(p.periodo))
      .sort((a, b) => Number(a) - Number(b));

    const blocks = chunk(periodsAll, 12);

    // ===== indices =====
    const periodById: Record<string, AnualizadoResponse["periodos"][number]> =
      {};
    for (const p of data.periodos ?? []) periodById[String(p.periodo)] = p;

    const plazaByPeriod: Record<string, AnualizadoResponse["plazas"][number]> =
      {};
    for (const z of data.plazas ?? []) {
      if (z?.periodo) plazaByPeriod[String(z.periodo)] = z;
    }

    const percCatalog = data.catalogos?.percepcionesByCodigo ?? {};
    const dedCatalog = data.catalogos?.deduccionesByCodigo ?? {};

    // ===== claves únicas (desde periodos) =====
    const percKeysSet = new Set<string>();
    const dedKeysSet = new Set<string>();

    for (const per of data.periodos ?? []) {
      for (const it of per.percepciones ?? [])
        percKeysSet.add(normalizeCode(it.codigo));
      for (const it of per.deducciones ?? [])
        dedKeysSet.add(normalizeCode(it.codigo));
    }

    const percKeys = [...percKeysSet].sort((a, b) => a.localeCompare(b));
    const dedKeys = [...dedKeysSet].sort((a, b) => a.localeCompare(b));

    // ===== PDF =====
    const doc = new PDFDocument({
      size: "LEGAL",
      layout: "landscape",
      margins: { top: 40, bottom: 40, left: 20, right: 20 },
    });

    const chunks: Uint8Array[] = [];
    doc.on("data", (chunk: Uint8Array) => {
      chunks.push(chunk);
    });

    const pdfBufferPromise = new Promise<Buffer>((resolve, reject) => {
      doc.on("end", () => resolve(Buffer.concat(chunks.map((c) => Buffer.from(c)))));
      doc.on("error", (err: unknown) => reject(err));
    });

    // ===== assets =====
    const escudoPath = path.join(
      process.cwd(),
      "public",
      "img",
      "escudo_logo.png"
    );
    const logoEdomexPath = path.join(process.cwd(), "public", "img", "logo.png");

    // ===== helpers =====
    const TEXT_COLOR = "#000000";
    const ZERO_COLOR = "#b0b0b0";

    // Footer final (SOLO después del bloque 13-24 y SOLO una vez al final real del PDF)
    const FOOTER_NAME = "LIC. EDUARDO FLORES ARCHUNDIA";
    const FOOTER_ROLE = "Subdirector Control de Pagos";


    const drawFinalFooter = () => {
      const pageHeight = doc.page.height;
      const bottom = pageHeight - doc.page.margins.bottom;
      const yBase = bottom - 34;
      const safeTopForFooter = yBase - 10;

      if (doc.y > safeTopForFooter) doc.addPage();

      const pageWidth = doc.page.width;
      const leftMargin = doc.page.margins.left;
      const rightMargin = doc.page.margins.right;
      const centerWidth = pageWidth - leftMargin - rightMargin;

      const ph = doc.page.height;
      const btm = ph - doc.page.margins.bottom;


      const footerOffsetY = 20; // ✅ prueba 6–14 (entre más, más abajo)
      const lineY = btm - 54 + footerOffsetY;
      const nameY = btm - 46 + footerOffsetY;
      const roleY = btm - 36 + footerOffsetY;

      const lineW = 220;
      const lineX = leftMargin + centerWidth / 2 - lineW / 2;
      doc.save();
      doc.strokeColor("#9aa0a6").lineWidth(0.6);
      doc.moveTo(lineX, lineY);
      doc.lineTo(lineX + lineW, lineY);
      doc.stroke();
      doc.restore();

      doc
        .font("Helvetica-Bold")
        .fontSize(8)
        .fillColor("#2b2b2b")
        .text(FOOTER_NAME, leftMargin, nameY, {
          width: centerWidth,
          align: "center",
        });

      doc
        .font("Helvetica")
        .fontSize(7)
        .fillColor("#444444")
        .text(FOOTER_ROLE, leftMargin, roleY, {
          width: centerWidth,
          align: "center",
        });

      doc.font("Helvetica").fontSize(6).fillColor("#666666");
      // doc.text(buildLegend(), leftMargin, legendY, { width: centerWidth, align: "center" });

      doc.fillColor("#000000");
    };

    // ===== render por bloque (01-12 y 13-24) =====
    for (let pageIndex = 0; pageIndex < blocks.length; pageIndex++) {
      const periods = blocks[pageIndex];
      if (pageIndex > 0) doc.addPage();

      // variables de página (let porque cambian si hay cortes por overflow)
      let pageWidth = doc.page.width;

      let leftMargin = doc.page.margins.left;
      let rightMargin = doc.page.margins.right;
      let centerWidth = pageWidth - leftMargin - rightMargin;

      const bottomLimit = () => doc.page.height - doc.page.margins.bottom - 8;

      // ========= header institucional =========
      const drawPageHeader = () => {
        pageWidth = doc.page.width;
        leftMargin = doc.page.margins.left;
        rightMargin = doc.page.margins.right;
        centerWidth = pageWidth - leftMargin - rightMargin;

        // Logos
        const logosTopY = 22;
        const escudoWidth = 90;
        const logoWidth = 100;

        try {
          doc.image(escudoPath, leftMargin, logosTopY, { width: escudoWidth });
        } catch { }

        try {
          const logoX = pageWidth - rightMargin - logoWidth;
          doc.image(logoEdomexPath, logoX, logosTopY, { width: logoWidth });
        } catch { }

        const headerY = 30;

        doc
          .font("Helvetica-Bold")
          .fontSize(14)
          .fillColor(TEXT_COLOR)
          .text("DIRECCIÓN GENERAL DE PERSONAL", leftMargin, headerY, {
            width: centerWidth,
            align: "center",
          });

        const blockLabel =
          periods.length > 0 ? `${periods[0]}-${periods[periods.length - 1]}` : "";

        doc
          .font("Helvetica")
          .fontSize(10)
          .fillColor(TEXT_COLOR)
          .text(
            `PERCEPCIONES Y DEDUCCIONES ANUALIZADAS - ${anio} (${blockLabel})`,
            leftMargin,
            headerY + 13,
            { width: centerWidth, align: "center" }
          );

        // Datos personales
        const contentTopY = headerY + 30 + 18;
        doc.y = contentTopY;

        const e = data.empleado;

        const baseY = doc.y;
        doc
          .font("Helvetica-Bold")
          .fontSize(7)
          .fillColor(TEXT_COLOR)
          .text("NOMBRE:", leftMargin, baseY, { continued: true });
        doc
          .font("Helvetica")
          .fontSize(7)
          .fillColor("#555555")
          .text(` ${e?.nombre ?? ""}`, { continued: false });

        doc
          .font("Helvetica-Bold")
          .fontSize(7)
          .fillColor(TEXT_COLOR)
          .text("CLAVE:", leftMargin + 220, baseY, { continued: true });
        doc
          .font("Helvetica")
          .fontSize(7)
          .fillColor("#555555")
          .text(` ${e?.id ?? ""}`, { continued: false });

        doc
          .font("Helvetica-Bold")
          .fontSize(7)
          .fillColor(TEXT_COLOR)
          .text("RFC:", leftMargin + 340, baseY, { continued: true });
        doc
          .font("Helvetica")
          .fontSize(7)
          .fillColor("#555555")
          .text(` ${e?.rfc ?? ""}`, { continued: false });

        doc
          .font("Helvetica-Bold")
          .fontSize(7)
          .fillColor(TEXT_COLOR)
          .text("CURP:", leftMargin + 460, baseY, { continued: true });
        doc
          .font("Helvetica")
          .fontSize(7)
          .fillColor("#555555")
          .text(` ${e?.curp ?? ""}`, { continued: false });

        doc
          .font("Helvetica-Bold")
          .fontSize(7)
          .fillColor(TEXT_COLOR)
          .text("ISSEMYM:", leftMargin + 640, baseY, { continued: true });
        doc
          .font("Helvetica")
          .fontSize(7)
          .fillColor("#555555")
          .text(` ${e?.issemym ?? ""}`, { continued: false });

        doc.moveDown(0.8);
      };

      // ========== tabla: medidas ==========
      const claveColWidth = 24;
      const conceptColWidth = 150;
      const rowHeight = 9;
      const tableTopGap = 10;

      let tableTop = 0;
      let rowY = 0;

      let availableWidth = 0;
      let colWidth = 0;

      let xClave = 0;
      let xConcept = 0;
      let tableLeft = 0;
      let tableRight = 0;

      // === tuning visual (ajustes finos) ===
      const textYOffset = -0.8; // mueve texto dentro de la celda ( + baja / - sube )
      const hLineOffset = 2.6; // mueve línea horizontal ( + sube / - baja )
      const conceptPadLeft = 4; // padding en concepto
      const numPadRight = 3.5; // padding derecha en valores
      const gridXOffset = 0; // mueve TODAS las líneas verticales ( - izq / + der )

      const recalcTableMetrics = () => {
        pageWidth = doc.page.width;
        leftMargin = doc.page.margins.left;
        rightMargin = doc.page.margins.right;

        xClave = leftMargin;
        xConcept = xClave + claveColWidth;

        availableWidth =
          pageWidth -
          leftMargin -
          rightMargin -
          claveColWidth -
          conceptColWidth;

        colWidth = availableWidth / Math.max(1, periods.length);

        tableLeft = xClave;
        tableRight = xConcept + conceptColWidth + periods.length * colWidth;

        tableTop = doc.y + tableTopGap;
        rowY = tableTop;
      };

      const drawHorizontalDivider = (yLine: number) => {
        doc.save();
        doc.strokeColor("#dcdcdc").lineWidth(0.2);
        doc.moveTo(tableLeft, yLine);
        doc.lineTo(tableRight, yLine);
        doc.stroke();
        doc.restore();
      };

      const drawTableColumnsHeader = () => {
        doc.font("Helvetica-Bold").fontSize(6.5).fillColor(TEXT_COLOR);

        doc.text("CLAVE", xClave, rowY + textYOffset, { width: claveColWidth });

        doc.text("CONCEPTO", xConcept + conceptPadLeft, rowY + textYOffset, {
          width: conceptColWidth - conceptPadLeft,
        });

        periods.forEach((p, idx) => {
          const x = xConcept + conceptColWidth + idx * colWidth;
          doc.text(p, x, rowY + textYOffset, {
            width: colWidth - numPadRight,
            align: "right",
          });
        });

        drawHorizontalDivider(rowY + rowHeight - hLineOffset);
        rowY += rowHeight * 1.3;
      };

      const ensureSpace = () => {
        if (rowY + rowHeight <= bottomLimit()) return;

        doc.addPage();
        drawPageHeader();
        recalcTableMetrics();
        drawTableColumnsHeader();
      };

      const drawRowMoney = (
        clave: string,
        concepto: string,
        values: number[]
      ) => {
        ensureSpace();

        doc.font("Helvetica").fontSize(6.0).fillColor(TEXT_COLOR);

        doc.text(clave, xClave, rowY + textYOffset, { width: claveColWidth });

        doc.text(concepto, xConcept + conceptPadLeft, rowY + textYOffset, {
          width: conceptColWidth - conceptPadLeft,
        });

        periods.forEach((_, idx) => {
          const x = xConcept + conceptColWidth + idx * colWidth;
          const v = values[idx] ?? 0;

          doc.fillColor(v === 0 ? ZERO_COLOR : TEXT_COLOR);
          doc.text(v === 0 ? "" : money(v), x, rowY + textYOffset, {
            width: colWidth - numPadRight,
            align: "right",
          });
        });

        drawHorizontalDivider(rowY + rowHeight - hLineOffset);
        doc.fillColor(TEXT_COLOR);
        rowY += rowHeight;
      };

      const drawRowMoneyTotal = (concepto: string, values: number[]) => {
        ensureSpace();

        doc.font("Helvetica-Bold").fontSize(6.5).fillColor("#000000");

        doc.text("", xClave, rowY + textYOffset, { width: claveColWidth });

        doc.text(concepto, xConcept + conceptPadLeft, rowY + textYOffset, {
          width: conceptColWidth - conceptPadLeft,
        });

        periods.forEach((_, idx) => {
          const x = xConcept + conceptColWidth + idx * colWidth;
          const v = values[idx] ?? 0;

          doc.fillColor("#000000");
          doc.text(v === 0 ? "" : money(v), x, rowY + textYOffset, {
            width: colWidth - numPadRight,
            align: "right",
          });
        });

        drawHorizontalDivider(rowY + rowHeight - hLineOffset);
        doc.fillColor(TEXT_COLOR);
        rowY += rowHeight;
      };

      const drawRowText = (
        concepto: string,
        values: string[],
        opts?: { isPlazaRow?: boolean }
      ) => {
        ensureSpace();

        const fs = opts?.isPlazaRow ? 6.6 : 6.0;
        doc.font("Helvetica").fontSize(fs).fillColor(TEXT_COLOR);

        doc.text("", xClave, rowY + textYOffset, { width: claveColWidth });

        doc.text(concepto, xConcept + conceptPadLeft, rowY + textYOffset, {
          width: conceptColWidth - conceptPadLeft,
        });

        periods.forEach((_, idx) => {
          const x = xConcept + conceptColWidth + idx * colWidth;
          const v = values[idx] ?? "";

          doc.fillColor(v ? TEXT_COLOR : ZERO_COLOR);
          doc.text(v, x, rowY + textYOffset, {
            width: colWidth - numPadRight,
            align: "right",
          });
        });

        drawHorizontalDivider(rowY + rowHeight - hLineOffset);
        doc.fillColor(TEXT_COLOR);
        rowY += rowHeight;
      };

      const drawSectionTitle = (title: string) => {
        ensureSpace();
        rowY += rowHeight * 0.9;

        doc.font("Helvetica-Bold").fontSize(6.5).fillColor(TEXT_COLOR);

        doc.text(title, xConcept + conceptPadLeft, rowY + textYOffset, {
          width: conceptColWidth - conceptPadLeft,
        });

        drawHorizontalDivider(rowY + rowHeight - hLineOffset);
        rowY += rowHeight;
      };

      const drawVerticalGrid = (topY: number, bottomY: number) => {
        const xs: number[] = [];

        // líneas internas
        xs.push(xClave + claveColWidth + gridXOffset);
        xs.push(xConcept + conceptColWidth + gridXOffset);

        periods.forEach((_, idx) => {
          const x = xConcept + conceptColWidth + idx * colWidth;
          xs.push(x + gridXOffset);
        });

        doc.save();
        doc.strokeColor("#e0e0e0").lineWidth(0.2);
        xs.forEach((x) => {
          doc.moveTo(x, topY);
          doc.lineTo(x, bottomY);
        });
        doc.stroke();
        doc.restore();
      };

      // ========= Inicio de bloque =========
      drawPageHeader();
      recalcTableMetrics();

      const tableTopActual = tableTop;
      drawTableColumnsHeader();

      // ===== Plaza rows =====
      const headerRows: Array<{ label: string; get: PlazaRowGetter }> = [
        { label: "Plaza", get: (z) => z?.plaza },
        { label: "Adscripción", get: (z) => z?.ads },
        { label: "Cheque", get: (z) => z?.cheque },
        { label: "Categoría", get: (z) => z?.categoria },
        { label: "Centro de Trabajo", get: (z) => z?.centroTrabajo },
        { label: "Lugar de Pago", get: (z) => z?.lugarPago },
      ];

      for (const hr of headerRows) {
        const vals = periods.map((p) => {
          const z = plazaByPeriod[p];
          return hr.get(z) ?? "";
        });

        // 👈 solo estas filas con letra un poco más grande
        drawRowText(hr.label, vals, { isPlazaRow: true });
      }

      // ===== PERCEPCIONES =====
      drawSectionTitle("PERCEPCIONES");

      for (const code of percKeys) {
        const desc = percCatalog[code] ?? "-";
        const values = periods.map((p) => {
          const per = periodById[p];
          const item = (per?.percepciones ?? []).find(
            (x) => normalizeCode(x.codigo) === code
          );
          return Number(item?.importe) || 0;
        });

        if (values.every((n) => (Number(n) || 0) === 0)) continue;

        drawRowMoney(code, desc, values);
      }

      const totalPercepciones = periods.map((p) => {
        const per = periodById[p];
        return (per?.percepciones ?? []).reduce(
          (acc, it) => acc + (Number(it?.importe) || 0),
          0
        );
      });

      rowY += rowHeight * 0.2;
      drawRowMoneyTotal("Total de Percepciones", totalPercepciones);
      rowY += rowHeight * 0.2;

      // ===== DEDUCCIONES =====
      drawSectionTitle("DEDUCCIONES");

      for (const code of dedKeys) {
        const desc = dedCatalog[code] ?? "-";
        const values = periods.map((p) => {
          const per = periodById[p];
          const item = (per?.deducciones ?? []).find(
            (x) => normalizeCode(x.codigo) === code
          );
          return Number(item?.importe) || 0;
        });

        drawRowMoney(code, desc, values);
      }

      const totalDeducciones = periods.map((p) => {
        const per = periodById[p];
        return (per?.deducciones ?? []).reduce(
          (acc, it) => acc + (Number(it?.importe) || 0),
          0
        );
      });

      rowY += rowHeight * 0.2;
      drawRowMoneyTotal("Total de Deducciones", totalDeducciones);

      // ===== NETO =====
      const neto = periods.map(
        (_, idx) => (totalPercepciones[idx] ?? 0) - (totalDeducciones[idx] ?? 0)
      );
      drawRowMoneyTotal("Neto por período", neto);

      // Grid vertical (solo para la primera página del bloque; visualmente ok)
      const tableBottom = rowY - hLineOffset;
      drawVerticalGrid(tableTopActual, tableBottom);
    }

    // ✅ Footer final SOLO al final del PDF
    drawFinalFooter();

    doc.end();
    const pdfBuffer = await pdfBufferPromise;

    return new NextResponse(new Uint8Array(pdfBuffer), {
      status: 200,
      headers: {
        "Content-Type": "application/pdf",
        "Content-Disposition": `attachment; filename="anualizado_${anio}.pdf"`,
        "Cache-Control": "no-store",
      },
    });
  } catch (e) {
    console.error("PDF ERROR:", e);
    return new NextResponse("Error generando PDF", { status: 500 });
  }
}