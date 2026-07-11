import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./pessoa-web-card.css";

export default function PessoaWebCard({ nome, imagem, onClick }) {
  return (
    <button type="button" className="pessoa-web-card" onClick={onClick}>
      <LoadingImage
        src={imagem}
        alt={nome}
        className="pessoa-web-card-imagem"
        imgClassName="pessoa-web-card-img"
      />
      <span className="pessoa-web-card-texto">
        <strong>{nome}</strong>
      </span>
      <ChevronRightIcon className="pessoa-web-card-seta" aria-hidden="true" />
    </button>
  );
}
