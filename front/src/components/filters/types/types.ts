export type Option<T> = { value: T; label: string };

export type AutocompleteSource =
    | "KEPLER"
    | "GLINK_PRESTAMOS"
    | "GLINK_PENSIONES"
    | "GLINK_HISTORICO"
    | "DAVS_HISTORICO";

export type UniversalTextMode = 'free' | 'numericKey9';

export type ModoEnvioTextoUniversal =
    | 'libre'
    | 'resueltoONumerico'
    | 'soloNumerico';

export type FilterQuery = {
    text?: string;
    anio?: number;
    quincena?: string;
    neyempResolved?: string;
};


export type UniversalFiltersProps = {
    busy?: boolean;

    initialText?: string;
    initialAnio?: number | null;
    initialQuincena?: string | null;

    /**
     * (Legacy) si es true, oculta quincena.
     * Lo mantenemos para no romper pantallas.
     */
    hideQuincena?: boolean;

    /**
     * ✅ NUEVO: oculta año y/o quincena sin romper el componente.
     * - por default: true (se muestran)
     */
    showYear?: boolean;
    showQuincena?: boolean;


    yearOptions?: Option<number>[];
    quincenaOptions?: Option<string>[];

    textPlaceholder?: string;



    onFilter: (q: FilterQuery) => void;
    autocompleteSource?: AutocompleteSource;
    autoSubmitOnSelect?: boolean;


    /**
      * Define el comportamiento del input de texto.
      * - free: permite texto libre
      * - numericKey9: solo 9 caracteres numéricos
      */
    textMode?: UniversalTextMode;
    modoEnvioTexto?: ModoEnvioTextoUniversal;

    /** 🔒 Si es true, exige texto para poder filtrar */
    requireText?: boolean;
    requireYear?: boolean;
    requireQuincena?: boolean;

    // PDF para todos los modulos que se requieren 
    showPdfButton?: boolean;
    onGeneratePdf?: () => void;
    pdfBusy?: boolean;
    pdfLabel?: string;
};
