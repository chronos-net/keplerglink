# Codex Context

## Objetivo actual

Este proyecto esta trabajando el modal/drawer de comentarios dentro del modulo de comprobantes.

Ruta principal:

`src/features/comprobantes/ui/CommentsDrawer`

La idea actual del drawer es:

1. Mostrar resumen del servidor publico.
2. Mostrar importes del periodo.
3. Mostrar registros del periodo con filtros:
   `Todos`, `Con comentario`, `Sin comentario`.
4. Dar prioridad visual al estado del comentario, los importes y el acceso al comentario.
5. Mantener los datos administrativos como informacion secundaria.

## Arquitectura actual del drawer

### Data / API

La data remota se consume desde:

`src/features/comentarios/hook/useComentarios.ts`

Ese hook se usa desde:

`src/features/comprobantes/ui/ReciboHeader.tsx`

Y termina alimentando:

`CommentsModal -> CommentsDrawerView`

### Tipos

Los tipos compartidos del drawer ya viven en:

`src/features/comprobantes/types/commentsDrawer.types.ts`

Tipos principales:

- `CommentItem`
- `CommentsHeader`

### Utils

Las funciones puras del drawer viven en:

`src/features/comprobantes/utils/commentsDrawer.utils.ts`

Funciones actuales:

- `pick`
- `isZeroAccount`
- `pickAccount`
- `money`
- `sumMoney`
- `dateFromComment`
- `formatDate`
- `itemDate`
- `hasCommentText`

### Hook de vista

La logica de interaccion y datos derivados del drawer vive en:

`src/features/comprobantes/ui/CommentsDrawer/hooks/useCommentsDrawerView.ts`

Responsabilidades actuales:

- filtro actual
- conteo con comentario / sin comentario
- registros filtrados
- totales del periodo
- control de expansion de comentario por card

## Division actual de componentes

Dentro de:

`src/features/comprobantes/ui/CommentsDrawer/components`

componentes actuales:

- `CommentsDrawerHeader`
- `CommentsLoadingState`
- `CommentsErrorState`
- `CommentsSummaryHero`
- `CommentsAmountsSummary`
- `CommentsFilterRow`
- `CommentsEmptyState`
- `CommentRecordCard`

### Rol de cada uno

- `CommentsDrawerView`: orquestador del drawer.
- `CommentsDrawerHeader`: cabecera del modal.
- `CommentsSummaryHero`: nombre, clave y periodo.
- `CommentsAmountsSummary`: resumen de importes.
- `CommentsFilterRow`: filtros de registros.
- `CommentsEmptyState`: estado vacio.
- `CommentsErrorState`: estado de error.
- `CommentsLoadingState`: estado de carga.
- `CommentRecordCard`: card individual del registro.

## Estado actual

El refactor ya va encaminado y el drawer esta dividido en:

- `types`
- `utils`
- `hook de vista`
- `components`
- `view`

Esto ya sigue una separacion sana:

- `types` = contratos
- `utils` = funciones puras
- `hooks` = logica de UI / estado derivado
- `components` = piezas de presentacion
- `view` = composicion principal

## Pendientes cercanos

1. Limpiar JSX duplicado en `CommentRecordCard`.
2. Eliminar hacks temporales de CSS.
3. Compactar clases repetidas en `comments-drawer-v2.module.css`.
4. Corregir textos con encoding roto como `AdscripciÃ³n`.
5. Decidir si algunos componentes se quedan dentro de `CommentsDrawer/components` o si alguno merece subir de nivel.

## Regla de trabajo sugerida con Codex

Usar Codex para:

- revisar arquitectura
- revisar estado real del codigo
- proponer divisiones sanas de componentes
- hacer refactors chicos o medianos con contexto del repo
- validar si una carpeta, hook, util o tipo tiene sentido
- ejecutar cambios concretos y correr `tsc`

Usar ChatGPT para:

- brainstorming visual
- propuestas grandes de UX
- comparar alternativas de interfaz
- redactar prompts o ideas de flujo
- discutir patrones de forma mas abierta

## Regla practica para ahorrar contexto

Cuando se trabaje con Codex:

1. pasar solo los archivos del cambio actual
2. decir si es analisis, refactor o implementacion
3. evitar enviar demasiada historia conversacional si ya existe este documento
4. apoyarse en este archivo para retomar contexto rapido

## Nota importante

Este archivo sirve como memoria de trabajo del refactor del drawer de comentarios.
No sustituye la lectura del codigo real, pero si ayuda a retomar rapido el estado del proyecto sin gastar tanto contexto.
