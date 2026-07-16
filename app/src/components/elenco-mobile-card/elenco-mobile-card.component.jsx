import { useNavigate } from "react-router-dom";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./elenco-mobile-card.css";

export default function ElencoMobileCard({ pessoa, className = "" }) {
  const navigate = useNavigate();
  const foto = pessoa.urlImagem || undefined;

  return (
    <button
      type="button"
      className={["elenco-mobile-card", className].filter(Boolean).join(" ")}
      onClick={() => navigate(`/ator/${pessoa.id}`)}
    >
      <LoadingImage
        src={foto}
        alt={pessoa.nome}
        className="elenco-mobile-foto"
        imgClassName="elenco-mobile-foto-img"
      />
      <span className="elenco-mobile-texto">
        <strong>{pessoa.nome}</strong>
        <span>{pessoa.nomePersonagem}</span>
      </span>
      <ChevronRightIcon className="elenco-mobile-seta" aria-hidden="true" />
    </button>
  );
}
