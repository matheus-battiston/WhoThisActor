import { useNavigate } from "react-router-dom";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./producao-ator-web-card.css";

export default function ProducaoAtorWebCard({ producao, tipo }) {
  const navigate = useNavigate();
  const tipoMidia = tipo === "FILME" ? "MOVIE" : "TV";
  const poster = producao.posterLink || undefined;

  return (
    <button
      type="button"
      className="producao-ator-web-card"
      onClick={() => navigate(`/exibirElenco/${tipoMidia}/${producao.id}`)}
    >
      <LoadingImage
        src={poster}
        alt={producao.nomeProducao}
        className="producao-ator-web-card-poster"
      />
      <span className="producao-ator-web-card-info">
        <strong>{producao.nomeProducao}</strong>
        {producao.nomePersonagem ? (
          <span>{producao.nomePersonagem}</span>
        ) : null}
      </span>
    </button>
  );
}
