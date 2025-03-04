import { useNavigate, useParams } from "react-router-dom";
import { useGetElenco } from "../../api/hooks/useGetElenco/useGetElenco.hook";
import { useNomePersonagem } from "../../api/hooks/useNomePersonagem/useNomePersonagem.hook";
import { useGetSerieInfo } from "../../api/hooks/useGetSerieInfo/useGetSerieInfo.hook";
import { useEffect, useState } from "react";
import Loading from "../../components/loading/loading.component";
import SerieInfo from "../../components/serie-info/serie-info.component";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/imageTmdb";
import InputSearch from "../../components/pesquisa/pesquisa.component";
import "./exibir-elenco.css";
import ExibePessoaPersonagem from "../../components/exibe-pessoa-personagem/exibe-pessoa-personagem.component";
import { clearError } from "../../redux/store";
import { useDispatch, useSelector } from "react-redux";

export function ExibirElencoScreen() {
  const { changePersonagem, personagemNome } = useNomePersonagem();
  const { name, tipo } = useParams();
  const { pessoas, getElencoFunc } = useGetElenco();
  const { serie, getSerieInfoFunc } = useGetSerieInfo();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getElencoFunc(name, personagemNome, tipo);
  }, [getElencoFunc, name, personagemNome, tipo]);

  useEffect(() => {
    if (pessoas && !serie) {
      getSerieInfoFunc(name, tipo);
    }
  }, [pessoas, name, getSerieInfoFunc, serie, tipo]);

  useEffect(() => {
    if (serie && pessoas) {
      setLoading(false);
    }
  }, [serie, pessoas, setLoading]);

  return (
    <div className="container-personagens">
      {loading ? (
        <Loading />
      ) : (
        <div className="container-elenco">
          <SerieInfo
            imagem={URL_BASE_IMAGEM_TMDB + serie.imagem}
            nome={serie.nome}
          />
          <div className="input-personagem">
            <InputSearch
              onChange={changePersonagem}
              texto={"Pesquise seu personagem"}
              placeholder={"Alex Dunphy"}
            />
          </div>
          <div className="lista-personagens">
            {" "}
            {pessoas.map((item, index) => (
              <ExibePessoaPersonagem
                key={index}
                linkImagem={item.urlImagem}
                nomeAtor={item.nome}
                nomePersonagem={item.nomePersonagem}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default ExibirElencoScreen;
