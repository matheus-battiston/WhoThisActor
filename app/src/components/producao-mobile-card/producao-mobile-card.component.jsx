import { useNavigate } from "react-router-dom";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./producao-mobile-card.css";

export default function ProducaoMobileCard({ producao, tipo }) {
  const navigate = useNavigate();
  const tipoMidia = tipo === "FILME" ? "MOVIE" : "TV";
  const poster = producao.posterLink || undefined;

  return (
    <button
      type="button"
      className="producao-mobile-card"
      onClick={() => navigate(`/exibirElenco/${tipoMidia}/${producao.id}`)}
    >
      <LoadingImage
        src={poster}
        alt={producao.nomeProducao}
        className="producao-mobile-card-poster"
      />
      <span className="producao-mobile-card-info">
        <strong>{producao.nomeProducao}</strong>
        {producao.nomePersonagem ? (
          <span>{producao.nomePersonagem}</span>
        ) : null}
      </span>
    </button>
  );
}
