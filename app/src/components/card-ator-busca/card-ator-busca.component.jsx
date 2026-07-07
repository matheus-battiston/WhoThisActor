import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./card-ator-busca.css";

export default function CardAtorBusca({
  titulo,
  descricao,
  imagem,
  onClick,
  formato = "poster",
}) {
  return (
    <button type="button" className="ator-busca-card" onClick={onClick}>
      <LoadingImage
        src={imagem}
        alt={titulo}
        className={`ator-busca-card-imagem ator-busca-card-imagem-${formato}`}
        imgClassName="ator-busca-card-img"
      />
      <span className="ator-busca-card-texto">
        <strong>{titulo}</strong>
        {descricao ? <span>{descricao}</span> : null}
      </span>
      <ChevronRightIcon className="ator-busca-card-seta" />
    </button>
  );
}
