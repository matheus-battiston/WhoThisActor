import "./exibe-pessoa-personagem.css";
import { useNavigate } from "react-router-dom";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import { LoadingImage } from "../loading-image/loading-image.component";

export function ExibePessoaPersonagem({ pessoa }) {
  const navigate = useNavigate();
  const foto = pessoa.urlImagem
    ? URL_BASE_IMAGEM_TMDB + pessoa.urlImagem
    : undefined;

  function verAtor() {
    navigate(`/ator/${pessoa.id}`);
  }

  return (
    <div className="linha-ator" onClick={verAtor}>
      <LoadingImage
        src={foto}
        alt={pessoa.nome}
        className="image-wrapper-ator"
        imgClassName="image-ator"
      />
      <div className="texto-ator-personagem">
        <h2 className="nome-ator" title={pessoa.nome}>
          {pessoa.nome}
        </h2>
        <h3 className="nome-personagem" title={pessoa.nomePersonagem}>
          {pessoa.nomePersonagem}
        </h3>
      </div>
    </div>
  );
}

export default ExibePessoaPersonagem;
