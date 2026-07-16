import { LoadingImage } from "../loading-image/loading-image.component";
import "./busca-producao-card.css";

export default function BuscaProducaoCard({
  titulo,
  descricao,
  imagem,
  onClick,
}) {
  return (
    <button type="button" className="busca-producao-card" onClick={onClick}>
      <LoadingImage
        src={imagem}
        alt={titulo}
        className="busca-producao-card-imagem"
        imgClassName="busca-producao-card-img"
      />
      <span className="busca-producao-card-texto">
        <strong>{titulo}</strong>
        <span>{descricao}</span>
      </span>
    </button>
  );
}
