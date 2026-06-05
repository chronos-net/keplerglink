import { NextResponse } from 'next/server'
import PDFDocument from 'pdfkit'
import { Buffer } from 'node:buffer'
import path from 'node:path'
import type { DescPlazasResponseDto } from '@/features/desc-plazas/types/desc-plazas.dto'

export const runtime = 'nodejs'

type PdfBody = {
  anio: number
  quincena: string
  data: DescPlazasResponseDto
}

function money(n?: number) {
  return (n ?? 0).toLocaleString('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  })
}

function dash(v?: string | number | null) {
  if (v === undefined || v === null || v === '') return '\u2014'
  return sanitizeUtf8(String(v))
}

/** Corrige secuencias mojibake frecuentes (UTF-8 mal interpretado). */
function sanitizeUtf8(text: string): string {
  return text
    .replace(/\u00E2\u20AC\u201D/g, '\u2014')
    .replace(/\u00E2\u20AC\u201C/g, '\u2014')
    .replace(/â€"/g, '\u2014')
    .replace(/â€"/g, '\u2014')
    .replace(/â€¢/g, '\u00B7')
}

export async function POST(req: Request) {
  try {
    const body = (await req.json()) as PdfBody
    const anio = body?.anio ?? new Date().getFullYear()
    const periodo = body?.quincena ? sanitizeUtf8(String(body.quincena)) : '\u2014'
    const data = body?.data

    if (!data) {
      return new NextResponse("Falta 'data' para generar PDF", { status: 400 })
    }

    const empleado = data.empleado
    const plazaPrincipal = data.plazaPrincipal
    const plazas = data.plazas ?? []
    const totales = data.totalGlobales

    const doc = new PDFDocument({
      size: 'LETTER',
      margins: { top: 40, bottom: 40, left: 40, right: 40 },
    })

    const chunks: Uint8Array[] = []
    doc.on('data', (chunk: Uint8Array) => chunks.push(chunk))

    const bufferPromise = new Promise<Buffer>((resolve, reject) => {
      doc.on('end', () => resolve(Buffer.concat(chunks.map((c) => Buffer.from(c)))))
      doc.on('error', (err: unknown) => reject(err))
    })

    const colors = {
      primary: '#000000',
      text: '#000000',
      muted: '#4B5563',
      border: '#D1D5DB',
      headerBg: '#F3F4F6',
      tableHeaderBg: '#E5E7EB',
      rowStripe: '#F9FAFB',
      accent: '#9F2141',
    }

    doc.lineWidth(0.5).strokeColor(colors.border)

    const pageWidth =
      doc.page.width - doc.page.margins.left - doc.page.margins.right
    const leftX = doc.page.margins.left
    const bottomLimit = doc.page.height - doc.page.margins.bottom - 20

    const ensureSpace = (needed: number) => {
      if (doc.y + needed > bottomLimit) {
        doc.addPage()
        doc.y = doc.page.margins.top
      }
    }

    /* —— Encabezado institucional —— */
    const leftLogoPath = path.join(process.cwd(), 'public', 'img/escudo_logo.png')
    const rightLogoPath = path.join(process.cwd(), 'public', 'img/logo.png')
    const headerTopY = doc.y
    const logoWidth = 68

    try {
      doc.image(leftLogoPath, leftX, headerTopY, { width: logoWidth })
    } catch {
      /* noop */
    }
    try {
      doc.image(rightLogoPath, leftX + pageWidth - logoWidth, headerTopY, {
        width: logoWidth,
      })
    } catch {
      /* noop */
    }

    const headerTextY = headerTopY + 12

    doc
      .font('Helvetica-Bold')
      .fontSize(12)
      .fillColor(colors.primary)
      .text('DIRECCION GENERAL DEL PERSONAL', leftX, headerTextY, {
        width: pageWidth,
        align: 'center',
      })

    doc
      .font('Helvetica')
      .fontSize(7)
      .fillColor(colors.muted)
        .text(
          'DESGLOSE DE PLAZAS DEL/DE LA SERVIDOR(A) PUBLICO(A)',
          leftX,
          headerTextY + 12,
          { width: pageWidth, align: 'center' }
        )

    const periodoLabel = 'PERIODO '
    const periodoValue = `${periodo} - ${anio}`
    const periodoFontSize = 7

    doc.font('Helvetica').fontSize(periodoFontSize)
    const labelWidth = doc.widthOfString(periodoLabel)
    doc.font('Helvetica-Bold').fontSize(periodoFontSize)
    const valueWidth = doc.widthOfString(periodoValue)
    const totalWidth = labelWidth + valueWidth
    const periodoX = leftX + (pageWidth - totalWidth) / 2
    const periodoY = headerTextY + 19

    doc
      .font('Helvetica')
      .fontSize(periodoFontSize)
      .fillColor(colors.muted)
      .text(periodoLabel, periodoX, periodoY, { continued: true })

    doc
      .font('Helvetica-Bold')
      .fontSize(periodoFontSize)
      .fillColor(colors.primary)
      .text(periodoValue)

    doc.y = headerTextY + 55

    /* —— Datos generales —— */
    const drawLabelValueRows = (
      startX: number,
      startY: number,
      colW: number,
      rows: [string, string][],
      labelW: number,
      rowH: number
    ) => {
      let y = startY
      rows.forEach(([label, value]) => {
        doc.rect(startX, y, labelW, rowH).fillAndStroke(colors.headerBg, colors.border)
        doc.rect(startX + labelW, y, colW - labelW, rowH).stroke()
        doc
          .font('Helvetica-Bold')
          .fontSize(6)
          .fillColor(colors.muted)
          .text(label, startX + 4, y + 3, { width: labelW - 6 })
        doc
          .font('Helvetica')
          .fontSize(6)
          .fillColor(colors.text)
          .text(value, startX + labelW + 4, y + 3, {
            width: colW - labelW - 8,
            align: 'right',
          })
        y += rowH
      })
      return y
    }

    const tableX = leftX
    const tableY = doc.y
    const tableW = pageWidth
    const rowH = 11
    const halfW = tableW / 2
    const labelW = 62
    const headerRowH = 12

    const personalRows: [string, string][] = [
      ['NOMBRE', dash(empleado.negnom)],
      ['RFC', dash(empleado.rfc)],
      ['CLAVE S.P.', dash(empleado.neyemp)],
    ]

    const pagoRows: [string, string][] = [
      ['ADSCRIPCION', dash(empleado.ads)],
      ['CHEQUE', dash(empleado.cheque)],
      ['BANCO', dash(empleado.banco)],
      ['CUENTA', dash(empleado.numCuenta)],
      ['RECIBO', dash(empleado.numRecibo)],
    ]

    const maxRows = Math.max(personalRows.length, pagoRows.length)
    const boxH = headerRowH + maxRows * rowH + rowH

    doc.rect(tableX, tableY, tableW, boxH).stroke()

    doc
      .rect(tableX, tableY, halfW, headerRowH)
      .fillAndStroke(colors.headerBg, colors.border)
    doc
      .rect(tableX + halfW, tableY, halfW, headerRowH)
      .fillAndStroke(colors.headerBg, colors.border)

    doc
      .font('Helvetica-Bold')
      .fontSize(6)
      .fillColor(colors.primary)
      .text('DATOS PERSONALES', tableX, tableY + 4, { width: halfW, align: 'center' })
    doc
      .font('Helvetica-Bold')
      .fontSize(6)
      .fillColor(colors.primary)
      .text('DATOS DE PAGO', tableX + halfW, tableY + 4, {
        width: halfW,
        align: 'center',
      })

    doc.moveTo(tableX + halfW, tableY).lineTo(tableX + halfW, tableY + boxH).stroke()

    drawLabelValueRows(tableX, tableY + headerRowH, halfW, personalRows, labelW, rowH)
    drawLabelValueRows(
      tableX + halfW,
      tableY + headerRowH,
      halfW,
      pagoRows,
      labelW,
      rowH
    )

    const ppY = tableY + headerRowH + maxRows * rowH
    const ppLabelW = 90
    doc.rect(tableX, ppY, ppLabelW, rowH).fillAndStroke(colors.headerBg, colors.border)
    doc.rect(tableX + ppLabelW, ppY, tableW / 4 - ppLabelW, rowH).stroke()
    doc
      .font('Helvetica-Bold')
      .fontSize(6)
      .fillColor(colors.muted)
      .text('PLAZA PRINCIPAL', tableX + 4, ppY + 3)
    doc
      .font('Helvetica')
      .fontSize(6)
      .fillColor(colors.text)
      .text(dash(plazaPrincipal?.plazaId), tableX + ppLabelW + 4, ppY + 3, {
        width: tableW / 4 - ppLabelW - 8,
        align: 'right',
      })

    const secX = tableX + tableW / 4
    const secLabelW = 80
    doc.rect(secX, ppY, secLabelW, rowH).fillAndStroke(colors.headerBg, colors.border)
    doc.rect(secX + secLabelW, ppY, tableW / 4 - secLabelW, rowH).stroke()
    doc
      .font('Helvetica-Bold')
      .fontSize(6)
      .fillColor(colors.muted)
      .text('SEC. PRINCIPAL', secX + 4, ppY + 3)
    doc
      .font('Helvetica')
      .fontSize(6)
      .fillColor(colors.text)
      .text(dash(plazaPrincipal?.secuenciaPlaza), secX + secLabelW + 4, ppY + 3, {
        width: tableW / 4 - secLabelW - 8,
        align: 'right',
      })

    doc.y = tableY + boxH + 8

    /* —— Totales globales —— */
    const totalsGlobal: [string, string][] = [
      ['TOTAL PERCEPCIONES', money(totales.percepciones)],
      ['TOTAL DEDUCCIONES', money(totales.deducciones)],
      ['NETO GLOBAL', money(totales.neto)],
    ]

    const totalsY = doc.y
    const totalsH = 20
    const totalsColW = tableW / totalsGlobal.length
    let tX = tableX

    totalsGlobal.forEach(([label, value]) => {
      const isNeto = label.includes('NETO')
      doc
        .rect(tX, totalsY, totalsColW, totalsH)
        .fillAndStroke(isNeto ? colors.tableHeaderBg : colors.rowStripe, colors.border)
      doc
        .font('Helvetica')
        .fontSize(6)
        .fillColor('#6B7280')
        .text(label, tX + 6, totalsY + 4, { width: totalsColW - 12 })
      doc
        .font(isNeto ? 'Helvetica-Bold' : 'Helvetica')
        .fontSize(7)
        .fillColor(colors.text)
        .text(value, tX + 6, totalsY + 11, { width: totalsColW - 12, align: 'right' })
      tX += totalsColW
    })

    doc.y = totalsY + totalsH + 14

    /* —— Por plaza —— */
    const tableGap = 5
    const tableWidth2 = (pageWidth - tableGap) / 2
    const tableTitleH = 16
    const columnHeaderH = 10
    const rowHeight2 = 10
    const plazaMetaH = 14
    const plazaTotalsH = 18

    const puestoText = (puesto?: string, leyenda?: string) => {
      const p = dash(puesto)
      const l = leyenda?.trim() ? sanitizeUtf8(leyenda.trim()) : ''
      if (!l) return p
      if (p === '\u2014') return l
      return `${p} \u00B7 ${l}`
    }

    plazas.forEach((pz, index) => {
      const percepItems = pz.percepciones?.percepciones ?? []
      const deducItems = pz.deducciones?.deducciones ?? []
      const maxConceptRows = Math.max(percepItems.length, deducItems.length, 1)
      const sectionH =
        plazaMetaH +
        tableTitleH +
        columnHeaderH +
        maxConceptRows * rowHeight2 +
        plazaTotalsH +
        24

      ensureSpace(sectionH)

      /* Meta plaza */
      const metaY = doc.y
      doc
        .rect(tableX, metaY, tableW, plazaMetaH)
        .fillAndStroke('#FDF2F5', colors.border)

      doc
        .font('Helvetica-Bold')
        .fontSize(6)
        .fillColor(colors.text)
        .text(
          `PLAZA ${index + 1}  |  Plaza: ${dash(pz.plazaId)}  |  Sec: ${dash(pz.secuenciaPlaza)}  |  Puesto: ${puestoText(pz.puesto, pz.leyendaPuesto)}  |  Lug. pago: ${dash(pz.lugpago)}  |  Centro: ${dash(pz.centroTrabajo)}`,
          tableX + 6,
          metaY + 4,
          { width: tableW - 12 }
        )

      doc.y = metaY + plazaMetaH + 4

      const tablesTopY2 = doc.y
      const xLeft2 = leftX
      const xRight2 = leftX + tableWidth2 + tableGap

      const drawTableTitle = (x: number, title: string) => {
        doc
          .rect(x, tablesTopY2, tableWidth2, tableTitleH)
          .fillAndStroke(colors.headerBg, colors.border)
        doc
          .font('Helvetica-Bold')
          .fontSize(7)
          .fillColor(colors.primary)
          .text(title.toUpperCase(), x + 6, tablesTopY2 + 5, {
            width: tableWidth2 - 12,
            align: 'left',
          })
      }

      const drawColumnHeader = (x: number) => {
        const y = tablesTopY2 + tableTitleH
        doc
          .rect(x, y, tableWidth2, columnHeaderH)
          .fillAndStroke(colors.tableHeaderBg, colors.border)
        doc
          .font('Helvetica-Bold')
          .fontSize(6)
          .fillColor('#4B5563')
          .text('CLAVE', x + 6, y + 2)
        doc.text('IMPORTE', x + tableWidth2 - 56, y + 2, { width: 50, align: 'right' })
      }

      drawTableTitle(xLeft2, 'Percepciones')
      drawTableTitle(xRight2, 'Deducciones')
      drawColumnHeader(xLeft2)
      drawColumnHeader(xRight2)

      const rowsStartY2 = tablesTopY2 + tableTitleH + columnHeaderH
      let yLeft2 = rowsStartY2
      let yRight2 = rowsStartY2

      if (percepItems.length === 0) {
        doc.rect(xLeft2, yLeft2, tableWidth2, rowHeight2).stroke()
        doc
          .font('Helvetica')
          .fontSize(6)
          .fillColor(colors.muted)
          .text('Sin percepciones', xLeft2 + 6, yLeft2 + 3)
        yLeft2 += rowHeight2
      } else {
        percepItems.forEach((p) => {
          doc.rect(xLeft2, yLeft2, tableWidth2, rowHeight2).stroke()
          doc
            .font('Helvetica')
            .fontSize(6)
            .fillColor(colors.text)
            .text(dash(p.codigo), xLeft2 + 6, yLeft2 + 3)
          doc.text(money(p.importe), xLeft2 + tableWidth2 - 56, yLeft2 + 3, {
            width: 50,
            align: 'right',
          })
          yLeft2 += rowHeight2
        })
      }

      if (deducItems.length === 0) {
        doc.rect(xRight2, yRight2, tableWidth2, rowHeight2).stroke()
        doc
          .font('Helvetica')
          .fontSize(6)
          .fillColor(colors.muted)
          .text('Sin deducciones', xRight2 + 6, yRight2 + 3)
        yRight2 += rowHeight2
      } else {
        deducItems.forEach((d) => {
          doc.rect(xRight2, yRight2, tableWidth2, rowHeight2).stroke()
          doc
            .font('Helvetica')
            .fontSize(6)
            .fillColor(colors.text)
            .text(dash(d.codigo), xRight2 + 6, yRight2 + 3)
          doc.text(money(d.importe), xRight2 + tableWidth2 - 56, yRight2 + 3, {
            width: 50,
            align: 'right',
          })
          yRight2 += rowHeight2
        })
      }

      const tablesBottomY2 = Math.max(yLeft2, yRight2)
      doc.rect(xLeft2, tablesTopY2, tableWidth2, tablesBottomY2 - tablesTopY2).stroke()
      doc.rect(xRight2, tablesTopY2, tableWidth2, tablesBottomY2 - tablesTopY2).stroke()

      const colImporteOffset = tableWidth2 - 56
      doc
        .moveTo(xLeft2 + colImporteOffset, tablesTopY2 + tableTitleH)
        .lineTo(xLeft2 + colImporteOffset, tablesBottomY2)
        .stroke()
      doc
        .moveTo(xRight2 + colImporteOffset, tablesTopY2 + tableTitleH)
        .lineTo(xRight2 + colImporteOffset, tablesBottomY2)
        .stroke()

      doc.y = tablesBottomY2 + 6

      /* Totales plaza */
      const plazaTotals: [string, string][] = [
        ['TOTAL PERCEPCIONES', money(pz.percepciones?.total)],
        ['TOTAL DEDUCCIONES', money(pz.deducciones?.total)],
        ['NETO', money(pz.neto)],
      ]

      const ptY = doc.y
      const ptColW = tableW / plazaTotals.length
      let px = tableX

      plazaTotals.forEach(([label, value]) => {
        const isNeto = label === 'NETO'
        doc
          .rect(px, ptY, ptColW, plazaTotalsH)
          .fillAndStroke(isNeto ? colors.tableHeaderBg : colors.rowStripe, colors.border)
        doc
          .font('Helvetica')
          .fontSize(6)
          .fillColor('#6B7280')
          .text(label, px + 6, ptY + 3, { width: ptColW - 12 })
        doc
          .font(isNeto ? 'Helvetica-Bold' : 'Helvetica')
          .fontSize(7)
          .fillColor(colors.text)
          .text(value, px + 6, ptY + 10, { width: ptColW - 12, align: 'right' })
        px += ptColW
      })

      doc.y = ptY + plazaTotalsH + 12
    })

    /* —— Leyenda —— */
    ensureSpace(40)
    const now = new Date()
    const fechaHora = now.toLocaleString('es-MX', {
      dateStyle: 'long',
      timeStyle: 'short',
    })

    const leyenda =
      'Este documento se genera unicamente para consulta a traves del Sistema de Consulta de Recibos de Percepciones y Deducciones; ' +
      'no constituye comprobante oficial de pago ni tiene validez juridica. ' +
      `Fue elaborado automaticamente el ${fechaHora} en la Direccion General del Personal. ` +
      'Para cualquier aclaracion, tramite formal o seguimiento debera acudir a los canales oficiales de atencion de la Direccion General del Personal.'

    doc
      .font('Helvetica-Oblique')
      .fontSize(7)
      .fillColor('#6B7280')
      .text(leyenda, leftX, doc.y, { width: pageWidth, align: 'center' })

    doc.end()
    const buffer = await bufferPromise

    return new NextResponse(new Uint8Array(buffer), {
      status: 200,
      headers: {
        'Content-Type': 'application/pdf',
        'Content-Disposition': `attachment; filename="desglose_plazas_${anio}_${periodo}.pdf"`,
        'Cache-Control': 'no-store',
      },
    })
  } catch (e) {
    console.error('DESC PLAZAS PDF ERROR:', e)
    return new NextResponse('Error generando PDF', { status: 500 })
  }
}
