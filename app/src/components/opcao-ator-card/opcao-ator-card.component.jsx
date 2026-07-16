import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./opcao-ator-card.css";

function montarImagem(path) {
  if (!path) return undefined;
  if (path.startsWith("http")) return path;

  return `${URL_BASE_IMAGEM_TMDB}${path}`;
}

export default function OpcaoAtorCard({ ator, onClick }) {
  return (
    <button type="button" className="opcao-ator-card" onClick={onClick}>
      <LoadingImage
        src={montarImagem(ator.imagem)}
        alt={ator.identidade}
        className="opcao-ator-card-imagem"
      />

      <span className="opcao-ator-card-conteudo">
        <strong>{ator.identidade}</strong>
        <ChevronRightIcon className="opcao-ator-card-seta" />
      </span>
    </button>
  );
}
