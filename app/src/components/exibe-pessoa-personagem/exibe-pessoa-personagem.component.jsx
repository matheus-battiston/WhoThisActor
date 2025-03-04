import { useEffect, useState } from "react";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/imageTmdb";
import "./exibe-pessoa-personagem.css";
import padrao from "../../assets/images/image-not-found.jpg";
import { useNavigate } from "react-router-dom";

export function ExibePessoaPersonagem({
  linkImagem,
  nomeAtor,
  nomePersonagem,
}) {
  const [foto, setFoto] = useState(null);
  const navigate = useNavigate();

  function verAtor() {
    navigate(`/exibirProducoes/${encodeURIComponent(nomeAtor)}`);
  }

  useEffect(() => {
    if (linkImagem) {
      setFoto(URL_BASE_IMAGEM_TMDB + linkImagem);
    } else {
      setFoto(padrao);
    }
  }, [linkImagem]);

  return (
    <div className="linha-ator" onClick={verAtor}>
      <img
        src={foto}
        alt={""}
        className="image-ator"
        onError={(e) => (e.target.src = "../../assets/images/foto_padrao.jpg")}
      />
      <div className="producaoTexto">
        <h2 className="nome-ator">{nomeAtor}</h2>
        <h3 className="nome-personagem">{nomePersonagem}</h3>
      </div>
    </div>
  );
}

export default ExibePessoaPersonagem;
