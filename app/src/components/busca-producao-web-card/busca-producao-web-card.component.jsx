import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./busca-producao-web-card.css";

export default function BuscaProducaoWebCard({
  titulo,
  descricao,
  imagem,
  onClick,
}) {
  return (
    <button type="button" className="busca-producao-web-card" onClick={onClick}>
      <LoadingImage
        src={imagem}
        alt={titulo}
        className="busca-producao-web-card-imagem"
        imgClassName="busca-producao-web-card-img"
      />
      <span className="busca-producao-web-card-texto">
        <strong>{titulo}</strong>
        {descricao ? <span>{descricao}</span> : null}
      </span>
      <ChevronRightIcon
        className="busca-producao-web-card-seta"
        aria-hidden="true"
      />
    </button>
  );
}
