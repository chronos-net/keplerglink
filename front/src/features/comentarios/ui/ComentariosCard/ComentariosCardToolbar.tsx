import { Calendar, Search } from 'lucide-react';
import s from "./ComentariosCardToolbar.module.css";

type Props = {
    query: string;
    year: string;
    qna: string;
    years: string[];
    qnas: string[];
    onQueryChange: (value: string) => void;
    onYearChange: (value: string) => void;
    onQnaChange: (value: string) => void;
};


export default function ComentariosCardToolbar({
    query,
    year,
    qna,
    years,
    qnas,
    onQueryChange,
    onYearChange,
    onQnaChange

}: Props) {
    return (

        <div className={s.toolbar}>
            <label className={s.searchField}>
                <Search size={16} className={s.fieldIcon} />
                <input
                    className={s.fieldInput}
                    type="text"
                    placeholder="Buscar por periodo, comentario, cheque, usuario..."
                    value={query}
                    onChange={(event) => onQueryChange(event.target.value)}
                />
            </label>

            <div className={s.toolbarFilters}>
                <label className={s.selectField}>
                    <Calendar size={16} className={s.fieldIcon} />
                    <select
                        className={s.fieldSelect}
                        value={year}
                        onChange={(event) => onYearChange(event.target.value)}
                    >
                        <option value="all">Todos los años</option>
                        {years.map((value) => (
                            <option key={value} value={value}>
                                {value}
                            </option>
                        ))}
                    </select>
                </label>

                <label className={s.selectField}>
                    <Calendar size={16} className={s.fieldIcon} />
                    <select
                        className={s.fieldSelect}
                        value={qna}
                        onChange={(event) => onQnaChange(event.target.value)}
                    >
                        <option value="all">Quincena</option>
                        {qnas.map((value) => (
                            <option key={value} value={value}>
                                {value}
                            </option>
                        ))}
                    </select>
                </label>
            </div>
        </div>
    )
}
