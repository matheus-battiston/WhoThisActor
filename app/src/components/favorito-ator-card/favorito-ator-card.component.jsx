import { useNavigate } from "react-router-dom";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./favorito-ator-card.css";

function montarImagem(path) {
  return path || undefined;
}

export default function FavoritoAtorCard({
  ator,
  onRemoverFavorito,
  removendo = false,
}) {
  const navigate = useNavigate();
  const abrirAtor = () => navigate(`/ator/${ator.id}`);

  const abrirAtorComTeclado = (event) => {
    if (event.currentTarget !== event.target) return;
    if (event.key !== "Enter" && event.key !== " ") return;

    event.preventDefault();
    abrirAtor();
  };

  return (
    <article
      className="favorito-ator-card"
      role="button"
      tabIndex={0}
      onClick={abrirAtor}
      onKeyDown={abrirAtorComTeclado}
    >
      <span className="favorito-ator-card-imagem-wrapper">
        <LoadingImage
          src={montarImagem(ator.urlImagem)}
          alt={ator.nome}
          className="favorito-ator-card-imagem"
          imgClassName="favorito-ator-card-img"
        />

        <FavoritoIcon
          ativo
          disabled={removendo}
          className="favorito-ator-card-favorito"
          onClick={(event) => {
            event.stopPropagation();
            onRemoverFavorito(ator);
          }}
        />
      </span>

      <span className="favorito-ator-card-rodape">
        <strong>{ator.nome}</strong>
      </span>
    </article>
  );
}
