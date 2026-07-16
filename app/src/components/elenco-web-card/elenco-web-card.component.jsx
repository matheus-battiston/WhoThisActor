import { useNavigate } from "react-router-dom";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./elenco-web-card.css";

export default function ElencoWebCard({ pessoa }) {
  const navigate = useNavigate();
  const foto = pessoa.urlImagem || undefined;

  return (
    <button
      type="button"
      className="elenco-web-card"
      onClick={() => navigate(`/ator/${pessoa.id}`)}
    >
      <LoadingImage
        src={foto}
        alt={pessoa.nome}
        className="elenco-web-card-foto"
        imgClassName="elenco-web-card-foto-img"
      />
      <span className="elenco-web-card-texto">
        <strong>{pessoa.nome}</strong>
        <span>{pessoa.nomePersonagem}</span>
      </span>
      <ChevronRightIcon className="elenco-web-card-seta" aria-hidden="true" />
    </button>
  );
}
