import CloseIcon from "@mui/icons-material/Close";
import "./producao-overview-modal.css";

export default function ProducaoOverviewModal({ nome, overview, onClose }) {
  return (
    <div
      className="producao-overview-modal"
      role="dialog"
      aria-modal="true"
      aria-label={`Sinopse de ${nome}`}
      onClick={onClose}
    >
      <div
        className="producao-overview-modal-conteudo"
        onClick={(event) => event.stopPropagation()}
      >
        <button
          type="button"
          className="producao-overview-fechar"
          onClick={onClose}
          aria-label="Fechar sinopse"
        >
          <CloseIcon />
        </button>
        <strong>{nome}</strong>
        <span>{overview}</span>
      </div>
    </div>
  );
}
