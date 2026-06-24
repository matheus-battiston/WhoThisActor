import "./filtro-favoritos.css";

export default function FiltroFavoritos({ ativo, onChange }) {
  return (
    <label className={`filtro-favoritos ${ativo ? "ativo" : ""}`}>
      <div className="filtro-favoritos-texto">
        <span className="filtro-favoritos-titulo">Somente favoritos</span>
        <span className="filtro-favoritos-descricao">
          Buscar nas series salvas
        </span>
      </div>

      <input
        className="filtro-favoritos-input"
        type="checkbox"
        checked={ativo}
        onChange={(event) => onChange(event.target.checked)}
      />

      <span className="filtro-favoritos-switch" aria-hidden="true">
        <span className="filtro-favoritos-thumb" />
      </span>
    </label>
  );
}
